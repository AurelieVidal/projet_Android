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
import fr.epf.mm.projet_android.model.Utilisateur

class FavorisActivity : AppCompatActivity() {
    lateinit var genres: List<Genre>
    private var utilisateur : Utilisateur? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoris)

        supportActionBar?.setTitle("Favoris")

        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        Log.d("EPF", "onCreate: $utilisateur")
        genres = intent.getParcelableArrayListExtra<Genre>("genres")!!


        var favoris = getUserFav()


        if (favoris != null) {
            val FavRecyclerView = findViewById<RecyclerView>(R.id.favoris_recycler_view)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            FavRecyclerView.layoutManager = layoutManager
            FavRecyclerView.adapter = MovieAdapter(this, favoris!!, genres!!, utilisateur)
        }
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
        var favoris = getUserFav()

        if (favoris !=null){
            val FavRecyclerView  = findViewById<RecyclerView>(R.id.favoris_recycler_view)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            FavRecyclerView.layoutManager = layoutManager
            FavRecyclerView.adapter = MovieAdapter(this, favoris!!, genres!!, utilisateur)
        }

    }


    private fun getUserFav() : List<Movie>{
        val utilisateurs = GetUtilisateurMemory(this)
        var favs : List<Movie>  = listOf()
        for (user in utilisateurs){
            if (user.id == utilisateur?.id){
                if (user.favoris!= null){
                    favs =  user.favoris!!
                }
            }
        }
        return favs
    }
}