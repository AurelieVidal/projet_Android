package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur

class CompteActivity : AppCompatActivity() {
    private var utilisateur : Utilisateur? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle("Mon compte")

        val genres = intent.getParcelableArrayListExtra<Genre>("genres")!!

        setContentView(R.layout.activity_compte)


        val miseAJourButton = findViewById<Button>(R.id.mise_a_jour_compte)

        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        val nomEditText = findViewById<EditText>(R.id.nom_compte)
        val prenomEditText = findViewById<EditText>(R.id.prenom_compte)
        val pseudoEditText = findViewById<EditText>(R.id.pseudo_compte)
        val user = getUser()
        if (user != null) {
            nomEditText.setText(user.nom)
            prenomEditText.setText(user.prenom)
            pseudoEditText.setText(user.pseudo)
        }


        val FavoriRecyclerView = findViewById<RecyclerView>(R.id.liste_film_favori_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        var favoris = getUserFav()
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        FavoriRecyclerView.layoutManager = layoutManager
        if (favoris != null) {
            FavoriRecyclerView.adapter = MovieAdapter(this, favoris, genres, utilisateur)
        }





        miseAJourButton.setOnClickListener {
            val nomEditText = findViewById<EditText>(R.id.nom_compte)
            val prenomEditText = findViewById<EditText>(R.id.prenom_compte)
            val pseudoEditText = findViewById<EditText>(R.id.pseudo_compte)

            val pseudo = pseudoEditText.text.toString()
            val prenom = prenomEditText.text.toString()
            val nom = nomEditText.text.toString()

            Log.d("nouveau compte", "onCreate: ${pseudo}${nom}${prenom}")

            val ancienUtilisateurs = GetUtilisateurMemory(this)
            Log.d("EPF2", "utilisateurs en mémoire: ${ancienUtilisateurs.size}")

            for (i in ancienUtilisateurs.indices) {
                if (ancienUtilisateurs[i].id == utilisateur?.id) {
                    ancienUtilisateurs[i].pseudo = pseudo
                    ancienUtilisateurs[i].nom = nom
                    ancienUtilisateurs[i].prenom = prenom
                }
            }

            saveUtilisateur(ancienUtilisateurs)

        }




    }

    private fun getUser() : Utilisateur{
        val utilisateurs = GetUtilisateurMemory(this)
        lateinit var userReturn : Utilisateur
        for (user in utilisateurs){
            if (user.id == utilisateur?.id){
                userReturn = user
            }
        }
        return userReturn
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_diconnect -> {
                val intent = Intent(this, MenuAccueilActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveUtilisateur(utilisateurs: List<Utilisateur>) {
        val gson = Gson()

        val sharedPreferences = getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putString("utilisateurs", gson.toJson(utilisateurs))
        editor?.apply()
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
