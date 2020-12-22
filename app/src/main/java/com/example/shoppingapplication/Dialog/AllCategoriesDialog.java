package com.example.shoppingapplication.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.shoppingapplication.Activity.SearchActivity;
import com.example.shoppingapplication.R;

import java.util.ArrayList;

public class AllCategoriesDialog extends DialogFragment {

    public static final String ALL_CATEGORIES = "categories";
    public static final String CALLING_ACTIVITY = "activity";
    private GetCategory getCategory;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_all_categories, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final String callingActivity = bundle.getString(CALLING_ACTIVITY);
            final ArrayList<String> categories = bundle.getStringArrayList(ALL_CATEGORIES);
            if (categories != null) {
                ListView listView = view.findViewById(R.id.listView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        categories);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        switch (callingActivity) {
                            case "main_activity":
                                Intent intent = new Intent(getActivity(), SearchActivity.class);
                                intent.putExtra("category", categories.get(position));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                                dismiss();
                                break;
                            case "search_activity":
                                try {
                                    getCategory = (GetCategory) getActivity();
                                    getCategory.onGetCategoryResult(categories.get(position));
                                    dismiss();
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }
        return builder.create();
    }

    public interface GetCategory {
        void onGetCategoryResult(String category);
    }
}
