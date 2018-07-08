package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharedPrefStorageTest {
    @Mock
    private SharedPreferences sharedPrefs;

    private SharedPrefStorage testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new SharedPrefStorage(sharedPrefs);
    }

    @Test
    public void save() {
        final Time time = mock(Time.class);
        final SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPrefs.edit()).thenReturn(editor);
        testSubject.save(time);
        verify(time).toPrefs(editor);
        verify(editor).apply();
    }

    @Test
    public void delete() {
        final Time time = mock(Time.class);
        final SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPrefs.edit()).thenReturn(editor);
        when(editor.remove(anyString())).thenReturn(editor);
        when(time.getPrefKey()).thenReturn("pref_key");
        testSubject.delete(time);
        verify(editor).apply();
    }
}
