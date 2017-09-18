package com.colman.androidfinalproject.Data;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InputValidation  {
    private Context context;
    private static final String TAG = "InputValidation";

    public InputValidation(Context context) {
        this.context = context;
    }

    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText,
                                         TextInputLayout textInputLayout,
                                         String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    public boolean isEditTextFilled(EditText editText, String message) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            editText.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            editText.setError(null);
        }

        return true;
    }

    /**
     * method to check InputEditText has valid email .
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText,
                                         TextInputLayout textInputLayout,
                                         String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public Date getValidateDate(EditText editDate, String message, String dateFormat) {

        Date dtValidate = null;
        String value = editDate.getText().toString();

        if (!value.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                dtValidate = sdf.parse(value);
                editDate.setError(null);
                        
            } catch (Exception pe){
                Log.d(TAG, "ERROR getValidateDate" + pe.getMessage());
            }
        }
        
        if (dtValidate == null){
            editDate.setError(message);
            hideKeyboardFrom(editDate);
        }
        
        return dtValidate;
    }

    public int getValidateInt(EditText editInt, String message) {

        int nValidate = -1;
        String value = editInt.getText().toString();

        if (!value.isEmpty()) {
            try {
                nValidate = Integer.parseInt(value);
                editInt.setError(null);

            } catch (NumberFormatException pe){
                Log.d(TAG, "ERROR getValidateDate" + pe.getMessage());
            }
        }

        if (nValidate == -1){
            editInt.setError(message);
            hideKeyboardFrom(editInt);
        }

        return nValidate;
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1,
                                          TextInputEditText textInputEditText2,
                                          TextInputLayout textInputLayout,
                                          String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(),
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
