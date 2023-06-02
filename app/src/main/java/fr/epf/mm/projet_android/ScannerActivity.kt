package fr.epf.mm.projet_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ScannerActivity : AppCompatActivity() {
    private lateinit var IDs: List<Genre>
    private lateinit var barcodeView: DecoratedBarcodeView
    private var utilisateur: Utilisateur? = null

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            val barcodeValue = result.text
            try {
                val movie = getMovie(barcodeValue)
                if (movie != null) {
                    val intent = Intent(this@ScannerActivity, DetailMovieActivity::class.java)
                    intent.putExtra("movie", movie)
                    intent.putExtra("utilisateur", utilisateur)
                    intent.putParcelableArrayListExtra("genres", ArrayList(IDs))
                    this@ScannerActivity.startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ScannerActivity,
                    getString(R.string.Toast_scanner_error),
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@ScannerActivity, MainActivity::class.java)
                this@ScannerActivity.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        supportActionBar?.setTitle("QR Code")

        IDs = intent.getParcelableArrayListExtra<Genre>("genres")!!
        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        barcodeView = findViewById(R.id.barcodeScannerView)
        barcodeView.decodeContinuous(barcodeCallback)
        barcodeView.setStatusText(getString(R.string.scan))
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
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
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(
                    this, getString(R.string.toast_scanner_permission), Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@ScannerActivity, MainActivity::class.java)
                this@ScannerActivity.startActivity(intent)
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 123
    }

    private fun getMovie(code: String): Movie? {
        val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create()).build()
        val movieApi = retrofit.create(MovieApi::class.java)
        val apiKey = "31e4672492df89a26175c865fed7271a"
        val language = "fr"
        val appendToResponse = "videos,credits,recommendations"
        var movie: Movie? = null
        runBlocking {
            val response =
                movieApi.getMovieDetails(code.toLong(), apiKey, language, appendToResponse)
            val movied = response.body()
            val genre_ids: Array<Long>? = movied?.genres?.map { it.id }?.toTypedArray()
            if (movied != null) {
                movie = Movie(
                    code.toLong(),
                    movied.title,
                    movied.release_date,
                    movied.vote_average,
                    genre_ids!!,
                    movied.poster_path,
                    movied.backdrop_path,
                    movied.popularity,
                    movied.original_language!!
                )
            }
        }
        return movie
    }
}