package com.katya.katyacepfeneri.camera;

import android.hardware.Camera;

public class CameraOld implements CameraSupport {

    private Camera camera;

    @Override
    public boolean openCamera (int cameraId) {
        boolean result = false;
        //Ignore cameraId
        if (camera == null) {
            this.camera = Camera.open();
            result = true;

        }
        return result;
    }

    @Override
    public void releaseCamera (){
        if (camera != null) {
            camera.release();
        }
        camera = null;
    }

    @Override
    public boolean isFlashOn () {
        return Camera.Parameters.FLASH_MODE_TORCH.equals(camera.getParameters().getFlashMode());
    }

    @Override
    public void setFlashOn() {
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
    }

    @Override
    public void setFlashOff () {
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();

    }
}
