package com.example.flixsterplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixstar.R
import okhttp3.Headers
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var rvMovies: RecyclerView
    private val movies = mutableListOf<Movie>()
    private lateinit var adapter: MovieAdapter

    companion object {
        private const val TAG = "MainActivity"
        private const val NOW_PLAYING_URL =
            "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.rvMovies)
        adapter = MovieAdapter(movies)
        rvMovies.adapter = adapter
        rvMovies.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    private fun fetchMovies() {
        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                try {
                    val results = json?.jsonObject?.getJSONArray("results")
                    if (results != null) {
                        for (i in 0 until results.length()) {
                            val movieJson = results.getJSONObject(i)
                            val movie = Movie(
                                title = movieJson.getString("title"),
                                overview = movieJson.getString("overview"),
                                posterPath = movieJson.optString("poster_path", "")
                            )
                            movies.add(movie)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON parsing error", e)
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "Failed to fetch movies: $statusCode", throwable)
            }
        })
    }
}
