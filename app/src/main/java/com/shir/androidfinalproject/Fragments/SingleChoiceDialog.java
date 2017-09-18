package com.colman.androidfinalproject.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.colman.androidfinalproject.CallBacks.GetSelectedItemCallback;

import java.util.List;

@SuppressLint("ValidFragment")
public class SingleChoiceDialog extends DialogFragment {
    public static final String EVENT_ID = "EVENT_ID";
    public static final String ITEMS = "ITEMS";
    public static final String SELECTED = "SELECTED";
    public static final String TITLE = "TITLE";

    private CharSequence[] csItems;
    private GetSelectedItemCallback mCallback;
    String selectedItem;

    @SuppressLint("ValidFragment")
    public SingleChoiceDialog(GetSelectedItemCallback callback){
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        selectedItem = "";
        Bundle bundle = getArguments();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setPositiveButton("Save", (dialog12, which) ->{
            mCallback.onApproved(selectedItem);
            dialog12.dismiss();
        });

        dialog.setNegativeButton("Cancel", (dialog12, which) -> {
            mCallback.onCanceled();
            dialog12.dismiss();
        });

        dialog.setTitle(bundle.getString(TITLE));
        List<String> list = (List<String>)bundle.get(ITEMS);
        int position = bundle.getInt(SELECTED);
        csItems = list.toArray(new CharSequence[list.size()]);
        dialog.setSingleChoiceItems(csItems, position,
                (dialog1, which) -> selectedItem = csItems[which].toString());

        return dialog.create();
    }
}