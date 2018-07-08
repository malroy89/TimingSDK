package de.dzmitry_lamaka.timingsdk.sdk.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import de.dzmitry_lamaka.timingsdk.sdk.SendTimeApiCall;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SendTaskTest {
    @Mock
    private SendTimeApiCall sendTimeApiCall;
    @Mock
    private Storage storage;
    @Mock
    private Time time;

    private SendTask testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new SendTask(sendTimeApiCall, storage, time);
    }

    @Test
    public void perform() throws Exception {
        testSubject.perform();
        verify(sendTimeApiCall).send(time);
        verify(storage).delete(time);
    }

    @Test(expected = IOException.class)
    public void perform_whenExceptionIsThrown() throws Exception {
        doThrow(new IOException()).when(sendTimeApiCall).send(time);
        testSubject.perform();
        verify(sendTimeApiCall).send(time);
        verify(storage, never()).delete(time);
    }
}
