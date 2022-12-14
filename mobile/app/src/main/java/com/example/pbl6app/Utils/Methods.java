package com.example.pbl6app.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.R;

public class Methods {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Methods.context = context;
    }

    public static void showDialog(
            int drawableResource,
            String titleDialog,
            String contentDialog,
            String textNoButton,
            String textYesButton,
            ListenerDialog listenerDialog
    ) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.imageDialog);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResource));

        TextView txtContentDialog = dialog.findViewById(R.id.txtContentDialog);
        txtContentDialog.setText(contentDialog);

        TextView txtTitleDialog = dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText(titleDialog);

        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);
        btnCancelDialog.setOnClickListener(view -> listenerDialog.onNoClick(dialog));
        btnCancelDialog.setText(textNoButton);
        if(textNoButton.equals("")){
            btnCancelDialog.setVisibility(View.GONE);
        } else {
            btnCancelDialog.setVisibility(View.VISIBLE);
        }

        Button btnChangeDialog = dialog.findViewById(R.id.btnChangeDialog);
        btnChangeDialog.setOnClickListener(view -> listenerDialog.onYesClick(dialog));
        btnChangeDialog.setText(textYesButton);
        if(textYesButton.equals("")){
            btnChangeDialog.setVisibility(View.GONE);
        } else {
            btnChangeDialog.setVisibility(View.VISIBLE);
        }

        dialog.show();
    }

    public static String toStringNumber(int price) {
        if (price == 0) {
            return "0";
        }
        StringBuilder s = new StringBuilder();
        int count = 0;
        int temp = Math.abs(price);
        while (temp != 0L) {
            s.append(temp % 10);
            count += 1;
            temp /= 10;
            if (count == 3 && temp != 0L) {
                s.append(",");
                count = 0;
            }
        }
        s.append((price < 0L) ? "-" : "");
        return s.reverse().toString();
    }
}
