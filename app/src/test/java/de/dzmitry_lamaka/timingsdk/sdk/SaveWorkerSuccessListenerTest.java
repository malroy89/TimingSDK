package de.dzmitry_lamaka.timingsdk.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.task.Param;
import de.dzmitry_lamaka.timingsdk.sdk.task.SaveTask;
import de.dzmitry_lamaka.timingsdk.sdk.task.TaskType;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;
import de.dzmitry_lamaka.timingsdk.sdk.SaveWorkerSuccessListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SaveWorkerSuccessListenerTest {
    @Mock
    private Worker sendWorker;
    @Mock
    private TimeTaskFactory timeTaskFactory;
    @Captor
    private ArgumentCaptor<Param> paramCaptor;

    @Test
    public void onSuccess() {
        final Task newTask = mock(Task.class);
        when(timeTaskFactory.createTask(any(Param.class))).thenReturn(newTask);
        final SaveTask saveTask = mock(SaveTask.class);
        final Time time = mock(Time.class);
        when(saveTask.getTime()).thenReturn(time);
        final SaveWorkerSuccessListener testSubject = new SaveWorkerSuccessListener(sendWorker, timeTaskFactory);
        testSubject.onSuccess(saveTask);
        verify(timeTaskFactory).createTask(paramCaptor.capture());
        final Param captured = paramCaptor.getValue();
        assertThat(captured.time).isEqualTo(time);
        assertThat(captured.taskType).isEqualTo(TaskType.SEND);
        verify(sendWorker).addTask(newTask);
    }
}
