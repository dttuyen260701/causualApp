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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        if (dialog == null || !dialog.isShowing()) {
            dialog = new Dialog(context);
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
        }


        dialog.setOnDismissListener(dialog -> {
            listenerDialog.onDismiss();
        });


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

    public static void sendNotification(String title, String content, int drawable, int responseCode, boolean forNewChannel) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.CHANNEL_ID);
        Intent intent = new Intent(context,
                (Constant.USER.getId().isEmpty())
                        ? LoginActivity.class
                        : (Constant.USER.getRole() == Constant.ROLE_WORKER) ? MainActivity.class : MainActivityUser.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("KEY_SETTING_FRAG", responseCode);

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.ic_app);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        builder.setLargeIcon(bitmap);
        if(responseCode != -1) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setContentText(content);

        NotificationManagerCompat.from(context).notify((forNewChannel) ?
                Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(4)) :
                123, builder.build());
    }

    public static String getPastTimeString(String dateString) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm");
            Date postDate = sdf.parse(dateString);
            Date currentDate = new Date();
            long diffInTime = currentDate.getTime() - postDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
            long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
            long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime) / 365l;
            long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime) / 30l;
            long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);

            if (diffInYear < 1) {
                if (diffInMonth < 1) {
                    if (diffInDay < 1) {
                        if (diffInHour < 1) {
                            if (diffInMinutes < 1) {
                                return "vài giây";
                            } else {
                                return diffInMinutes + " phút";
                            }
                        } else {
                            return diffInHour + " giờ";
                        }
                    } else {
                        return diffInDay + " ngày";
                    }
                } else {
                    return diffInMonth + " tháng";
                }
            } else {
                return diffInYear + " năm";
            }
        }catch (Exception e){
            return "";
        }
    }

}
