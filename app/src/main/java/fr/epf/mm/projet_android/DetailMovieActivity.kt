package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : AppCompatActivity() {
    private var utilisateur: Utilisateur? = null
    private lateinit var movie: Movie
    private var share1 = "Bonjour \uD83D\uDC4B, connais-tu le film "
    private var share2 = "Voici un petit résumé : "
    private var share3 = "Appuie sur le lien suivant pour voir la bande annonce ! "

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        supportActionBar?.setTitle("Détails du film")

        movie = intent.extras?.get("movie") as Movie
        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur
        val genres = intent.getParcelableArrayListExtra<Genre>("genres")

        lateinit var detail_movie: MovieD
        lifecycleScope.launch {
            try {
                detail_movie = movie.id.let { getMovieDetails(it) }
            } catch (e: Exception) {
                Log.e("EPF", "Error getting movie details", e)
            }
        }

        val ImageViewAffiche = findViewById<ImageView>(R.id.detail_background)
        loadImage(detail_movie.backdrop_path, ImageViewAffiche)

        val ImageViewPoster = findViewById<ImageView>(R.id.detail_poster)
        loadImage(detail_movie.poster_path, ImageViewPoster)

        val textViewVote = findViewById<TextView>(R.id.detail_vote)
        textViewVote.text = detail_movie.vote_average.toString()
        if (detail_movie.vote_average < 4) {
            textViewVote.setTextColor(Color.rgb(204, 0, 0))
        } else if (detail_movie.vote_average < 7) {
            textViewVote.setTextColor(Color.rgb(255, 192, 0))
        } else {
            textViewVote.setTextColor(Color.rgb(146, 208, 80))
        }


        val imageButtonFav = findViewById<ImageButton>(R.id.detail_button_favorite)
        var favoris = getUserFav(utilisateur!!, this)
        for (fav in favoris) {
            if (fav.id == movie.id) {
                movie.favori = true
                val colorStateList = ColorStateList.valueOf(Color.rgb(204, 0, 0))
                imageButtonFav.imageTintList = colorStateList
            }
        }
        imageButtonFav.setOnClickListener {
            if (movie.favori == true) {
                val colorStateList = ColorStateList.valueOf(Color.rgb(0, 0, 0))
                imageButtonFav.setImageTintList(colorStateList)
                Toast.makeText(this, getString(R.string.toast_remove_favoris), Toast.LENGTH_SHORT)
                    .show()

                val clone: MutableList<Movie> = mutableListOf()
                for (fav in favoris) {
                    if (fav.id != movie.id) {
                        clone.add(fav)
                    }
                }
                favoris = clone.toList()
            } else {
                val colorStateList = ColorStateList.valueOf(Color.rgb(204, 0, 0))
                imageButtonFav.setImageTintList(colorStateList)
                Toast.makeText(this, getString(R.string.toast_add_favoris), Toast.LENGTH_SHORT)
                    .show()

                val clone: MutableList<Movie> = mutableListOf()
                favoris.let { it1 -> clone.addAll(it1) }
                clone.add(movie)
                favoris = clone.toList()
            }
            utilisateur?.favoris = favoris
            saveFavorites()
            movie.favori = !movie.favori
        }

        val textViewTitle = findViewById<TextView>(R.id.detail_title)
        textViewTitle.text = detail_movie.title
        share1 += detail_movie.title + " ?"

        val textViewDate = findViewById<TextView>(R.id.detail_date)
        if (detail_movie.release_date.length > 4) {
            textViewDate.text = formatDate(detail_movie.release_date)
        } else {
            textViewDate.text = getString(R.string.Unknowned_date)
        }

        val textViewTime = findViewById<TextView>(R.id.detail_time)
        textViewTime.text = formatTime(detail_movie.runtime)

        val textViewResume = findViewById<TextView>(R.id.detail_summary)
        textViewResume.text = detail_movie.overview
        share2 += detail_movie.overview

        val textViewBudget = findViewById<TextView>(R.id.detail_budget)
        textViewBudget.text = detail_movie.budget?.let { formatAmount(it) }

        val textViewRevenue = findViewById<TextView>(R.id.detail_revenue)
        textViewRevenue.text = detail_movie.revenue?.let { formatAmount(it) }

        val textViewVO = findViewById<TextView>(R.id.detail_vo)
        val languageCode = detail_movie.original_language
        val languageName = languageCode?.let { TmdbLanguage.fromCode(it)?.fullName }
        textViewVO.text = languageName

        val textViewGenres = findViewById<TextView>(R.id.detail_genres)
        val strGenres = detail_movie.genres?.joinToString(separator = ", ") { it.name }
        textViewGenres.text = strGenres

        val actorRecyclerView = findViewById<RecyclerView>(R.id.detail_actors_recyclerview)
        val ActorLayoutManager = LinearLayoutManager(this)
        ActorLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        actorRecyclerView.layoutManager = ActorLayoutManager
        actorRecyclerView.adapter = ActorAdapter(this, detail_movie.credits.cast)

        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.detail_youtube_player_view)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = detail_movie.videos?.results?.firstOrNull()?.key
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            }
        })
        share3 += "https://www.youtube.com/watch?v=" + detail_movie.videos?.results?.firstOrNull()?.key


        val recRecyclerView = findViewById<RecyclerView>(R.id.detail_movies_recyclerview)
        val recLayoutManager = LinearLayoutManager(this)
        recLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recRecyclerView.layoutManager = recLayoutManager
        recRecyclerView.adapter =
            detail_movie.recommendations?.results?.let {
                genres?.let { it1 ->
                    MovieAdapter(
                        this, it,
                        it1,
                        utilisateur
                    )
                }
            }
    }

    private fun saveFavorites() {
        val utilisateurs = GetUtilisateurMemory(this)
        for (i in utilisateurs.indices) {
            val user = utilisateurs[i]
            if (user.id == utilisateur?.id) {
                utilisateurs[i] = utilisateur!!
            }
        }
        saveUtilisateur(utilisateurs, this)
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
            val budget = movied?.budget
            val revenue = movied?.revenue
            val languageVo = movied?.original_language
            val overview = movied?.overview
            val runtime = movied?.runtime

            val actors: List<Actor>
            actors = movied?.credits?.cast?.filter {
                it.order <= 10 && it.known_for_department == "Acting"
            }?.map {
                Actor(
                    it.name,
                    it.profile_path,
                    it.character,
                    it.known_for_department,
                    it.order
                )
            }!!

            moviedd = MovieD(
                movied.genres,
                overview,
                runtime,
                budget,
                revenue,
                languageVo,
                movied.videos,
                movied.recommendations,
                creditsResponse(actors),
                movied.title,
                movied.vote_average,
                movied.poster_path,
                movied.backdrop_path,
                movied.release_date,
                movied.popularity
            )
        }
        return moviedd
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_comment -> {
                val intent = Intent(this, CommentActivity::class.java)
                intent.putExtra("utilisateur", utilisateur)
                intent.putExtra("movie", movie)
                this.startActivity(intent)
            }
            R.id.action_share -> {
                val shareBuilder = StringBuilder()
                shareBuilder.append(share1).append("\n")
                shareBuilder.append(share2).append("\n")
                shareBuilder.append(share3)

                val shareText = shareBuilder.toString()

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sujet du partage")
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(intent, "Partager avec"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}