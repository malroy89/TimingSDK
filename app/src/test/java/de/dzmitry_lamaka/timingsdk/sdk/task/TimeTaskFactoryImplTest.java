package de.dzmitry_lamaka.timingsdk.sdk.task;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

@RunWith(MockitoJUnitRunner.class)
public class TimeTaskFactoryImplTest {
    @Mock
    private Storage storage;
    @Mock
    private SendTimeApiCall sendTimeApiCall;

    private TimeTaskFactoryImpl testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new TimeTaskFactoryImpl(storage, sendTimeApiCall);
    }

    @Test(expected = IllegalStateException.class)
    public void createTask_whenSaveType_whenTimeIsNull_shouldThrowException() {
        testSubject.createTask(new Param(TaskType.SAVE));
    }

    @Test
    public void createTask_whenSaveType() {
        Assertions.assertThat(testSubject.createTask(new Param(TaskType.SAVE, new Time())))
            .isNotNull()
            .isInstanceOf(SaveTask.class);
    }

    @Test(expected = IllegalStateException.class)
    public void createTask_whenSendType_whenTimeIsNull_shouldThrowException() {
        testSubject.createTask(new Param(TaskType.SEND));
    }

    @Test
    public void createTask_whenSendType() {
        Assertions.assertThat(testSubject.createTask(new Param(TaskType.SEND, new Time())))
            .isNotNull()
            .isInstanceOf(SendTask.class);
    }

    @Test
    public void createTask_whenSendAllType() {
        Assertions.assertThat(testSubject.createTask(new Param(TaskType.SEND_ALL)))
            .isNotNull()
            .isInstanceOf(SendAllTask.class);
    }
}
