package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import fr.epf.mm.projet_android.model.Utilisateur
import java.util.*
import fr.epf.mm.projet_android.InscriptionActivity as InscriptionActivity
import fr.epf.mm.projet_android.MainActivity as MainActivity

class MenuAccueilActivity : AppCompatActivity() {
    var idUtilisateur: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_accueil)

        supportActionBar?.setTitle("Connexion")

        var utilisateur = intent.extras?.get("utilisateur") as? Utilisateur

        val erreur = findViewById<TextView>(R.id.erreur_menu_activitty)
        erreur.visibility = View.GONE

        val connexionButton = findViewById<Button>(R.id.connexion_menu_accueil_activity)
        val inscriptionButton = findViewById<Button>(R.id.inscription_menu_accueil_activity)
        connexionButton.setOnClickListener {
            val pseudoTV = findViewById<TextView>(R.id.pseudo_accueil_menu)
            val motDePasseTV = findViewById<TextView>(R.id.mot_passe_menu_activitty)
            val pseudo = pseudoTV.text.toString()
            val motDePasse = motDePasseTV.text.toString()

            val utilisateurs = GetUtilisateurMemory(this)
            var estInscrit = false

            for (utils in utilisateurs) {
                if ((pseudo.equals(utils.pseudo)) && (motDePasse.equals(utils.motDePasse))) {
                    estInscrit = true
                    idUtilisateur = utils.id
                    utilisateur = utils
                }
            }

            if (estInscrit) {
                erreur.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("utilisateur", utilisateur)
                startActivity(intent)
            } else {
                erreur.visibility = View.VISIBLE
            }
        }

        inscriptionButton.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }
}










