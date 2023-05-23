package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class InscriptionActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)

        /*val movie = intent.extras?.get("movie") as? Movie
        val genres = intent.getParcelableArrayListExtra<Genre>("genres")*/
        val pseudo = findViewById<TextView>(R.id.pseudo_creation_compte)
        val motDePasse= findViewById<TextView>(R.id.mot_passe_creation)
        val prenom =findViewById<TextView>(R.id.prenom_creation_compte)
        val nom = findViewById<TextView>(R.id.nom_creation_compte)

        Log.d("EPF", "onCreate: ${pseudo}${motDePasse}${nom}${prenom}")

        /*lateinit var detail_movie: MovieD
        lifecycleScope.launch {
            try {
                detail_movie = movie?.id?.let { getMovieDetails(it) }!!
                Log.d("EPF", detail_movie.overview.toString())
            } catch (e: Exception) {
                Log.e("EPF", "Error getting movie details", e)
            }
        }*/


    }

    private fun saveUtilisateur(utilisateurs: List<Utilisateur>) {
        val gson = Gson()

        val sharedPreferences = getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putString("utilisateurs: ", gson.toJson(utilisateurs))
        editor?.apply()
    }

    private fun GetUtilisateurMemory(): List<Utilisateur> {
        val utilisateurs: MutableList<Utilisateur> = mutableListOf()
        val sharedPreferences = getSharedPreferences("Utilisateurs", Context.MODE_PRIVATE)
        val utilisateursJson = sharedPreferences.getString("Utilisateurs", null)
        val gson = Gson()
        if (!utilisateursJson.isNullOrEmpty()) {
            val utilisateur = gson.fromJson(utilisateursJson, Array<Utilisateur>::class.java).toMutableList()
            Log.d("EPF", "utilisateurs: $utilisateurs")
            utilisateurs.addAll(utilisateurs)
        }
        return utilisateurs.toList()
    }

    /*private suspend fun getUtilisateurDetails(utilisateurId: Long): MovieD {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieApi = retrofit.create(MovieApi::class.java)
        lateinit var moviedd: MovieD
        val apiKey = "31e4672492df89a26175c865fed7271a"
        val language = "fr"
        val appendToResponse = "videos,credits,recommendations"

        runBlocking {
            val response = movieApi.getMovieDetails(movieId, apiKey, language, appendToResponse)
            val movied = response.body()
            val genres = movied?.genres?.joinToString(separator = ", ") { it.name }
            val test = movied?.genres?.joinToString(separator = ", ")
            val budget = movied?.budget
            val revenue = movied?.revenue
            val languageVo = movied?.original_language
            val trailerUrl = movied?.videos?.results?.firstOrNull()?.let { "https://www.youtube.com/watch?v=${it.key}" }
            val recommendations = movied?.recommendations?.results
            val overview = movied?.overview
            val runtime = movied?.runtime

            val cast = movied?.credits?.cast?.joinToString(separator = ", "){it.name}

            val actors: List<Actor>
            actors = movied?.credits?.cast?.filter {
                it.order <= 10 && it.known_for_department == "Acting"
            }?.map {
                Actor (
                    it.name,
                    it.profile_path,
                    it.character,
                    it.known_for_department,
                    it.order
                )
            }!!


            moviedd = MovieD(
                movied?.genres,
                overview,
                runtime,
                budget,
                revenue,
                languageVo,
                movied?.videos,
                movied?.recommendations,
                creditsResponse(actors),
                movied.title,
                movied.vote_average,
                movied.poster_path,
                movied.backdrop_path,
                movied.release_date,
                movied.popularity
            )

            /*
            val title: String,
    val vote_average: Float,
    val poster_path: String,
    val backdrop_path: String
             */

        }

        return moviedd
    }*/



    /*fun formatAmount(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.currency = Currency.getInstance("USD")
        return formatter.format(amount)
    }*/


}