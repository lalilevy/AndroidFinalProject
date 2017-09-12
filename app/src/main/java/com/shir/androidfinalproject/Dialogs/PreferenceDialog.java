package com.shir.androidfinalproject.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PreferenceDialog extends DialogFragment {

    public interface PreferenceDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


    public String selectedValue = "";

    private PreferenceDialogListener mListener;
    List<String> currList = new ArrayList<String>();
    int index = -1;

    @Override
    @NonNull
    public Dialog onCreateDialog (Bundle savedInstancsState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Choose your preference");
        builder.setSingleChoiceItems((String [])currList.toArray(), index, null);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedValue = currList.get(which);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PreferenceDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public void initVars(String selected , List<String> lst){
        this.selectedValue = selected;
        this.currList = lst;

        index = this.currList.indexOf(selected);
    }


}
