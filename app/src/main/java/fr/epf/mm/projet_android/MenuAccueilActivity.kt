package fr.epf.mm.projet_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
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
import fr.epf.mm.projet_android.InscriptionActivity as InscriptionActivity
import fr.epf.mm.projet_android.MainActivity as MainActivity


class MenuAccueilActivity : AppCompatActivity() {
    var idUtilisateur : Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_accueil)

        val erreur=findViewById<TextView>(R.id.erreur_menu_activitty)

        erreur.visibility=View.GONE

        val connexionButton = findViewById<Button>(R.id.connexion_menu_accueil_activity)
        val inscriptionButton = findViewById<Button>(R.id.inscription_menu_accueil_activity)

        var utilisateur = intent.extras?.get("utilisateur") as? Utilisateur



        // Condition pour activer ou désactiver le bouton
        connexionButton.setOnClickListener{
            val pseudoTV = findViewById<TextView>(R.id.pseudo_accueil_menu)
            val motDePasseTV = findViewById<TextView>(R.id.mot_passe_menu_activitty)

            val pseudo = pseudoTV.text.toString()
            val motDePasse = motDePasseTV.text.toString()

            var utilisateurs = GetUtilisateurMemory(this)

            var estInscrit= false
            Log.d("EPF test", "onCreate: ${pseudo}${motDePasse}${utilisateurs.size}")



            //on regarde si l'utilisateur a déjà un compte
            for (utils in utilisateurs){
                Log.d("EPF", "onCreate: ${pseudo}${motDePasse} et la suite c'est :${utils.pseudo}${utils.motDePasse}")

                if((pseudo.equals( utils.pseudo)) && (motDePasse.equals(utils.motDePasse))){
                    estInscrit=true
                    idUtilisateur=utils.id
                    utilisateur = utils
                }
            }

            if(estInscrit ){
                Log.d("EPF inscription", "il est connecté")

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("utilisateur",utilisateur)
                startActivity(intent)
            }
            else{
               erreur.visibility=View.VISIBLE
            }
        }


        inscriptionButton.setOnClickListener{
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }



    }










