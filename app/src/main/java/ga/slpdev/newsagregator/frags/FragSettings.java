package ga.slpdev.newsagregator.frags;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ga.slpdev.newsagregator.R;

public class FragSettings extends Fragment {

    private static FragSettings instance;
    private View rootView;
    private Switch settingsDarkTheme;

    public FragSettings() {
    }

    public static FragSettings newInstance() {
        return new FragSettings();
    }

    public static FragSettings getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = newInstance();
            return instance;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_settings, container, false);

        settingsDarkTheme = rootView.findViewById(R.id.settings_dark_theme);

        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getBaseContext());


        settingsDarkTheme.setChecked(settings.getString("dark", "2").compareTo("2") != 0);

        settingsDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("dark", settingsDarkTheme.isChecked() ? "1" : "2");
                editor.apply();
                getContext().setTheme(settingsDarkTheme.isChecked() ? R.style.DarkTheme : R.style.AppTheme);
            }
        });

        return rootView;
    }
}
