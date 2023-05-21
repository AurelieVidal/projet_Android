package fr.epf.mm.projet_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Size;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class QRCodeScanner extends AppCompatActivity {
private EditText qrCodeTxt;
private PreviewView previewView;

private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        qrCodeTxt = findViewById(R.id.qrCodeTxt);
        previewView = findViewById(R.id.cameraPreview);

        //cheking for camera permisssions
        /*if(ContextCompat.checkSelfPermission(QRCodeScanner.this, Manifest.permission.
        }
        else {
            ActivityCompat.requestPermissions(QRCodeScanner.this,new String[]{Manifest.permission.CAMERA},101);
        }*/

    }
    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(QRCodeScanner.this);
        cameraProviderListenableFuture.addListener(new Runnable() {
         @Override
          public void run() {
             try {
                 ProcessCameraProvider cameraProvider=cameraProviderListenableFuture.get();
                 bindImageAnalysis(cameraProvider);
             } catch (ExecutionException e) {
                 throw new RuntimeException(e);
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }

         }
        },ContextCompat.getMainExecutor( QRCodeScanner.this));

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNls String[] permissions, @NonNls int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if(grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
            init();
        }
        else{
            Toast.makeText(QRCodeScanner.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(QRCodeScanner.this), new @ExperimentalGetImage @ExperimentalGetImage @ExperimentalGetImage @ExperimentalGetImage ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy image) {
                @SuppressLint("UnsafeOptInUsageError") Image mediaImage =image.getImage();
                if(mediaImage!=null){
                    InputImage image2 =InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                    BarcodeScanner scanner = BarcodeScanning.getClient();

                    Task<List<Barcode>> results = scanner.process(image2);

                    results.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            for(Barcode barcode : barcodes){
                                final String getValue = barcode.getRawValue();

                                qrCodeTxt.setText(getValue);
                            }
                            image.close();
                            mediaImage.close();
                        }
                    });

                }
            }
        });

        Preview preview =new Preview.Builder().build();
        CameraSelector cameraSelector =new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this,cameraSelector,imageAnalysis,preview);


    }
}