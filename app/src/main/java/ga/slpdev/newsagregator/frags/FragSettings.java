package ga.slpdev.newsagregator.frags;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        settingsDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(getContext(), "isChecked", Toast.LENGTH_SHORT).show();
                SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(getActivity().getString(R.string.SETTINGS_DARK_THEME), b);
                editor.commit();
            }
        });

        return rootView;
    }
}
