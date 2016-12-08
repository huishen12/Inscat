package com.spacebunny.hshen.inscat.view.favorite_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.spacebunny.hshen.inscat.R;

public class NewCategoryDialogFragment extends DialogFragment {
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_CATEGORY_DESCRIPTION = "category_description";

    public EditText categoryName;
    public EditText categoryDescription;

    public static final String TAG = "NewCategoryDialogFragment";

    public static NewCategoryDialogFragment newInstance() {
        return new NewCategoryDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_category, null);
        categoryName = (EditText) view.findViewById(R.id.new_category_name);
        categoryDescription = (EditText) view.findViewById(R.id.new_category_description);

        return new AlertDialog.Builder(getContext()).setView(view)
                                                    .setTitle(R.string.new_category_title)
                                                    .setPositiveButton(R.string.new_category_create, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int i) {
                                                            Intent result = new Intent();
                                                            result.putExtra(KEY_CATEGORY_NAME, categoryName.getText().toString());
                                                            result.putExtra(KEY_CATEGORY_DESCRIPTION, categoryDescription.getText().toString());
                                                            getTargetFragment().onActivityResult(CategoryListFragment.REQ_CODE_NEW_CATEGORY, Activity.RESULT_OK, result);
                                                            dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.new_category_cancel, null)
                                                    .show();
    }
}
