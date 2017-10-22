package com.katya.katyacepfeneri;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Main window
public class MainActivity extends Activity {

    boolean hasFlash;
    Button feneriAc;
    Button feneriKapa;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
