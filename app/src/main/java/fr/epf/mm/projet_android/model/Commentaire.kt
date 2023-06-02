package fr.epf.mm.projet_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Commentaire(
    val contenu: String,
    val idMovie: Long?,
    val id: Int,
    val idUtilisateur: Int?,
    val date: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Commentaire

        if (id != other.id) return false
        if (contenu != other.contenu) return false
        if (idMovie != other.idMovie) return false
        if (idUtilisateur != other.idUtilisateur) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + contenu.hashCode()
        result = 31 * result + idMovie.hashCode()
        result = 31 * result + idUtilisateur.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}