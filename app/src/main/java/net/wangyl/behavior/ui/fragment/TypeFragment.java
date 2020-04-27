package net.wangyl.behavior.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.wangyl.behavior.R;
import net.wangyl.behavior.adapter.TypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TypeFragment extends Fragment {

    private Context mContext;

    public TypeFragment() {

    }

    public static TypeFragment newInstance() {
        TypeFragment fragment = new TypeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i + 1 + "");
        }
        TypeAdapter adapter = new TypeAdapter(mContext, list, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
