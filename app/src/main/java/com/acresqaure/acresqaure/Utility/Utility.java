package com.acresqaure.acresqaure.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.acresqaure.acresqaure.Listeners.ErrorDialogActionListener;
import com.acresqaure.acresqaure.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by parasdhawan on 21/02/16.
 */
public class Utility {

    static ProgressDialog pd;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showKeyboard(Context context,EditText editText){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showToast(Context context,String text){
        Toast.makeText(context,text,
                Toast.LENGTH_SHORT).show();
    }

    public static boolean isApiResponseSuccess(String json){

        try {
            JSONObject jsonObject=new JSONObject(json);
            boolean successValue=jsonObject.optBoolean("Success");
            return successValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showRetryDialog(Context context, final ErrorDialogActionListener errorDialogActionListener){
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("We are unable to process your request.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialogActionListener.onRetry();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialogActionListener.onCancel();
                    }
                })
                .show();
    }

    public static void showMessageDialog(Context context, String msg){
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void showProgressDialog(Context context){
        pd = new ProgressDialog(context);
        pd.setMessage("loading");
        pd.show();
    }

    public static void dismissProgressDialog(){
        pd.dismiss();
    }

    public static void setEditTextWarning(final EditText editText, final Context context, final String msg){
        editText.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.warning, 0);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (editText.getCompoundDrawables()[DRAWABLE_RIGHT]!=null) {
                        if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            showToast(context, msg);

                            return true;
                        }
                    }
                }
                return false;
            }
        });


    }

    public static void removeEditTextWarning(EditText editText){
        editText.setCompoundDrawablesWithIntrinsicBounds(0,0, 0, 0);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
