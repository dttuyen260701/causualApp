package com.example.pbl6app.Listeners;

import android.app.Dialog;

public interface ListenerDialog {
    void onDismiss();
    void onNoClick(Dialog dialog);
    void onYesClick(Dialog dialog);
}
