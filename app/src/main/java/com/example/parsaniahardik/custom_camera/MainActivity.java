package com.example.parsaniahardik.custom_camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.quanticheart.camerapreview.Util.CameraView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraView.start(MainActivity.this);
            }
        });
    }
}
