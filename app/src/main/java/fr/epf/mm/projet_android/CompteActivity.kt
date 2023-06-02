package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Utilisateur

class CompteActivity : AppCompatActivity() {
    private var utilisateur: Utilisateur? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compte)

        supportActionBar?.setTitle("Mon compte")

        val genres = intent.getParcelableArrayListExtra<Genre>("genres")!!
        utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        val miseAJourButton = findViewById<Button>(R.id.mise_a_jour_compte)
        val nomEditText = findViewById<EditText>(R.id.nom_compte)
        val prenomEditText = findViewById<EditText>(R.id.prenom_compte)
        val pseudoEditText = findViewById<EditText>(R.id.pseudo_compte)
        val user = getUser()
        nomEditText.setText(user.nom)
        prenomEditText.setText(user.prenom)
        pseudoEditText.setText(user.pseudo)

        val FavoriRecyclerView = findViewById<RecyclerView>(R.id.liste_film_favori_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        val favoris = getUserFav(utilisateur!!, this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        FavoriRecyclerView.layoutManager = layoutManager
        FavoriRecyclerView.adapter = MovieAdapter(this, favoris, genres, utilisateur)

        miseAJourButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_account_update), Toast.LENGTH_SHORT).show()

            val nomEditText = findViewById<EditText>(R.id.nom_compte)
            val prenomEditText = findViewById<EditText>(R.id.prenom_compte)
            val pseudoEditText = findViewById<EditText>(R.id.pseudo_compte)

            val pseudo = pseudoEditText.text.toString()
            val prenom = prenomEditText.text.toString()
            val nom = nomEditText.text.toString()

            val ancienUtilisateurs = GetUtilisateurMemory(this)

            for (i in ancienUtilisateurs.indices) {
                if (ancienUtilisateurs[i].id == utilisateur?.id) {
                    ancienUtilisateurs[i].pseudo = pseudo
                    ancienUtilisateurs[i].nom = nom
                    ancienUtilisateurs[i].prenom = prenom
                }
            }
            saveUtilisateur(ancienUtilisateurs, this)
        }
    }

    private fun getUser(): Utilisateur {
        val utilisateurs = GetUtilisateurMemory(this)
        lateinit var userReturn: Utilisateur
        for (user in utilisateurs) {
            if (user.id == utilisateur?.id) {
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
}

fun saveUtilisateur(utilisateurs: List<Utilisateur>, context: Context) {
    val gson = Gson()
    val sharedPreferences = context.getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
    val editor = sharedPreferences?.edit()
    editor?.clear()
    editor?.putString("utilisateurs", gson.toJson(utilisateurs))
    editor?.apply()
}