package com.example.pbl6app.Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.R;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.activities.MainActivity;
import com.example.pbl6app.activities.MainActivityUser;

import java.util.Calendar;

public class Methods {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static NotificationManager notificationManager;
    private static NotificationChannel channel;
    private static Dialog dialog;

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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        dialog.setOnDismissListener(dialog -> {
            listenerDialog.onDismiss();
        });

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
        if (textNoButton.equals("")) {
            btnCancelDialog.setVisibility(View.GONE);
        } else {
            btnCancelDialog.setVisibility(View.VISIBLE);
        }

        Button btnChangeDialog = dialog.findViewById(R.id.btnChangeDialog);
        btnChangeDialog.setOnClickListener(view -> listenerDialog.onYesClick(dialog));
        btnChangeDialog.setText(textYesButton);
        if (textYesButton.equals("")) {
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

    public static void makeToast(String message) {
        Toast.makeText(Methods.context, message, Toast.LENGTH_SHORT).show();
    }

    public static void sendNotification(String title, String content, int drawable) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.CHANNEL_ID);
        Intent intent = new Intent(context,
                (Constant.USER.getId().isEmpty())
                        ? LoginActivity.class
                        : (Constant.USER.getRole() == Constant.ROLE_WORKER) ? MainActivity.class : MainActivityUser.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("KEY_SETTING_FRAG", 1);

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(context, 3,
                intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setSmallIcon(R.drawable.ic_app);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        builder.setLargeIcon(bitmap);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setContentText(content);

        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (channel == null) {
            channel = new NotificationChannel(
                    "123123",
                    "CHANNELLL",
                    NotificationManager.IMPORTANCE_HIGH
            );
        }

        notificationManager.createNotificationChannel(channel);
        NotificationManagerCompat.from(context).notify(Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(4)), builder.build());
    }

}
