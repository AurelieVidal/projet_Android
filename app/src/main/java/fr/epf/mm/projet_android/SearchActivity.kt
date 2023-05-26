package fr.epf.mm.projet_android


import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import fr.epf.mm.projet_android.model.Movie
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.ProcessBuilder.Redirect
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class SearchActivity : AppCompatActivity() {
    lateinit var genres: List<Genre>
    lateinit var movies: List<Movie>
    lateinit var actual_list: List<Movie>
    lateinit var tri_list: List<Movie>
    lateinit var genresResult : List<Genre>
    private var actives: MutableList<Boolean> = mutableListOf()
    private var yearUpLimit = 2200
    private var yearLowLimit = 0
    private var voteUpLimit = 10.0
    private var voteLowLimit = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        genres = intent.getParcelableArrayListExtra<Genre>("genres")!!


        //partie tri : OK
        val TextNumber = findViewById<TextView>(R.id.search_number_results)
        val RecyclerViewSearch = findViewById<RecyclerView>(R.id.search_recycler_view)
        val spinner: Spinner = findViewById(R.id.search_sort_spinner)
        val items = resources.getStringArray(R.array.levels).toList()
        val adapter =
            CustomSpinnerAdapter(this, items, ContextCompat.getColor(this, R.color.light_gray))
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val customAdapter = parent.adapter as? CustomSpinnerAdapter
                customAdapter?.setDialogOpen(true)

                val selectedItem = parent.getItemAtPosition(position) as String
                if (selectedItem == "Note") {
                    Log.d("EPF", "onItemSelected: PAR NOTE")
                    val moviesTries = trierMoviesParVoteAverage(tri_list)
                    tri_list = moviesTries
                    val filteredMovies = tri_list.filter { movie ->
                        movie.vote_average in voteLowLimit..voteUpLimit && movie.release_date.substring(0, 4).toInt() in yearLowLimit..yearUpLimit
                    }

                    val layoutManager = LinearLayoutManager(this@SearchActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    RecyclerViewSearch.layoutManager = layoutManager
                    RecyclerViewSearch.adapter = MovieAdapter(this@SearchActivity, filteredMovies, genres)
                    affichageNbFilms (TextNumber, filteredMovies.size)

                }
                try {
                    if (selectedItem == "Popularité") {
                        Log.d("EPF", "onItemSelected: PAR POP")
                        Log.d("EPF", "onItemSelected: PAR NOTE")
                        val moviesTries = trierMoviesParPopularity(tri_list)
                        tri_list = moviesTries
                        val filteredMovies = tri_list.filter { movie ->
                            movie.vote_average in voteLowLimit..voteUpLimit && movie.release_date.substring(0, 4).toInt() in yearLowLimit..yearUpLimit
                        }

                        val layoutManager = LinearLayoutManager(this@SearchActivity)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        RecyclerViewSearch.layoutManager = layoutManager
                        RecyclerViewSearch.adapter = MovieAdapter(this@SearchActivity, filteredMovies, genres)
                        affichageNbFilms (TextNumber, filteredMovies.size)
                    }
                } catch (e: java.lang.Exception) {}

                if (selectedItem == "Date") {
                    Log.d("EPF", "onItemSelected: PAR DATE")
                    val moviesTries = trierMoviesParDate(tri_list)
                    tri_list = moviesTries
                    val filteredMovies = tri_list.filter { movie ->
                        movie.vote_average in voteLowLimit..voteUpLimit && movie.release_date.substring(0, 4).toInt() in yearLowLimit..yearUpLimit
                    }

                    val layoutManager = LinearLayoutManager(this@SearchActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    RecyclerViewSearch.layoutManager = layoutManager
                    RecyclerViewSearch.adapter = MovieAdapter(this@SearchActivity, filteredMovies, genres)
                    affichageNbFilms (TextNumber, filteredMovies.size)

                }



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        //partie barre de recherche : OK
        val SearchButton = findViewById<ImageButton>(R.id.search_button)
        val EditTextSearch = findViewById<EditText>(R.id.search_edit_text)
        val rootView = window.decorView.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Clavier affiché
                SearchButton.setImageResource(R.drawable.baseline_close_24)
            } else {
                // Clavier masqué
                SearchButton.setImageResource(R.drawable.baseline_search_24)
            }
        }

        SearchButton.setOnClickListener {
            val inputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                // Clavier actif
                EditTextSearch.setText("")
            } else {
                // Clavier inactif -> activer le clavier
                inputMethodManager.showSoftInput(EditTextSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }


        //partie résultat de recherche  : ok
        val layoutFilter = findViewById<LinearLayout>(R.id.search_filter_layout)
        val layoutSort = findViewById<LinearLayout>(R.id.search_sort_layout)
        layoutFilter.visibility = View.INVISIBLE
        layoutSort.visibility = View.INVISIBLE

        EditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val inputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(EditTextSearch.windowToken, 0)

                val result = (EditTextSearch.text.toString())
                Log.d("EPF", "RECHERCHE: ${result}")

                //faire la requête pour la recherche
                movies = Results(result)
                actual_list = movies
                tri_list = movies
                Log.d("EPF", "onCreate: ${movies}")

                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                RecyclerViewSearch.layoutManager = layoutManager
                val adapter = MovieAdapter(this, movies, genres)
                RecyclerViewSearch.adapter = adapter

                if (movies.size == 0) {
                    TextNumber.text = "Aucun film trouvé"
                    layoutFilter.visibility = View.INVISIBLE
                    layoutSort.visibility = View.INVISIBLE
                } else if (movies.size == 1) {
                    TextNumber.text = "1 film trouvé"
                    layoutFilter.visibility = View.INVISIBLE
                    layoutSort.visibility = View.INVISIBLE
                } else {
                    TextNumber.text = "${movies.size} films trouvés"
                    layoutFilter.visibility = View.VISIBLE
                    layoutSort.visibility = View.VISIBLE
                }


                val genreIds = movies.flatMap { it.genre_ids.asList() }
                    .distinct()
                genresResult = genreIds.map { Genre(it, getStrGenre(it, genres)) }
                //initialister la liste
                for (g in genresResult){
                    actives.add(true)
                }



                val RWGenres = findViewById<RecyclerView>(R.id.search_recycler_genres)
                val layoutManagerGenres = LinearLayoutManager(this)
                layoutManagerGenres.orientation = LinearLayoutManager.HORIZONTAL
                RWGenres.layoutManager = layoutManagerGenres
                RWGenres.adapter = SearchAdapter(this, genresResult, object : SearchAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        // Autre logique à exécuter lorsque l'élément est cliqué
                    }

                    override fun onItemColorChanged(position: Int, isActive: Boolean) {
                        actives[position] = !actives[position]


                        for (b in actives){
                            Log.d("EPF", "${b}")
                        }

                        val clone= mutableListOf<Movie>()

                        for (movie in movies) {
                            val genre_Ids = movie.genre_ids

                            var allGenresSelected = true

                            for (genreId in genre_Ids) {
                                val genreIndex = genreIds.indexOfFirst { it == genreId }

                                if (!actives[genreIndex]) {
                                    allGenresSelected = false
                                    break
                                }
                            }

                            if (allGenresSelected) {
                                clone.add(movie)
                            }
                        }

                        tri_list = clone



                        //récup la valeur de tri et l'appliquer
                        if (spinner.selectedItem == "Note"){
                            tri_list = trierMoviesParVoteAverage(tri_list)
                        }
                        if (spinner.selectedItem == "Popularité"){
                            tri_list = trierMoviesParPopularity(tri_list)
                        }
                        if (spinner.selectedItem == "Date"){
                            tri_list = trierMoviesParDate(tri_list)

                        }

                        //ajuster le recycler
                        val filteredMovies = tri_list.filter { movie ->
                            movie.vote_average in voteLowLimit..voteUpLimit && movie.release_date.substring(0, 4).toInt() in yearLowLimit..yearUpLimit
                        }

                        val layoutManager = LinearLayoutManager(this@SearchActivity)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        RecyclerViewSearch.layoutManager = layoutManager
                        RecyclerViewSearch.adapter = MovieAdapter(this@SearchActivity, filteredMovies, genres)
                        affichageNbFilms (TextNumber, filteredMovies.size)


                    }
                })

                true
            } else {
                false
            }
        }


        //partie filtres

        val recyclerView = findViewById<RecyclerView>(R.id.search_recycler_view)
        //recyclerView.addOnChildAttachStateChangeListener(this@SearchActivity)


        val filterButton = findViewById<ImageButton>(R.id.search_filter_button)
        filterButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.view_pop_up, null)
            //val editText = dialogLayout.findViewById<EditText>(R.id.test_edit_view)

            val UpValue = dialogLayout.findViewById<TextView>(R.id.popup_high_interval)
            val LowValue = dialogLayout.findViewById<TextView>(R.id.popup_low_interval)
            val slider = dialogLayout.findViewById<RangeSlider>(R.id.popup_sliderRange)
            slider.addOnChangeListener(object : RangeSlider.OnChangeListener {
                override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
                    val values = slider.values
                    if (values.isNotEmpty()) {
                        val lowValue = (values[0]*10.0).roundToInt()/10.0
                        LowValue.text = lowValue.toString()

                        val highValue = (values[1]*10.0).roundToInt()/10.0
                        UpValue.text = highValue.toString()
                    }
                }
            })


            val UpYear = dialogLayout.findViewById<EditText>(R.id.popup_up_limit)
            val LowYear = dialogLayout.findViewById<EditText>(R.id.popup_down_limit)

            with(builder) {
                setPositiveButton("Valider") { dialog, which ->
                    //Log.d("EPF", "onCreate: ${editText.text}")
                    try {
                        yearUpLimit = UpYear.text.toString().toInt()
                        yearLowLimit = LowYear.text.toString().toInt()
                    }
                    catch (e : java.lang.Exception){
                    }
                    voteUpLimit = UpValue.text.toString().toDouble()
                    voteLowLimit = LowValue.text.toString().toDouble()

                    val filteredMovies = tri_list.filter { movie ->
                        movie.vote_average in voteLowLimit..voteUpLimit && movie.release_date.substring(0, 4).toInt() in yearLowLimit..yearUpLimit
                    }

                    val layoutManager = LinearLayoutManager(this@SearchActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    RecyclerViewSearch.layoutManager = layoutManager
                    RecyclerViewSearch.adapter = MovieAdapter(this@SearchActivity, filteredMovies, genres)
                    affichageNbFilms (TextNumber, filteredMovies.size)

                }
                setNegativeButton("Annuler") { dialog, which ->
                    Log.d("EPF", "Annuler")
                }
                setView(dialogLayout)
                show()
            }


        }
    }



    private fun Results (input: String): List<Movie> {
        val results: List<Movie>
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.themoviedb.org/3/")
            .build()

        val service = retrofit.create(TMBDSearch::class.java)
        val apiKey = "31e4672492df89a26175c865fed7271a"
        runBlocking {

            results = service.getMovies(input, apiKey).results.map {
                Movie (
                    it.id,
                    it.title,
                    it.release_date,
                    it.vote_average,
                    it.genre_ids,
                    it.poster_path,
                    it.backdrop_path,
                    it.popularity,
                    it.original_language
                )


            }
            //checkFav(movies_pop, movies)

        }
        Log.d("EPF", "${results}")
        return results





    }

    fun trierMoviesParVoteAverage(movies: List<Movie>): List<Movie> {
        return movies.sortedByDescending { it.vote_average }
    }


    fun trierMoviesParPopularity(movies: List<Movie>): List<Movie> {
        return movies.sortedByDescending { it.popularity }
    }

    fun trierMoviesParDate(movies: List<Movie>): List<Movie> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return movies.sortedByDescending { movie ->
            val date = dateFormat.parse(movie.release_date)
            date ?: Date(0) // Si la conversion de la date échoue, utilise une date par défaut
        }
    }


    fun affichageNbFilms (textView: TextView, taille: Int){
        if (taille ==0){
            textView.text = "Aucun résultats trouvé..."
        } else if (taille ==1){
            textView.text = "1 résultat trouvé"
        } else {
            textView.text = "$taille résultats trouvés"
        }
    }


}