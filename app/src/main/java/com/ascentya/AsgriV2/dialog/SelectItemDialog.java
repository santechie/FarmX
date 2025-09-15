package com.ascentya.AsgriV2.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SelectItemDialog extends BottomSheetDialogFragment {

    private String title;
    private List<Object> objects;
    private Action action;

    public SelectItemDialog(String title, List<Object> objects, Action action) {
        this.title = title;
        this.objects = objects;
        this.action = action;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((ListView) view.findViewById(R.id.listView)).setAdapter(
                new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_1, objects){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View itemView = super.getView(position, convertView, parent);
                        ((TextView) itemView.findViewById(android.R.id.text1)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        return itemView;
                    }
                }
        );

        ((ListView) view.findViewById(R.id.listView)).setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dismiss();
                        action.onItemClicked(i);
                    }
                }
        );
    }

    public interface Action{
        void onItemClicked(int position);
    }

}
