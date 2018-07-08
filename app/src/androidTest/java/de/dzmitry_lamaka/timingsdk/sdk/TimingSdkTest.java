package de.dzmitry_lamaka.timingsdk.sdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import de.dzmitry_lamaka.timingsdk.JustForTestsActivity;
import de.dzmitry_lamaka.timingsdk.sdk.di.TestConfig;
import de.dzmitry_lamaka.timingsdk.sdk.di.TestDependenciesProvider;
import de.dzmitry_lamaka.timingsdk.sdk.di.TestDependencyFactory;
import de.dzmitry_lamaka.timingsdk.sdk.di.Config;
import de.dzmitry_lamaka.timingsdk.sdk.di.DependenciesFactory;
import de.dzmitry_lamaka.timingsdk.sdk.di.Injector;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * It wasn't possible to test async code (which uses HandlerThread and Handler) using Robolectric, that's why this
 * UI-test was written
 */
@RunWith(AndroidJUnit4.class)
public class TimingSdkTest {
    private static final long TIMESTAMP_MILLIS = 1530826842701L; // Thursday, July 5, 2018 9:40:42.701 PM
    @Rule
    public final ActivityTestRule<JustForTestsActivity> activityTestRule = new ActivityTestRule<>(JustForTestsActivity.class);
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    @NonNull
    private final MockWebServer server = new MockWebServer();
    @Mock
    private TestDependenciesProvider testDependenciesProvider;

    private TimestampHelper timestampHelper;
    private DependenciesFactory testDependencyFactory;
    private TimingSdk timingSdk;

    @Before
    public void setUp() throws Exception {
        server.start();
        timestampHelper = new TimestampHelper(TIMESTAMP_MILLIS);
        final Config testConfig = new TestConfig(server.url("").toString(), "test_storage");
        testDependencyFactory = new TestDependencyFactory(testConfig, testDependenciesProvider);
        final Injector testInjector = new Injector(testDependencyFactory);
        timingSdk = TimingSdk.getInstance();
        ReflectionHelpers.setField(timingSdk, "injector", testInjector);
    }

    @Test
    public void process() throws Exception {
        server.enqueue(new MockResponse().setBody("{\"seconds\": \"42\", \"id\": 101}"));
        server.enqueue(new MockResponse().setBody("{\"seconds\": \"43\", \"id\": 101}"));
        server.enqueue(new MockResponse().setBody("{\"seconds\": \"44\", \"id\": 101}"));
        server.enqueue(new MockResponse().setResponseCode(400));
        when(testDependenciesProvider.createTime())
            .thenReturn(new Time(timestampHelper.getTimestamp())) // 42
            .thenReturn(new Time(timestampHelper.getTimestamp())) // 42
            .thenReturn(new Time(timestampHelper.plusOneSecond())) // 43
            .thenReturn(new Time(timestampHelper.plusOneSecond())) // 44
            .thenReturn(new Time(timestampHelper.plusOneSecond())); // 45
        final Context context = activityTestRule.getActivity();
        timingSdk.onTrackSeconds(context);
        timingSdk.onTrackSeconds(context);
        timingSdk.onTrackSeconds(context);
        timingSdk.onTrackSeconds(context);
        timingSdk.onTrackSeconds(context);
        ConditionWatcher.waitForCondition(new RequestInstruction(4));
        final int numberOfExpectedSuccessfulRequests = 3;
        for (int i = 0; i < numberOfExpectedSuccessfulRequests; i++) {
            RecordedRequest request = server.takeRequest();
            assertThat(request.getBody().readUtf8()).isIn("{\"seconds\":\"42\"}", "{\"seconds\":\"43\"}", "{\"seconds\":\"44\"}");
            assertThat(request.getMethod()).isEqualTo("POST");
        }
        final List<Time> saved = testDependencyFactory.newStorage(testDependencyFactory.getSharedPrefs(context)).getAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).toString()).isEqualTo("1530826845701");
    }

    @Test
    public void processExisting() throws Exception {
        final Context context = activityTestRule.getActivity();
        final Storage storage = testDependencyFactory.newStorage(testDependencyFactory.getSharedPrefs(context));
        storage.save(new Time(timestampHelper.getTimestamp()));
        storage.save(new Time(timestampHelper.plusOneSecond()));
        server.enqueue(new MockResponse().setBody("{\"seconds\": \"42\", \"id\": 101}"));
        server.enqueue(new MockResponse().setBody("{\"seconds\": \"43\", \"id\": 101}"));
        timingSdk.onResume(context);
        ConditionWatcher.waitForCondition(new RequestInstruction(2));
        final int numberOfExpectedSuccessfulRequests = 2;
        for (int i = 0; i < numberOfExpectedSuccessfulRequests; i++) {
            RecordedRequest request = server.takeRequest();
            assertThat(request.getBody().readUtf8()).isIn("{\"seconds\":\"42\"}", "{\"seconds\":\"43\"}");
            assertThat(request.getMethod()).isEqualTo("POST");
        }
        assertThat(storage.getAll()).hasSize(0);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        timestampHelper = null;
        testDependencyFactory.getSharedPrefs(activityTestRule.getActivity()).edit().clear().apply();
        ReflectionHelpers.setField(timingSdk, "processor", null);
    }

    private class RequestInstruction extends Instruction {
        private final int numberOfExpectedRequests;

        private RequestInstruction(final int numberOfExpectedRequests) {
            this.numberOfExpectedRequests = numberOfExpectedRequests;
        }

        @Override
        public String getDescription() {
            return "Should be " + numberOfExpectedRequests + " requests";
        }

        @Override
        public boolean checkCondition() {
            return server.getRequestCount() == numberOfExpectedRequests;
        }
    }
}
