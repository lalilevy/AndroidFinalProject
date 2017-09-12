package com.shir.androidfinalproject.data;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputValidation  {
    private Context context;
    private static final String TAG = "InputValidation";

    /**
     * constructor
     *
     * @param context
     */
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


    /**
     * method to check InputEditText has valid email .
     *
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

    public Date getValidateDate(TextInputEditText editInputEditText,
                                        TextInputLayout textInputLayout,
                                        String message,
                                        String dateFormat) {

        Date dtValidate = null;
        String value = editInputEditText.getText().toString();

        if (!value.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);//, Locale.US);
                dtValidate = sdf.parse(value);
                
                textInputLayout.setErrorEnabled(false);
                        
            } catch (Exception pe){
                Log.d(TAG, "ERROR getValidateDate" + pe.getMessage());
            }
        }
        
        if (dtValidate == null){
            textInputLayout.setError(message);
            hideKeyboardFrom(editInputEditText);
        }
        
        return dtValidate;
    }

    public int getValidateInt(TextInputEditText editInputEditText,
                                TextInputLayout textInputLayout,
                                String message) {

        int nValidate = -1;
        String value = editInputEditText.getText().toString();

        if (!value.isEmpty()) {
            try {
                nValidate = Integer.parseInt(value);

                textInputLayout.setErrorEnabled(false);

            } catch (NumberFormatException pe){ }
        }

        if (nValidate == -1){
            textInputLayout.setError(message);
            hideKeyboardFrom(editInputEditText);
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
