package fr.epf.mm.projet_android

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import fr.epf.mm.projet_android.model.Movie
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val movie = intent.extras?.get("movie") as? Movie
        val genres = intent.getParcelableArrayListExtra<Genre>("genres")

        Log.d("EPF", "onCreate: ${movie}")

        lateinit var detail_movie: MovieD
        lifecycleScope.launch {
            try {
                detail_movie = movie?.id?.let { getMovieDetails(it) }!!
                Log.d("EPF", detail_movie.overview.toString())
            } catch (e: Exception) {
                Log.e("EPF", "Error getting movie details", e)
            }
        }


        val ImageViewAffiche = findViewById<ImageView>(R.id.detail_background)
        loadImage(detail_movie.backdrop_path, ImageViewAffiche)

        //poster
        val ImageViewPoster = findViewById<ImageView>(R.id.detail_poster)
        loadImage(detail_movie.poster_path, ImageViewPoster)

        //score
        val textViewVote = findViewById<TextView>(R.id.detail_vote)
        textViewVote.text = detail_movie.vote_average.toString()
        if (detail_movie.vote_average < 4) {
            textViewVote.setTextColor(Color.rgb(204,0,0))
        }else if (detail_movie.vote_average<7){
            textViewVote.setTextColor(Color.rgb(255,192,0))
        }
        else {
            textViewVote.setTextColor(Color.rgb(146,208,80))
        }

        //favori
        val imageButtonFav = findViewById<ImageButton>(R.id.detail_button_favorite)

        var isFavorite = false

        imageButtonFav.setOnClickListener {
            val colorStateList = imageButtonFav.imageTintList // récupère la liste des couleurs
            val defaultColor = colorStateList?.defaultColor // récupère la couleur par défaut (peut être null si aucune couleur par défaut n'est définie)
            val hexColor = defaultColor?.let { it1 -> Integer.toHexString(it1) }

            // Vérifie l'état actuel du bouton et change la couleur en conséquence
            if (isFavorite) {
                val colorStateList = ColorStateList.valueOf(Color.rgb(46,46,46))
                imageButtonFav.setImageTintList(colorStateList)
                movie?.favori = false

                Toast.makeText(this, "Film retiré des favoris", Toast.LENGTH_SHORT).show()
                Log.d("EPF", "pas favori")
            } else {
                val colorStateList = ColorStateList.valueOf(Color.rgb(204,0,0))
                imageButtonFav.setImageTintList(colorStateList)
                Toast.makeText(this, "Film ajouté aux favoris", Toast.LENGTH_SHORT).show()
                movie?.favori = true
            }

            isFavorite = !isFavorite
        }

        //title
        val textViewTitle = findViewById<TextView>(R.id.detail_title)
        textViewTitle.text = detail_movie.title

        //date
        val textViewDate = findViewById<TextView>(R.id.detail_date)
        if (detail_movie.release_date.length >4) {
            textViewDate.text = formatDate(detail_movie.release_date)
        } else {
            textViewDate.text = "Date inconnue"
        }

        //duree
        val textViewTime = findViewById<TextView>(R.id.detail_time)
        textViewTime.text = formatTime(detail_movie.runtime)

        //résumé
        val textViewResume = findViewById<TextView>(R.id.detail_summary)
        textViewResume.text = detail_movie.overview

        //budget
        val textViewBudget = findViewById<TextView>(R.id.detail_budget)
        textViewBudget.text = detail_movie.budget?.let { formatAmount( it) }

        //recettes
        val textViewRevenue = findViewById<TextView>(R.id.detail_revenue)
        textViewRevenue.text = detail_movie.revenue?.let { formatAmount(it) }

        //langue de la VO
        val textViewVO = findViewById<TextView>(R.id.detail_vo)
        val languageCode = detail_movie.original_language
        val languageName = languageCode?.let { TmdbLanguage.fromCode(it)?.fullName }
        textViewVO.text = languageName

        //genres
        val textViewGenres = findViewById<TextView>(R.id.detail_genres)
        val strGenres = detail_movie.genres?.joinToString(separator = ", ") { it.name }
        textViewGenres.text = strGenres

        //acteurs
        val actorRecyclerView = findViewById<RecyclerView>(R.id.detail_actors_recyclerview)
        val ActorLayoutManager = LinearLayoutManager(this)
        ActorLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        actorRecyclerView.layoutManager = ActorLayoutManager
        actorRecyclerView.adapter = ActorAdapter(this, detail_movie.credits.cast )

        //Bande Annonce
        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.detail_youtube_player_view)
/*
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = detail_movie.videos?.results?.firstOrNull().let { "${it?.key}" }
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })*/
        //var isVideoCued = false // Variable pour suivre si la vidéo est cued (prête à être lue)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = detail_movie.videos?.results?.firstOrNull()?.key

                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0f)
                    //isVideoCued = true
                }
            }
        })

        //Recommandations
        val recRecyclerView = findViewById<RecyclerView>(R.id.detail_movies_recyclerview)

        val recLayoutManager = LinearLayoutManager(this)
        recLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recRecyclerView.layoutManager = recLayoutManager
        recRecyclerView.adapter =
            detail_movie.recommendations?.results?.let { genres?.let { it1 ->
                MovieAdapter(this, it,
                    it1
                )
            } }











    }

    private suspend fun getMovieDetails(movieId: Long): MovieD {
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
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun formatAmount(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.currency = Currency.getInstance("USD")
        return formatter.format(amount)
    }

    fun formatTime(minutes: Long?): String {
        val hours = minutes?.div(60)
        val remainingMinutes = minutes?.rem(60)
        return "$hours h ${"%02d".format(remainingMinutes)} min"
    }
}