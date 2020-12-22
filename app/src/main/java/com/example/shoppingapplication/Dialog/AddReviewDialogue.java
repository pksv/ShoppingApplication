package com.example.shoppingapplication.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.shoppingapplication.Database.GroceryItem;
import com.example.shoppingapplication.Database.Review;
import com.example.shoppingapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.shoppingapplication.Activity.GroceryItemActivity.GROCERY_ITEM_KEY;

public class AddReviewDialogue extends DialogFragment {

    private AddReview addReview;
    private TextView txtItemName, txtWarning;
    private EditText etUserName, etReview;
    private Button btnAddReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_add_review, null);
        initViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final GroceryItem item = bundle.getParcelable(GROCERY_ITEM_KEY);
            if (item != null) {
                txtItemName.setText(item.getName());
                btnAddReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userName = etUserName.getText().toString();
                        String review = etReview.getText().toString();
                        String date = getCurrentDate();
                        if (userName.equals("") || review.equals("")) {
                            txtWarning.setText("Fill all blanks");
                            txtWarning.setVisibility(View.VISIBLE);
                        } else {
                            txtWarning.setVisibility(View.GONE);
                            try {
                                addReview = (AddReview) getActivity();
                                addReview.onAddReviewResult(new Review(item.getId(), userName, review, date));
                                dismiss();
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        return builder.create();
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY");
        return sdf.format(calendar.getTime());
    }

    private void initViews(View view) {
        txtItemName = view.findViewById(R.id.txtItemName);
        txtWarning = view.findViewById(R.id.txtWarning);
        etReview = view.findViewById(R.id.etReview);
        etUserName = view.findViewById(R.id.etUserName);
        btnAddReview = view.findViewById(R.id.btnAddReview);
    }

    public interface AddReview {
        void onAddReviewResult(Review review);
    }
}
