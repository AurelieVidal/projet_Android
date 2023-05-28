package fr.epf.mm.projet_android

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.projet_android.model.Movie
import fr.epf.mm.projet_android.model.Utilisateur


class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MovieAdapter(val context: Context, val movies: List<Movie>, val IDs: List<Genre>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_movie, parent, false)
        return MovieViewHolder(view)
    }*/



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.view_movie, parent, false)
        return MovieViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val movie = movies[position]
        val view = holder.itemView


        val textViewTitle = view.findViewById<TextView>(R.id.view_movie_title)


        val strTitle = movie.title
        val textSize = if (strTitle.length > 130) {
            14f
        } else if (strTitle.length > 85) {
            16f
        } else {
            20f
        }

        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        textViewTitle.text = strTitle

        val textViewYear = view.findViewById<TextView>(R.id.view_movie_year)
        if (movie.release_date.length >4) {
            textViewYear.text = movie.release_date.substring(0, 4)
        } else {
            textViewYear.text = "Date inconnue"
        }


        val textViewVote = view.findViewById<TextView>(R.id.view_movie_vote)
        textViewVote.text = movie.vote_average.toString()

        if (movie.vote_average < 4) {
            textViewVote.setTextColor(Color.rgb(204,0,0))
        }else if (movie.vote_average<7){
            textViewVote.setTextColor(Color.rgb(255,192,0))
        }
        else {
            textViewVote.setTextColor(Color.rgb(146,208,80))
        }






        val textViewGenres = view.findViewById<TextView>(R.id.view_movie_genres)



        val strgenres = movie.genre_ids.map { getStrGenre(it, IDs) }.joinToString(", ")

        textViewGenres.text = strgenres

        val imageViewBack = view.findViewById<ImageView>(R.id.view_movie_back)

        Log.d("EPF", "${movie.title} : ${movie.backdrop_path}")
        if (movie.backdrop_path != null){
            Log.d("EPF", "loadImage")
            loadImage(movie.backdrop_path, imageViewBack)

        }else {
            imageViewBack.setImageDrawable(null) // retire la source de l'image
            imageViewBack.setBackgroundColor(Color.rgb(46,46,46)) // met un fond noir
        }



        val imageViewPoster = view.findViewById<ImageView>(R.id.view_movie_poster)

        Log.d("EPF", "${movie.title} : ${movie.poster_path}")
        if (movie.poster_path != null){
            loadImage(movie.poster_path, imageViewPoster)

        }else {
            imageViewPoster.setImageDrawable(null) // retire la source de l'image
            imageViewPoster.setBackgroundColor(Color.rgb(59,56,56)) // met un fond noir
        }

        val cardView = view.findViewById<CardView>(R.id.view_movie_cardview)
/*
        for (fav in MainActivity.movies_fav){
            if (fav.id == movie.id){
                movie.favori = true
            }
        }*/
        cardView.click {


            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("movie", movie)
            intent.putParcelableArrayListExtra("genres", ArrayList(IDs))
            context.startActivity(intent)



        }


    }


    override fun getItemCount() = movies.size





    fun View.click(action : (View) -> Unit){
        Log.d("CLICK", "click")
        this.setOnClickListener(action)
    }
}

fun getStrGenre (genreId: Long, genres: List<Genre>): String {
    for (genre in genres){
        if (genre.id==genreId){
            return genre.name
        }
    }
    return ""
}






