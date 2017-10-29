package com.katya.katyacepfeneri;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.katya.katyacepfeneri.camera.CameraOld;
import com.katya.katyacepfeneri.camera.CameraSupport;

import java.util.Timer;
import java.util.TimerTask;

//Main window
public class MainActivity extends Activity implements SensorEventListener {

    //boolean hasFlash;
    private Button feneriAc;
    private Button feneriKapa;
    private Button isildakAc;
    private Button isildakKapa;

    private EditText edtTextPusula;

    //Timer for isildak
    private Timer timer;

    // device sensor manager
    private SensorManager sensorManager;

    private CameraSupport cameraSupport;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private  int pusulaDerece = 0;

    protected  void createCameraSupport () {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraSupport = new CameraNew(context);
        } else {
            cameraSupport = new CameraOld();
        }*/
        cameraSupport = new CameraOld();

    }

    protected void openCamera () {
        cameraSupport.openCamera(0);
    }
    protected boolean hasPermissions(Context context, String... permissions) {
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

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_main);


        if (!hasPermissions(this,Manifest.permission.CAMERA)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {
                Toast.makeText (this,"Camera permission is required",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        createCameraSupport ();

        // initialize your android device sensor capabilities
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        feneriAc =  (Button)findViewById(R.id.buttonFenerAc);
        feneriKapa = (Button)findViewById(R.id.buttonFenerKapa);
        isildakAc = (Button)findViewById(R.id.buttonIsildakAc);
        isildakKapa = (Button)findViewById(R.id.buttonIsildakKapa);

        // EditText that will tell the user what degree is he heading
        edtTextPusula = (EditText) findViewById(R.id.edt_text_pusula);

        /*hasFlash = getApplicationContext().getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);*/

        feneriAc.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                openCamera ();

                if (cameraSupport.isFlashOn() == false) {
                    cameraSupport.setFlashOn();
                }
            }
        });

        feneriKapa.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick (View v) {

                if (cameraSupport.isFlashOn()) {
                    cameraSupport.setFlashOff();
                }
                cameraSupport.releaseCamera();
            }
        });

        isildakAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (timer == null){

                    openCamera ();

                    timer = new Timer();

                    //1 second timer
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (cameraSupport.isFlashOn()) {
                                cameraSupport.setFlashOff();
                            } else {
                                cameraSupport.setFlashOn();
                            }
                        }
                    },0,1000);

                }

            }
        });
        isildakKapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null){
                    timer.cancel();
                    timer = null;

                    if (cameraSupport.isFlashOn()) {
                        cameraSupport.setFlashOff();
                    }

                    //Release the camera
                    cameraSupport.releaseCamera();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        int degree = (int)event.values[0];
        pusulaDerece = (int)((pusulaDerece * 0.90) + (degree * 0.10));
        String str = String.format("%d",pusulaDerece);
        edtTextPusula.setText(str);
    }
    protected void onDestroy () {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        //This is called when back button is pressed
        cameraSupport.releaseCamera();

        // to stop the listener and save battery
        sensorManager.unregisterListener(this);
    }


}
