package com.example.sign_signal_02;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.hardware.camera2.*;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.ByteArrayOutputStream;


public class SLG_PAGE extends AppCompatActivity {
    private TextureView textureView;
    private Button startButton, stopButton;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private boolean isStreaming = false;
    private StreamTask streamTask;

    private static final String SERVER_IP = "192.168.45.102"; // Change to your server's IP
    private static final int SERVER_PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slg_page2);

        textureView = findViewById(R.id.textureView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        // Request Camera Permission
        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

        // Open camera when TextureView is ready
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {}

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) { return false; }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {}
        });

        // Start Streaming Button Click
        startButton.setOnClickListener(view -> {
            if (!isStreaming) {
                isStreaming = true;
                streamTask = new StreamTask();
                streamTask.execute();
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        // Stop Streaming Button Click
        stopButton.setOnClickListener(view -> {
            isStreaming = false;
            if (streamTask != null) {
                streamTask.cancel(true);
            }
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
        });
    }

    // Open Camera
    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    startPreview();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    camera.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    camera.close();
                    cameraDevice = null;
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start Camera Preview
    private void startPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(640, 480);
            Surface surface = new Surface(texture);
            CameraCaptureSession.StateCallback captureCallback = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequest.addTarget(surface);
                        captureSession.setRepeatingRequest(captureRequest.build(), null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
            };
            cameraDevice.createCaptureSession(java.util.Collections.singletonList(surface), captureCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // AsyncTask for Streaming Video Frames
    private class StreamTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 OutputStream outputStream = socket.getOutputStream();
                 InputStream inputStream = socket.getInputStream()
            ) // Fixed reader declaration
            {

                while (isStreaming) {
                    Bitmap bitmap = textureView.getBitmap();
                    if (bitmap == null) continue;

                    // Convert Bitmap to ByteArray
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Compress to reduce size
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    // Convert ByteArray to Base64
                    String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                    // Send Base64-encoded image with newline delimiter
                    outputStream.write((encodedImage + "\n").getBytes());
                    outputStream.flush();

                    // Receive response from server
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead > 0) {
                        String response = new String(buffer, 0, bytesRead);
                        Log.d("ServerResponse", "Received: " + response);
                    }
                    Thread.sleep(100); // Adjust streaming speed
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}