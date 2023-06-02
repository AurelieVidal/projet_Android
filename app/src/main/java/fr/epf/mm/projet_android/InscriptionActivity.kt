package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Utilisateur
import java.util.*

class InscriptionActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)

        val error = findViewById<TextView>(R.id.inscription_error)
        error.visibility = View.GONE

        supportActionBar?.setTitle("Inscription")

        val inscriptionButton = findViewById<Button>(R.id.creation_compte_activity)
        inscriptionButton.setOnClickListener {
            val pseudoTv = findViewById<TextView>(R.id.pseudo_creation_compte)
            val motDePasseTv = findViewById<TextView>(R.id.mot_passe_creation)
            val prenomTv = findViewById<TextView>(R.id.prenom_creation_compte)
            val nomTv = findViewById<TextView>(R.id.nom_creation_compte)

            val pseudo = pseudoTv.text.toString()
            val motDePasse = motDePasseTv.text.toString()
            val prenom = prenomTv.text.toString()
            val nom = nomTv.text.toString()
            if (pseudo.length != 0 && motDePasse.length != 0 && prenom.length != 0 && nom.length != 0) {
                val ancienUtilisateurs = GetUtilisateurMemory(this)
                val utilisateurN = Utilisateur(
                    ancienUtilisateurs.size,
                    nom,
                    prenom,
                    pseudo,
                    motDePasse,
                    null,
                    null
                )
                var existant = false
                for (user in ancienUtilisateurs) {
                    if (user.pseudo == pseudo) {
                        existant = true
                    }
                }
                if (existant == false) {
                    error.visibility = View.GONE
                    ancienUtilisateurs.add(utilisateurN)
                    saveUtilisateur(ancienUtilisateurs, this)
                    val intent = Intent(this, MenuAccueilActivity::class.java)
                    intent.putExtra("utilisateur", utilisateurN)
                    startActivity(intent)
                } else {
                    error.text = getString(R.string.Incription_error_pseudo)
                    error.visibility = View.VISIBLE
                }
            } else {
                error.text = getString(R.string.Inscription_error_empty)
                error.visibility = View.VISIBLE
            }

        }
    }
}

fun GetUtilisateurMemory(context: Context): MutableList<Utilisateur> {
    val utilisateurs: MutableList<Utilisateur> = mutableListOf()
    val sharedPreferences = context.getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
    val utilisateursJson = sharedPreferences.getString("utilisateurs", null)
    val gson = Gson()
    if (!utilisateursJson.isNullOrEmpty()) {
        val utilisateurMem =
            gson.fromJson(utilisateursJson, Array<Utilisateur>::class.java).toMutableList()
        utilisateurs.addAll(utilisateurMem)
    }
    return utilisateurs
}



