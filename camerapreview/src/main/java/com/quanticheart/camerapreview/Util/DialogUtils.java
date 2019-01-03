/*
 * Copyright (c) Modifications and updates started on 2018/$today.mount/26, by programmer Jonn Alves, about the Naville Brasil contract.
 */

package com.quanticheart.camerapreview.Util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.example.parsaniahardik.custom_camera.R;

public class DialogUtils {

    private static Dialog dialog;

    public static View getView(Activity activity, int layout) {
        return activity.getLayoutInflater().inflate(layout, null);
    }

    public static Dialog openDialogFullScreen(Activity activity, View layout) {
        dialog = new Dialog(activity, R.style.DialogFullscreen);
        dialog.setContentView(layout);
        dialog.show();
        return dialog;
    }

}
