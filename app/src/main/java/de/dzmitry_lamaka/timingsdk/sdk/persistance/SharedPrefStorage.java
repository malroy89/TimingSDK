package de.dzmitry_lamaka.timingsdk.sdk.persistance;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;

public class SharedPrefStorage implements Storage {
    @NonNull
    private final SharedPreferences sharedPrefs;

    public SharedPrefStorage(@NonNull final SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    @Override
    public void save(@NonNull final Time time) {
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        time.toPrefs(editor);
        editor.apply();
    }

    @Override
    public void delete(@NonNull final Time time) {
        sharedPrefs
            .edit()
            .remove(time.getPrefKey())
            .apply();
    }

    @NonNull
    @Override
    public List<Time> getAll() {
        return new TimeListBuilder(sharedPrefs.getAll()).build();
    }
}
