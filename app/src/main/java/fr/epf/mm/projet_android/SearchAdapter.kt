package fr.epf.mm.projet_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class GenreViewHolder(view: View, listener: SearchAdapter.OnItemClickListener) :
    RecyclerView.ViewHolder(view) {
    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
}

class SearchAdapter(
    val context: Context,
    val genres: List<Genre>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<GenreViewHolder>() {
    private var actives: MutableList<Boolean> = MutableList(genres.size) { true }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cardView = inflater.inflate(R.layout.view_genre, parent, false)
        return GenreViewHolder(cardView, listener)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        val view = holder.itemView

        val textViewContent = view.findViewById<TextView>(R.id.view_genre_text_view)
        textViewContent.text = genre.name

        val cardView = view.findViewById<CardView>(R.id.view_genre_cardview)
        val backgroundColorInactive = ContextCompat.getColor(context, R.color.background)
        val backgroundColorActive = ContextCompat.getColor(context, R.color.light_gray)
        val isCardActive = actives[position]
        if (isCardActive) {
            textViewContent.setBackgroundColor(backgroundColorActive)
        } else {
            textViewContent.setBackgroundColor(backgroundColorInactive)
        }

        cardView.click {
            actives[position] = !actives[position]
            if (actives[position]) {
                textViewContent.setBackgroundColor(backgroundColorActive)
            } else {
                textViewContent.setBackgroundColor(backgroundColorInactive)
            }
            listener.onItemColorChanged(position, actives[position])
        }
    }

    override fun getItemCount() = genres.size

    fun View.click(action: (View) -> Unit) {
        this.setOnClickListener(action)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemColorChanged(position: Int, isActive: Boolean)
    }
}
