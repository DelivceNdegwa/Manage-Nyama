package com.delivce.managenyama.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.delivce.managenyama.R;

public class MyDialogs {
    Context context;

    public MyDialogs(Context context) {
        this.context = context;
    }

    // Dialogs
    public void createSuccessDialog(String successMsg) {
        Dialog successDialog = new Dialog(context);

        successDialog.setContentView(R.layout.success_dialog_layout);

        if(Build.VERSION.PREVIEW_SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            successDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_background));
        }
        successDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        successDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        TextView tvSuccessMsg = successDialog.findViewById(R.id.tv_success_message);
        tvSuccessMsg.setText(successMsg);

        successDialog.show();

        Button btnOkay = successDialog.findViewById(R.id.btn_dialog_okay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successDialog.dismiss();
            }
        });
    }

    public void createFailureDialog(String failureMsg) {
        Dialog failureDialog = new Dialog(context);

        failureDialog.setContentView(R.layout.failure_dialog_layout);

        if(Build.VERSION.PREVIEW_SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            failureDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_background));
        }

        failureDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        failureDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;


    }


}
