package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Utilisateur

class CompteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compte)
        val deconnexionButton = findViewById<Button>(R.id.deconexion_compte)
        val miseAJourButton=findViewById<Button>(R.id.mise_a_jour_compte)

        val utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        val nomEditText=findViewById<EditText>(R.id.nom_compte)
        nomEditText.text=utilisateur?.nom
        val prenomEditText=findViewById<EditText>(R.id.prenom_compte)
        prenomEditText.text=utilisateur?.prenom
        val pseudoEditText=findViewById<EditText>(R.id.pseudo_compte)
        pseudoEditText.text=utilisateur?.pseudo

        val FavoriRecyclerView  = findViewById<RecyclerView>(R.id.liste_film_favori_recyclerView)
        val layoutManagerPop = LinearLayoutManager(this)
        for(movie in utilisateur?.favoris!!){
        layoutManagerPop.orientation = LinearLayoutManager.HORIZONTAL
        FavoriRecyclerView.layoutManager = layoutManagerPop
            val genre
            FavoriRecyclerView.adapter = MovieAdapter(this, movie,genre!!)}

        deconnexionButton.click {
            val intent = Intent(this, MenuAccueilActivity::class.java)
            startActivity(intent)
        }

        miseAJourButton.click {
            val nomEditText=findViewById<EditText>(R.id.nom_compte)
            val prenomEditText=findViewById<EditText>(R.id.prenom_compte)
            val pseudoEditText=findViewById<EditText>(R.id.pseudo_compte)

            val pseudo = pseudoEditText.text.toString()
            val prenom = prenomEditText.text.toString()
            val nom = nomEditText.text.toString()

            Log.d("nouveau compte", "onCreate: ${pseudo}${nom}${prenom}")

            val ancienUtilisateurs = GetUtilisateurMemory(this)
            Log.d("EPF2", "utilisateurs en mémoire: ${ancienUtilisateurs.size}")

            for(i in ancienUtilisateurs.indices){
                if(ancienUtilisateurs[i].id==utilisateur?.id){
                    ancienUtilisateurs[i].pseudo=pseudo
                    ancienUtilisateurs[i].nom=nom
                    ancienUtilisateurs[i].prenom=prenom
                }
            }

            saveUtilisateur(ancienUtilisateurs)


        }
    }

    fun View.click(action : (View) -> Unit){
        Log.d("CLICK", "click")
        this.setOnClickListener(action)
    }

    private fun saveUtilisateur(utilisateurs: List<Utilisateur>) {
        val gson = Gson()

        val sharedPreferences = getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putString("utilisateurs", gson.toJson(utilisateurs))
        editor?.apply()
    }

    fun GetUtilisateurMemory(context: Context): MutableList<Utilisateur> {
        val utilisateurs: MutableList<Utilisateur> = mutableListOf()
        val sharedPreferences = context.getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
        val utilisateursJson = sharedPreferences.getString("utilisateurs", null)
        val gson = Gson()
        if (!utilisateursJson.isNullOrEmpty()) {
            val utilisateurMem = gson.fromJson(utilisateursJson, Array<Utilisateur>::class.java).toMutableList()
            Log.d("EPF", "utilisateurs: $utilisateurMem")
            utilisateurs.addAll(utilisateurMem)
        }
        return utilisateurs
    }

    companion object



}

