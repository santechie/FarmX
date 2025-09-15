package com.ascentya.AsgriV2.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MulitSelectItemDialog extends BottomSheetDialogFragment {

    private String title;
    private List<Object> objects;
    private ArrayList<Object> selectedObjects;
    private Action action;

    public MulitSelectItemDialog(String title, List<Object> objects, Action action) {
        this.title = title;
        this.objects = objects;
        this.action = action;
        this.selectedObjects = new ArrayList<>();
    }

    public MulitSelectItemDialog(String title, List<Object> objects,
                                 ArrayList<Object> selectedObjects, Action action) {
        this.title = title;
        this.objects = objects;
        this.action = action;
        this.selectedObjects = selectedObjects;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_multi_select_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((ListView) view.findViewById(R.id.listView)).setAdapter(
                new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_checked, objects){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View itemView = super.getView(position, convertView, parent);
                        ((CheckedTextView) itemView.findViewById(android.R.id.text1))
                                .setChecked(selectedObjects.contains(getItem(position)));
                        ((TextView) itemView.findViewById(android.R.id.text1))
                                .setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        return itemView;
                    }
                }
        );

        ((ListView) view.findViewById(R.id.listView)).setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                        if (selectedObjects.contains(objects.get(i))){
                            selectedObjects.remove(objects.get(i));
                        }else {
                            selectedObjects.add(objects.get(i));
                        }
                        ((ArrayAdapter) ((ListView) view.findViewById(R.id.listView)).getAdapter())
                                .notifyDataSetChanged();
                    }
                }
        );

        view.findViewById(R.id.confirm).setOnClickListener(v -> {
            dismiss();
            action.onComplete(selectedObjects);
        });
    }

    public interface Action{
        void onComplete(ArrayList<Object> selectedObjects);
    }

}
