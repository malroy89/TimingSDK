package de.dzmitry_lamaka.timingsdk.sdk;

import android.support.annotation.NonNull;

import de.dzmitry_lamaka.timingsdk.sdk.task.Param;
import de.dzmitry_lamaka.timingsdk.sdk.task.SaveTask;
import de.dzmitry_lamaka.timingsdk.sdk.task.TaskType;
import de.dzmitry_lamaka.timingsdk.sdk.task.TimeTaskFactory;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Task;
import de.dzmitry_lamaka.timingsdk.sdk.worker.Worker;

public class SaveWorkerSuccessListener implements Worker.SuccessListener {
    @NonNull
    private final Worker sendWorker;
    @NonNull
    private final TimeTaskFactory timeTaskFactory;

    public SaveWorkerSuccessListener(@NonNull final Worker sendWorker, @NonNull final TimeTaskFactory timeTaskFactory) {
        this.sendWorker = sendWorker;
        this.timeTaskFactory = timeTaskFactory;
    }

    @Override
    public void onSuccess(@NonNull final Task task) {
        final SaveTask saveTask = (SaveTask) task;
        final Task sendTask = timeTaskFactory.createTask(new Param(TaskType.SEND, saveTask.getTime()));
        sendWorker.addTask(sendTask);
    }
}
