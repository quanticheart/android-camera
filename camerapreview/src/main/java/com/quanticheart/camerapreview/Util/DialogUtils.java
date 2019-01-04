/*
 * Copyright (c) Modifications and updates started on 2018/$today.mount/26, by programmer Jonn Alves, about the Naville Brasil contract.
 */

package com.quanticheart.camerapreview.Util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.example.parsaniahardik.custom_camera.R;
import com.quanticheart.camerapreview.CameraPreview;

import java.util.Objects;

public class DialogUtils {

    //==============================================================================================
    //
    // ** init Vars
    //
    //==============================================================================================
    private static Dialog dialog;


    //==============================================================================================
    //
    // ** Utils
    //
    //==============================================================================================
    public static View getView(Activity activity, int layout) {
        return activity.getLayoutInflater().inflate(layout, null);
    }

    //==============================================================================================
    //
    // ** Open Dialog and utis
    //
    //==============================================================================================
    public static Dialog openDialogFullScreen(Activity activity, View layout) {
        dialog = new Dialog(activity, R.style.DialogFullscreen);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogNoAnimation;
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.show();
        return dialog;
    }

    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = null;
    }

    //==============================================================================================
    //
    // ** Create View Dialog
    //
    //==============================================================================================

    public static void openDialog(final Activity activity, final Bitmap bitmap) {

        View view = DialogUtils.getView(activity, R.layout.dialog_ok);

        String path = ImageUtils.saveImage(activity, bitmap);
        Bitmap newBitmap = ImageUtils.rotateBitmap(activity, Uri.parse(path), bitmap);

        ((ImageView) view.findViewById(R.id.imgCapture)).setImageBitmap(newBitmap);

        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtils.closeDialog();
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.closeDialog();
            }
        });

        DialogUtils.openDialogFullScreen(activity, view);


    }
}
