package net.wangyl.behavior.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.wangyl.behavior.R;

public class SwipeFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page1,container,false);
        View itemView = view.findViewById(R.id.itemview);
        itemView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"item被点击",Toast.LENGTH_SHORT).show();
    }
}
