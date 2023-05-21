package fr.epf.mm.projet_android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ScannerActivity : AppCompatActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // Traitement du résultat du code-barres scanné
            val barcodeValue = result.text
            Log.d("EPF", "barcodeResult: $barcodeValue")
            // Faire quelque chose avec la valeur du code-barres
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        barcodeView = findViewById(R.id.barcodeScannerView)
        barcodeView.decodeContinuous(barcodeCallback)
        barcodeView.setStatusText("Scannez un QR Code")
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        } else {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    private fun startCamera() {
        barcodeView.resume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                // Gestion de l'autorisation de la caméra refusée -> retourner à l'accueil
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 123
    }
}