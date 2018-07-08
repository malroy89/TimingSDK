package de.dzmitry_lamaka.timingsdk.sdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;
import de.dzmitry_lamaka.timingsdk.sdk.task.Param;
import de.dzmitry_lamaka.timingsdk.sdk.task.TaskType;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeProcessorTest {
    @Mock
    private TimeTaskFactory timeTaskFactory;
    @Mock
    private Worker saveWorker;
    @Mock
    private Worker sendWorker;
    @Mock
    private Logger logger;
    @Mock
    private Time time;
    @Captor
    private ArgumentCaptor<Param> paramCaptor;

    private TimeProcessor testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new TimeProcessor(timeTaskFactory, saveWorker, sendWorker, logger);
    }

    @Test
    public void process() {
        final Task task = mock(Task.class);
        when(timeTaskFactory.createTask(any(Param.class))).thenReturn(task);
        testSubject.process(time);
        verify(timeTaskFactory).createTask(paramCaptor.capture());
        final Param param = paramCaptor.getValue();
        assertThat(param.taskType).isEqualTo(TaskType.SAVE);
        assertThat(param.time).isEqualTo(time);
        verify(saveWorker).addTask(task);
    }

    @Test
    public void process_whenSameSecondsWithinMinute() {
        final Task task = mock(Task.class);
        when(timeTaskFactory.createTask(any(Param.class))).thenReturn(task);
        testSubject.process(time);
        verify(timeTaskFactory).createTask(paramCaptor.capture());
        final Param param = paramCaptor.getValue();
        assertThat(param.taskType).isEqualTo(TaskType.SAVE);
        assertThat(param.time).isEqualTo(time);
        verify(saveWorker).addTask(task);
        when(time.sameSecondWithinMinute(any(Time.class))).thenReturn(true);
        testSubject.process(time);
        verify(logger).d("Same second within the minute");
        verifyNoMoreInteractions(timeTaskFactory);
        verifyNoMoreInteractions(saveWorker);
    }

    @Test
    public void processExisting() {
        final Task task = mock(Task.class);
        when(timeTaskFactory.createTask(any(Param.class))).thenReturn(task);
        testSubject.processExisting();
        verify(timeTaskFactory).createTask(paramCaptor.capture());
        final Param param = paramCaptor.getValue();
        assertThat(param.taskType).isEqualTo(TaskType.SEND_ALL);
        assertThat(param.time).isNull();
        verify(sendWorker).addTask(task);
    }

    @Test
    public void release() {
        testSubject.release();
        verify(saveWorker).release();
        verify(sendWorker).release();
    }
}
