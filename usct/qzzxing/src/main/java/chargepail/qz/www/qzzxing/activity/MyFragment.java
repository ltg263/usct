package chargepail.qz.www.qzzxing.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import chargepail.qz.www.qzzxing.R;

/**
 * Created by LiuHT on 2018/3/14.
 */

public class MyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.activity_main,null);
        view.findViewById(R.id.main_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"test findview",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
