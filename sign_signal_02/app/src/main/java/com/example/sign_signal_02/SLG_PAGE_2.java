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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.hardware.camera2.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.ByteArrayOutputStream;

public class SLG_PAGE_2 extends AppCompatActivity {

    private TextureView textureView;
    private Button startButton, stopButton;
    private TextView responseTextView;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private boolean isStreaming = false;
    private StreamTask streamTask;

    private static final String SERVER_IP = "10.248.249.102";
    private static final int SERVER_PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slg_page2);

        textureView        = findViewById(R.id.textureView);
        startButton        = findViewById(R.id.startButton);
        stopButton         = findViewById(R.id.stopButton);
        responseTextView   = findViewById(R.id.responseTextView);

        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                openCamera();
            }
            @Override public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {}
            @Override public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) { return false; }
            @Override public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {}
        });

        startButton.setOnClickListener(view -> {
            if (!isStreaming) {
                isStreaming = true;
                // Passing the task header string to the stream task
                String task = getIntent().getStringExtra("task");

                streamTask = new StreamTask(task);
                streamTask.execute();
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
            }
        });

        stopButton.setOnClickListener(view -> {
            isStreaming = false;
            if (streamTask != null) streamTask.cancel(true);
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
            responseTextView.setText("Server response will appear here...");
        });
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[1];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return;
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override public void onOpened(@NonNull CameraDevice camera) { cameraDevice = camera; startPreview(); }
                @Override public void onDisconnected(@NonNull CameraDevice camera) { camera.close(); cameraDevice = null; }
                @Override public void onError(@NonNull CameraDevice camera, int error) { camera.close(); cameraDevice = null; }
            }, null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(640, 480);
            Surface surface = new Surface(texture);
            cameraDevice.createCaptureSession(
                    java.util.Collections.singletonList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            captureSession = session;
                            try {
                                CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                captureRequest.addTarget(surface);
                                captureSession.setRepeatingRequest(captureRequest.build(), null, null);
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                        @Override public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
                    }, null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Updated StreamTask class
    private class StreamTask extends AsyncTask<Void, Void, Void> {

        private String currentTask;

        // Constructor to receive the task string
        public StreamTask(String currentTask) {
            this.currentTask = currentTask;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 OutputStream outputStream = socket.getOutputStream();
                 InputStream inputStream = socket.getInputStream()) {

                while (isStreaming) {
                    Bitmap bitmap = textureView.getBitmap();
                    if (bitmap == null) continue;

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                    // Attach the header and the pipe character before the base64 string
                    String messageToSend = currentTask + "|" + encodedImage + "\n";
                    outputStream.write(messageToSend.getBytes());
                    outputStream.flush();

                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead > 0) {
                        String response = new String(buffer, 0, bytesRead);

                        runOnUiThread(() -> responseTextView.setText("Server: " + response));
                    }

                    Thread.sleep(100);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}