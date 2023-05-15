package fr.epf.mm.projet_android
import android.os.Parcelable
import fr.epf.mm.projet_android.model.Movie
import kotlinx.parcelize.Parcelize
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMBDPopular {

    @GET("movie/popular?api_key=31e4672492df89a26175c865fed7271a&language=fr")
    suspend fun getMovies(@Query("api_key") apiKey: String): GetMoviesResult


}

interface TMBDTopRated {

    @GET("movie/top_rated?api_key=31e4672492df89a26175c865fed7271a&language=fr")
    suspend fun getMovies(@Query("api_key") apiKey: String): GetMoviesResult


}

interface TMBDSearch {

    @GET("search/movie")
    suspend fun getMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "fr"
    ): GetMoviesResult
}

interface TMBDFrench {

    @GET("discover/movie?api_key=31e4672492df89a26175c865fed7271a&language=fr-FR&sort_by=release_date.desc&with_original_language=fr&vote_count.gte=50")
    suspend fun getMovies(@Query("api_key") apiKey: String): GetMoviesResult


}

interface TMBDTest {

    @GET("movie/{id}?api_key=31e4672492df89a26175c865fed7271a")
    suspend fun getMovies(@Path("id") id: Long, @Query("api_key") apiKey: String) : GetMoviesResult


}
data class GetMoviesResult (val results: List<Movie>)

interface TMDBGenres {

    @GET("genre/movie/list?api_key=31e4672492df89a26175c865fed7271a&language=fr")
    suspend fun GetGenresResult(): GetGenresResult


}

@Parcelize
data class Genre(val id: Long, val name: String) : Parcelable
data class GetGenresResult(val genres: List<Genre>)

interface MovieApi {
    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("append_to_response") appendToResponse: String
    ): Response<MovieD>
}
data class MovieD(
    val genres: List<Genre>?,
    val overview: String?,
    val runtime: Long?,
    val budget: Long?,
    val revenue: Long?,
    val original_language: String?,
    val videos: VideoResponse?,
    val recommendations: RecommendationsResponse?,
    val credits: creditsResponse,
    val title: String,
    val vote_average: Float,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String,
    val popularity: Float?
)

data class creditsResponse(
    val cast: List<Actor>
)

data class Actor(
    val name: String,
    val profile_path: String?,
    val character: String,
    val known_for_department: String,
    val order: Int
)

data class VideoResponse(
    val results: List<Video>
)

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)

data class RecommendationsResponse(
    val results: List<Movie>
)

enum class TmdbLanguage(val fullName: String) {
    EN("Anglais"),
    FR("Français"),
    ES("Espagnol"),
    DE("Allemand"),
    IT("Italien"),
    PT("Portugais"),
    RU("Russe"),
    JA("Japonais"),
    KO("Coréen"),
    ZH("Chinois"),
    AR("Arabe"),
    HI("Hindi"),
    TR("Turc"),
    PL("Polonais"),
    NL("Néerlandais"),
    SV("Suédois"),
    NO("Norvégien"),
    DA("Danois"),
    FI("Finnois"),
    CS("Tchèque"),
    HU("Hongrois");

    companion object {
        fun fromCode(code: String?): TmdbLanguage? {
            return values().find { it.name == code?.toUpperCase() }
        }
    }
}




