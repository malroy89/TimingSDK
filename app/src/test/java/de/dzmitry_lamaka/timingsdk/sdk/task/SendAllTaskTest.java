package de.dzmitry_lamaka.timingsdk.sdk.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SendAllTaskTest {
    @Mock
    private Storage storage;
    @Mock
    private TimeTaskFactory timeTaskFactory;

    private SendAllTask testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new SendAllTask(storage, timeTaskFactory);
    }

    @Test
    public void perform() throws Exception {
        final Task task = mock(Task.class);
        when(timeTaskFactory.createTask(any(Param.class))).thenReturn(task);
        when(storage.getAll()).thenReturn(Arrays.asList(mock(Time.class), mock(Time.class)));
        testSubject.perform();
        verify(timeTaskFactory, times(2)).createTask(any(Param.class));
        verify(task, times(2)).perform();
    }

    @Test
    public void perform_whenNoSavedData_shouldDoNothing() throws Exception {
        when(storage.getAll()).thenReturn(Collections.<Time>emptyList());
        testSubject.perform();
        verifyZeroInteractions(timeTaskFactory);
    }
}
