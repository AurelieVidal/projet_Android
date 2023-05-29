package fr.epf.mm.projet_android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Movie

class FavorisActivity : AppCompatActivity() {
    lateinit var favoris: List<Movie>
    lateinit var genres: List<Genre>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoris)

        supportActionBar?.setTitle("Favoris")

        genres = intent.getParcelableArrayListExtra<Genre>("genres")!!
        favoris = GetFavMemory()

        val FavRecyclerView  = findViewById<RecyclerView>(R.id.favoris_recycler_view)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        FavRecyclerView.layoutManager = layoutManager
        FavRecyclerView.adapter = MovieAdapter(this, favoris, genres!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favoris_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sync -> {
                sync()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sync() {
        favoris = GetFavMemory()
        val FavRecyclerView  = findViewById<RecyclerView>(R.id.favoris_recycler_view)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        FavRecyclerView.layoutManager = layoutManager
        FavRecyclerView.adapter = MovieAdapter(this, favoris, genres!!)
    }


    private fun GetFavMemory(): List<Movie> {
        val favoris: MutableList<Movie> = mutableListOf()
        val sharedPreferences = getSharedPreferences("Favoris", Context.MODE_PRIVATE)
        val moviesJson = sharedPreferences.getString("movies", null)
        val gson = Gson()
        if (!moviesJson.isNullOrEmpty()) { // Vérifie si moviesJson n'est pas null ou vide
            val movies = gson.fromJson(moviesJson, Array<Movie>::class.java).toMutableList()
            Log.d("EPF", "Movies: $movies")
            favoris.addAll(movies)
        }
        return favoris.toList()
    }
}