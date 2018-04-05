package ga.slpdev.newsagregator.frags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import ga.slpdev.newsagregator.R;


public class FragNewsPage extends Fragment {

    public FragNewsPage instance;
    View rootView;
    ImageView imagePreview;
    TextView description;
    String url = "", urlImage = "", text = "";

    public FragNewsPage() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            url = bundle.getString("url");
            urlImage = bundle.getString("urlImage");
            text = bundle.getString("text");
            Log.d("FragNewsPage", text);
        }

        Log.d("FragNewsPage", "Constructor");
    }

    public static FragNewsPage newInstance() {
        return new FragNewsPage();
    }

    public static FragNewsPage newInstance(Bundle __bundle) {
        FragNewsPage f = new FragNewsPage();
        f.setArguments(__bundle);
        return f;
    }

    public FragNewsPage getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = newInstance();
            return instance;
        }
    }

    public FragNewsPage getInstance(Bundle __bundle) {
        if (instance != null) {
            return instance;
        } else {
            instance = newInstance(__bundle);
            return instance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.news_page, container, false);
        imagePreview = rootView.findViewById(R.id.image_preview);
        description = rootView.findViewById(R.id.news_text);

        description.setText(this.getArguments().getString("text"));
        Glide.with(container.getContext()).load(getArguments().getString("urlImage")).thumbnail(0.5f).into(imagePreview);

        Log.d("FragNewsPage", "onCreateView");

        return rootView;
    }

}
