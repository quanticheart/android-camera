package com.quanticheart.camerapreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.parsaniahardik.custom_camera.R;
import com.quanticheart.camerapreview.CustomView.CamPreview;
import com.quanticheart.camerapreview.Util.DialogUtils;
import com.quanticheart.camerapreview.Util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPreview extends AppCompatActivity {

    //==============================================================================================
    //
    // ** Init Vars
    //
    //==============================================================================================

    private Context myContext;

    //Camera
    private Camera mCamera;
    private CamPreview mPreview;
    private Camera.PictureCallback mPicture;

    //Vars for camera
    private boolean cameraFront = false;
    public static Bitmap bitmap;

    //Vars for view
    private Button capture, switchCamera;
    private LinearLayout cameraPreview;


    //==============================================================================================
    //
    // ** Create View
    //
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initVars();
        initActions();
        initCamera();

    }

    //==============================================================================================
    //
    // ** Init Create Functions
    //
    //==============================================================================================

    private void initVars() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        cameraPreview = findViewById(R.id.cPreview);
        capture = findViewById(R.id.btnCam);
        switchCamera = findViewById(R.id.btnSwitch);

    }


    private void initActions() {

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, getPictureCallback());
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number of cameras
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    //release the old camera instance
                    //switch camera, from the front and the back and vice versa

                    releaseCamera();
                    chooseCamera();
                } else {

                }
            }
        });

    }

    private void initCamera() {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        mPreview = new CamPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);
        mCamera.startPreview();
    }

    //==============================================================================================
    //
    // ** Camera Functions
    //
    //==============================================================================================

    private int findFrontFacingCamera() {

        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;

    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;

            }

        }
        return cameraId;
    }


    public void chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(90);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }


    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    //==============================================================================================
    //
    // ** Interface
    //
    //==============================================================================================

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                ImageUtils.createBitmap(CameraPreview.this, data);
//                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
////                Intent intent = new Intent(CameraPreview.this, PictureActivity.class);
////                startActivity(intent);

            }
        };
        return picture;
    }



    //==============================================================================================
    //
    // ** Life Activity @Override
    //
    //==============================================================================================

    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        } else {
            Log.d("nu", "no null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        releaseCamera();
    }


}
