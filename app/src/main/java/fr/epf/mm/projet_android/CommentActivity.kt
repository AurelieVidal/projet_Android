package fr.epf.mm.projet_android

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.epf.mm.projet_android.model.Commentaire
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CommentActivity : AppCompatActivity() {
    private lateinit var RecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)


        val utilisateur = intent.extras?.get("utilisateur") as? Utilisateur
        val movie = intent.extras?.get("movie") as? Movie


        val comments = GetCommentsMemory(movie!!)
        val commentsM = comments.toMutableList()

        var coms : MutableList<Commentaire> = mutableListOf()
        for (com in comments){

            if (com.idMovie == movie.id) {
                coms.add(com)
            }
        }
        val trie = trierCommentsParDate (coms)
        RecyclerView  = findViewById<RecyclerView>(R.id.comment_recycler_view)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        RecyclerView.layoutManager = layoutManager
        RecyclerView.adapter = CommentAdapter(this, trie)

        val button = findViewById<Button>(R.id.comment_add_comment)
        val editText = findViewById<EditText>(R.id.comment_content)
        button.setOnClickListener {
            if (utilisateur != null) {
                Log.d("EPF", "AJOUTER UN COM")
                updateComments(editText.text.toString(), movie, utilisateur, comments.size, commentsM)
                val comments = GetCommentsMemory(movie!!)
                var coms : MutableList<Commentaire> = mutableListOf()
                for (com in comments){
                    if (com.idMovie == movie.id) {
                        coms.add(com)
                        Log.d("EPF", "onCreate: ${com.contenu}")
                    }
                }
                val trie = trierCommentsParDate (coms)
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerView.layoutManager = layoutManager
                RecyclerView.adapter = CommentAdapter(this, trie)
                editText.setText("")
            }

        }


    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateComments(content: String, movie: Movie, user: Utilisateur, id: Int, comments: MutableList<Commentaire>) {
        val commentaire = Commentaire(content, movie.id, id, user.id, LocalDateTime.now().toString())
        Log.d("EPF", "updateComments: $commentaire")
        comments.add(commentaire)
        saveComents(comments)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun trierCommentsParDate(comments: MutableList<Commentaire>): List<Commentaire> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val defaultDate = Date(Long.MAX_VALUE)

        return comments.sortedByDescending {
            try {
                val date = dateFormatter.parse(it.date)
                date ?: defaultDate
            } catch (e: ParseException) {
                defaultDate
            }
        }
    }




    private fun GetCommentsMemory(movie: Movie) : MutableList<Commentaire> {
        val listComments: MutableList<Commentaire> = mutableListOf()
        val listCommentsIdMovie: MutableList<Commentaire> = mutableListOf()
        val sharedPreferences = getSharedPreferences("comments", Context.MODE_PRIVATE)
        val commentsJson = sharedPreferences.getString("comments", null)
        val gson = Gson()
        if (!commentsJson.isNullOrEmpty()) {
            val comments = gson.fromJson(commentsJson, Array<Commentaire>::class.java).toMutableList()
            //Log.d("EPF", "Commentaires: $comments")
            listComments.addAll(comments)
        }
        for (com in listComments){
            if(com.idMovie==movie?.id)
                listCommentsIdMovie.add(com)
        }
        return listCommentsIdMovie
    }

    private fun saveComents(Coments: List<Commentaire>) {
        val gson = Gson()

        val sharedPreferences = getSharedPreferences("comments", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.putString("comments", gson.toJson(Coments))
        editor?.apply()
    }


}