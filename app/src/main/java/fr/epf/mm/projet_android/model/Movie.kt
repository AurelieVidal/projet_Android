package fr.epf.mm.projet_android.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Long,
    val title: String,
    val release_date: String,
    val vote_average: Float,
    val genre_ids: Array<Long>,
    val poster_path: String?,
    val backdrop_path: String?,
    val popularity: Float?,
    val original_language: String,

    ) : Parcelable {

    var favori: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (release_date != other.release_date) return false
        if (vote_average != other.vote_average) return false
        if (!genre_ids.contentEquals(other.genre_ids)) return false
        if (poster_path != other.poster_path) return false
        if (backdrop_path != other.backdrop_path) return false
        if (popularity != other.popularity) return false
        if (original_language != other.original_language) return false
        if (favori != other.favori) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + release_date.hashCode()
        result = 31 * result + vote_average.hashCode()
        result = 31 * result + genre_ids.contentHashCode()
        result = 31 * result + (poster_path?.hashCode() ?: 0)
        result = 31 * result + (backdrop_path?.hashCode() ?: 0)
        result = 31 * result + (popularity?.hashCode() ?: 0)
        result = 31 * result + original_language.hashCode()
        result = 31 * result + favori.hashCode()
        return result
    }
}

