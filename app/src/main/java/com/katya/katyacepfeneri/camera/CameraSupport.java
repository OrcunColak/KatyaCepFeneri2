package com.katya.katyacepfeneri.camera;

public interface CameraSupport {

    boolean openCamera (int cameraId);

    void releaseCamera ();

    boolean isFlashOn ();

    void setFlashOn();

    void setFlashOff ();
}
