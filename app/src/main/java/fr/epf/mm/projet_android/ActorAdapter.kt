package fr.epf.mm.projet_android

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view)


class ActorAdapter(val context: Context, val actors: List<Actor>) :  RecyclerView.Adapter<ActorViewHolder>() {
/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_movie, parent, false)
        return MovieViewHolder(view)
    }*/



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.view_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun getItemCount() = actors.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]
        val view = holder.itemView


        val ImageView = view.findViewById<ImageView>(R.id.actor_profile)
        if (actor.profile_path != null) {
            loadImage(actor.profile_path, ImageView)
        }else {
            val linearLayout = view.findViewById<LinearLayout>(R.id.actor_layout)
            linearLayout.background = ColorDrawable(ContextCompat.getColor(context, R.color.black))
            ImageView.setImageResource(R.drawable.baseline_person_24)
        }


        val textViewName = view.findViewById<TextView>(R.id.actor_name)
        textViewName.text = actor.name

        val textViewChararcter = view.findViewById<TextView>(R.id.actor_character)
        textViewChararcter.text = actor.character

    }


}























