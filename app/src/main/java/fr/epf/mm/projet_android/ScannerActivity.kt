package fr.epf.mm.projet_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import fr.epf.mm.projet_android.model.Movie
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ScannerActivity : AppCompatActivity() {
    private lateinit var IDs : List<Genre>

    private lateinit var barcodeView: DecoratedBarcodeView

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // Traitement du résultat du code-barres scanné
            val barcodeValue = result.text
            Log.d("EPF", "barcodeResult: $barcodeValue")
            try {
                val movie = getMovie(barcodeValue)
                if (movie != null){
                    Log.d("EPF", "movie: ${movie}")
                    val intent = Intent(this@ScannerActivity, DetailMovieActivity::class.java)
                    intent.putExtra("movie", movie)
                    intent.putParcelableArrayListExtra("genres", ArrayList(IDs))
                    this@ScannerActivity.startActivity(intent)
                }
            } catch (e : Exception){
                Toast.makeText(this@ScannerActivity, "Identifiant du film non reconnu", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ScannerActivity, MainActivity::class.java)
                this@ScannerActivity.startActivity(intent)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        IDs = intent.getParcelableArrayListExtra<Genre>("genres")!!

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
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ScannerActivity, MainActivity::class.java)
                this@ScannerActivity.startActivity(intent)
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 123
    }

    private fun getMovie (code: String) : Movie? {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val movieApi = retrofit.create(MovieApi::class.java)
            val apiKey = "31e4672492df89a26175c865fed7271a"
            val language = "fr"
            val appendToResponse = "videos,credits,recommendations"
            var movie: Movie? = null

            runBlocking {
                val response = movieApi.getMovieDetails(code.toLong(), apiKey, language, appendToResponse)
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