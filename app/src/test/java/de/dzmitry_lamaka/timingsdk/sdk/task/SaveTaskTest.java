package de.dzmitry_lamaka.timingsdk.sdk.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.dzmitry_lamaka.timingsdk.sdk.persistance.Storage;
import de.dzmitry_lamaka.timingsdk.sdk.persistance.Time;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SaveTaskTest {
    @Mock
    private Storage storage;
    @Mock
    private Time time;

    private SaveTask testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new SaveTask(storage, time);
    }

    @Test
    public void perform() {
        testSubject.perform();
        verify(storage).save(time);
        verifyNoMoreInteractions(storage);
    }
}
