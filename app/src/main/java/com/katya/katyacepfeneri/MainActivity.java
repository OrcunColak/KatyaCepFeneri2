package com.katya.katyacepfeneri;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

//Main window
public class MainActivity extends Activity {

    boolean hasFlash;
    Button feneriAc;
    Button feneriKapa;
    Camera camera;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            } else
            {
                Toast.makeText(this, "The app was not allowed to access camera", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (!hasPermissions(this,Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        //This is called only once
        camera = Camera.open();

        feneriAc =  (Button)findViewById(R.id.buttonAc);
        feneriKapa = (Button)findViewById(R.id.buttonKapa);

        hasFlash = getApplicationContext().getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        feneriAc.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                if (isLightOn () == false) {
                   setLightOn();
                }
            }
        });

        feneriKapa.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                if (isLightOn ()) {
                    setLightOff();
                }
            }
        });
    }

    protected void onDestroy () {
        //This is called when back button is pressed
        camera.release();
    }

    private void setLightOff () {
        Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();

    }

    private void setLightOn () {
        Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();

    }

    private boolean isLightOn () {
        return Parameters.FLASH_MODE_TORCH.equals(camera.getParameters().getFlashMode());

    }
}
