package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var genres: List<Genre>
    private var utilisateur: Utilisateur? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle("Accueil")

        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        val PopularRecyclerView  = findViewById<RecyclerView>(R.id.home_popular_recyclerView)
        val TopRecyclerView  = findViewById<RecyclerView>(R.id.home_top_recyclerView)
        val FrRecyclerView  = findViewById<RecyclerView>(R.id.home_fr_recyclerView)
        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur
        genres = Genres()


        val layoutManagerPop = LinearLayoutManager(this)
        layoutManagerPop.orientation = LinearLayoutManager.HORIZONTAL
        PopularRecyclerView.layoutManager = layoutManagerPop
        PopularRecyclerView.adapter = MovieAdapter(this, Popular(), genres, utilisateur)


        val layoutManagerTop = LinearLayoutManager(this)
        layoutManagerTop.orientation = LinearLayoutManager.HORIZONTAL
        TopRecyclerView.layoutManager = layoutManagerTop
        TopRecyclerView.adapter = MovieAdapter(this, TopRated(), genres, utilisateur)

        val layoutManagerFr = LinearLayoutManager(this)
        layoutManagerFr.orientation = LinearLayoutManager.HORIZONTAL
        FrRecyclerView.layoutManager = layoutManagerFr
        FrRecyclerView.adapter = MovieAdapter(this, French(), genres, utilisateur)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> {
                val intent = Intent(this, FavorisActivity::class.java)
                intent.putParcelableArrayListExtra("genres", ArrayList(genres))
                intent.putExtra("utilisateur", utilisateur)
                this.startActivity(intent)
            }
            R.id.action_QRCode -> {
                val intent = Intent(this, ScannerActivity::class.java)
                intent.putParcelableArrayListExtra("genres", ArrayList(genres))
                this.startActivity(intent)

            }
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putParcelableArrayListExtra("genres", ArrayList(genres))
                intent.putExtra("utilisateur", utilisateur)
                this.startActivity(intent)
            }
            R.id.action_compte -> {
                val intent = Intent(this, CompteActivity::class.java)
                intent.putExtra("utilisateur",utilisateur)
                intent.putParcelableArrayListExtra("genres", ArrayList(genres))
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }






    private fun Popular(): List<Movie> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()
        var movies_pop: List<Movie>
        val service = retrofit.create(TMBDPopular::class.java)
        val apiKey = "31e4672492df89a26175c865fed7271a"
        runBlocking {

            movies_pop = service.getMovies(apiKey).results.map {
                Movie (
                    it.id,
                    it.title,
                    it.release_date,
                    it.vote_average,
                    it.genre_ids,
                    it.poster_path,
                    it.backdrop_path,
                    it.popularity,
                    it.original_language
                )


            }

        }
        return movies_pop
    }



    private fun TopRated(): List<Movie> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()

        var movies_top: List<Movie>
        val service = retrofit.create(TMBDTopRated::class.java)
        val apiKey = "31e4672492df89a26175c865fed7271a"
        runBlocking {

            movies_top = service.getMovies(apiKey).results.map {
                Movie (
                    it.id,
                    it.title,
                    it.release_date,
                    it.vote_average,
                    it.genre_ids,
                    it.poster_path,
                    it.backdrop_path,
                    it.popularity,
                    it.original_language
                )

            }
        }

        return movies_top

    }

    private fun French(): List<Movie> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()
        var movies_fr: List<Movie>
        val service = retrofit.create(TMBDFrench::class.java)
        val apiKey = "31e4672492df89a26175c865fed7271a"
        runBlocking {

            movies_fr = service.getMovies(apiKey).results.map {
                Movie (
                    it.id,
                    it.title,
                    it.release_date,
                    it.vote_average,
                    it.genre_ids,
                    it.poster_path,
                    it.backdrop_path,
                    it.popularity,
                    it.original_language
                )


            }
        }
        return movies_fr

    }

    private fun Genres (): List<Genre> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()

        val service = retrofit.create(TMDBGenres::class.java)
        val genres: List<Genre>
        runBlocking {
            val response = service.GetGenresResult()
            genres = response.genres.map {
                Genre (
                    it.id,
                    it.name
                )
            }
        }
        return genres
    }



}

fun loadImage(url: String?, imageView: ImageView) {
    if (url != null){
        Glide.with(imageView)
            .load("https://image.tmdb.org/t/p/w500$url")
            .into(imageView)
    }

}

