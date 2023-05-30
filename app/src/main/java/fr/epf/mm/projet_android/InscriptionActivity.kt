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
        error.visibility=View.GONE

        supportActionBar?.setTitle("Inscription")



        val inscriptionButton = findViewById<Button>(R.id.creation_compte_activity)
        inscriptionButton.setOnClickListener {
            val pseudoTv = findViewById<TextView>(R.id.pseudo_creation_compte)
            val motDePasseTv= findViewById<TextView>(R.id.mot_passe_creation)
            val prenomTv =findViewById<TextView>(R.id.prenom_creation_compte)
            val nomTv = findViewById<TextView>(R.id.nom_creation_compte)

            val pseudo = pseudoTv.text.toString()
            val motDePasse = motDePasseTv.text.toString()
            val prenom = prenomTv.text.toString()
            val nom = nomTv.text.toString()
            Log.d("EPF", "onCreate: ${pseudo}${motDePasse}${nom}${prenom}, pseudo : ${pseudo.length}")
            if (pseudo.length !=0  && motDePasse.length!=0 && prenom.length!=0 && nom.length!=0){
                //val ancienUtilisateur : List<Utilisateur>
                val ancienUtilisateurs = GetUtilisateurMemory(this)
                Log.d("EPF2", "utilisateurs en mémoire: ${ancienUtilisateurs.size}")

                val utilisateurN = Utilisateur(ancienUtilisateurs.size,nom,prenom,pseudo,motDePasse,null, null)
                Log.d("EPF3", "nouveau utilisateur: ${utilisateurN.id}${utilisateurN.pseudo}${utilisateurN.motDePasse}")

                var existant =false
                for (user in ancienUtilisateurs){
                    if (user.pseudo==pseudo){
                        existant=true
                    }
                }
                if (existant ==false){
                    error.visibility=View.GONE
                    ancienUtilisateurs.add(utilisateurN)
                    Log.d("EPF4", "ajout nouveau utilisateur: ${ancienUtilisateurs.size}")

                    saveUtilisateur(ancienUtilisateurs)
                    //Log.d("EPF5", "nouveau utilisateur: ${utilisateurN.id}${utilisateurN.pseudo}${utilisateurN.motDePasse}")

                    val intent = Intent(this, MenuAccueilActivity::class.java)
                    intent.putExtra("utilisateur",utilisateurN)
                    startActivity(intent)
                } else {
                    error.text = "Le pseudo que vous avez saisit correspond déjà à celui d'un autre utilisateur, veuillez changer votre pseudo"
                    error.visibility=View.VISIBLE
                }

            } else {
                error.text = "Il y a au moins un champ non renseigné, veuillez compléter les champs vides."
                error.visibility=View.VISIBLE
                Log.d("EPF", "NON")
            }

        }
    }

    private fun saveUtilisateur(utilisateurs: List<Utilisateur>) {
        val gson = Gson()

        val sharedPreferences = getSharedPreferences("utilisateurs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putString("utilisateurs", gson.toJson(utilisateurs))
        editor?.apply()
    }



    companion object

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



