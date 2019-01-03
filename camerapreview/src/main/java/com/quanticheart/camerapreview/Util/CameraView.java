package com.quanticheart.camerapreview.Util;

import android.app.Activity;
import android.content.Intent;

import com.quanticheart.camerapreview.CameraPreview;

public class CameraView {

    public static void start(Activity activity) {
        Intent camera = new Intent(activity, CameraPreview.class);
        activity.startActivity(camera);
    }

}
