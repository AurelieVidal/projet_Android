package fr.epf.mm.projet_android

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.projet_android.model.Commentaire
import fr.epf.mm.projet_android.model.Utilisateur

class CommentAdapter(val context: Context, val comments: List<Commentaire>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_comment, parent, false)
        return MovieViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val com = comments[position]
        val view = holder.itemView

        val textViewUser = view.findViewById<TextView>(R.id.view_comment_user)
        val user = getUserFromId(com.idUtilisateur)
        if (user != null) {
            textViewUser.text = user.pseudo
        } else {
            textViewUser.text = ""
        }

        val textViewContent = view.findViewById<TextView>(R.id.view_comment_content)
        textViewContent.text = com.contenu
    }

    private fun getUserFromId(idUtilisateur: Int?): Utilisateur? {
        val utilisateurs = GetUtilisateurMemory(context)
        for (user in utilisateurs) {
            if (user.id == idUtilisateur) {
                return user
            }
        }
        return null
    }

    override fun getItemCount() = comments.size
}
