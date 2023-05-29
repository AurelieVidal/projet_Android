package fr.epf.mm.projet_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Utilisateur(
    val id: Int,
    var nom: String,
    var prenom: String,
    var pseudo: String,
    val motDePasse: String,
    val favoris: List<Movie>?,
    val comments: List<Commentaire>?
) : Parcelable {



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Utilisateur

        if (id != other.id) return false
        if (nom != other.nom) return false
        if (prenom != other.prenom) return false
        if (pseudo != other.pseudo) return false
        if (motDePasse != other.motDePasse) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nom.hashCode()
        result = 31 * result + prenom.hashCode()
        result = 31 * result + pseudo.hashCode()
        result = 31 * result + motDePasse.hashCode()
        return result
    }
}
