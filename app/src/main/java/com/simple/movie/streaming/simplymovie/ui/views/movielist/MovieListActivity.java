package com.simple.movie.streaming.simplymovie.ui.views.movielist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.simple.movie.streaming.simplymovie.R;
import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.requests.Service;
import com.simple.movie.streaming.simplymovie.response.MovieMultipleResponse;
import com.simple.movie.streaming.simplymovie.ui.shared.SnappingRecyclerView;
import com.simple.movie.streaming.simplymovie.ui.views.adapters.MovieRecyclerView;
import com.simple.movie.streaming.simplymovie.ui.views.adapters.OnMovieListener;
import com.simple.movie.streaming.simplymovie.ui.views.moviedetails.MovieDetailsActivity;
import com.simple.movie.streaming.simplymovie.ui.views.movielist.MovieListViewModel;
import com.simple.movie.streaming.simplymovie.utils.Credentials;
import com.simple.movie.streaming.simplymovie.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

  // RecyclerView
  private SnappingRecyclerView snappingRecyclerView;
  private MovieRecyclerView movieRecyclerViewAdapter;

  // ViewModel
  private MovieListViewModel movieListViewModel;

  boolean isPopular = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // SearchView
    SetupSearchView();

    snappingRecyclerView = findViewById(R.id.recyclerView);

    snappingRecyclerView.enableViewScaling(true);

    movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

    ConfigureRecyclerView();
    // Calling the observers
    ObserveAnyChange();

    ObservePopularMovies();

    //Getting popular movies
    movieListViewModel.searchMoviePop(1);
  }

    private void ObservePopularMovies() {
        movieListViewModel
                .getPopularMovies()
                .observe(
                        this,
                        new Observer<List<MovieModel>>() {
                            @Override
                            public void onChanged(List<MovieModel> movieModels) {
                                // Observe any data change
                                if (movieModels != null) {
                                    for (MovieModel movieModel : movieModels) {
                                        // Get the data in log
                                       // Log.v("Tagy", "onChange: " + movieModel.getTitle());

                                        movieRecyclerViewAdapter.setMovies(movieModels);
                                    }
                                }
                            }
                        });
    }

    // Get a data from searchView & query th api to ge the results (Movies)
  private void SetupSearchView() {
    final SearchView searchView = findViewById(R.id.search_view);
    searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            movieListViewModel.searchMovieApi(
                // Get search string from search view
                query, 1);
            return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            return false;
          }
        });

    searchView.setOnSearchClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            isPopular = false;
        }
    });
  }

  // Observables any data change

  private void ObserveAnyChange() {
    movieListViewModel
        .getMovies()
        .observe(
            this,
            new Observer<List<MovieModel>>() {
              @Override
              public void onChanged(List<MovieModel> movieModels) {
                // Observe any data change
                if (movieModels != null) {
                  for (MovieModel movieModel : movieModels) {
                    // Get the data in log
                    Log.v("Tagy", "onChange: " + movieModel.getTitle());
                      movieRecyclerViewAdapter.setMovies(movieModels);

                  }
                }
              }
            });
  }

  /*// 4- Calling method in Main Activity
  private void searchMovieApi(String query, int pageNumber){
      movieListViewModel.searchMovieApi(query, pageNumber);
  }*/

  // 5- Initializing recyclerview & adding data into it
  private void ConfigureRecyclerView() {
    // Live Data  can't be passed via constructor
    movieRecyclerViewAdapter = new MovieRecyclerView(this);

      snappingRecyclerView.setHasFixedSize(false);

      snappingRecyclerView.setAdapter(movieRecyclerViewAdapter);

      snappingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    //RecyclerView Pagination
      // Loading next page of api request
      snappingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if(!recyclerView.canScrollHorizontally(1)){
                // Here we need to display the next search results on the next page of api
                movieListViewModel.searchNextpage();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    });
  }

  private void GetRetrofitResponse() {
    MovieApi movieApi = Service.getMovieApi();

    Call<MovieMultipleResponse> responseCall =
        movieApi.searchMovie(Credentials.API_KEY, "Action", "1");

    responseCall.enqueue(
        new Callback<MovieMultipleResponse>() {
          @Override
          public void onResponse(
              Call<MovieMultipleResponse> call, Response<MovieMultipleResponse> response) {
            if (response.code() == 200) {
              Log.v("Tag", "The response" + response.body().toString());
              List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

              for (MovieModel movie : movies) {
                Log.v("Tag", "The release date" + movie.getRelease_date());
              }
            } else {
              try {
                Log.v("Tag", "Error" + response.errorBody().string());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<MovieMultipleResponse> call, Throwable t) {
            t.printStackTrace();
          }
        });
  }

  private void GetRetrofitResponseAccordingToID() {
    MovieApi movieApi = Service.getMovieApi();
    Call<MovieModel> responseCall = movieApi.getMovie(550, Credentials.API_KEY);

    responseCall.enqueue(
        new Callback<MovieModel>() {
          @Override
          public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
            if (response.code() == 200) {
              MovieModel movie = response.body();
              Log.v("Tag", "The response" + movie.getTitle());

            } else {
              try {
                Log.v("Tag", "Error" + response.errorBody().string());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }

          @Override
          public void onFailure(Call<MovieModel> call, Throwable t) {
            t.printStackTrace();
          }
        });
  }

  @Override
  public void onMovieClick(int position) {
    //  Toast.makeText(this, "The Position " + position, Toast.LENGTH_SHORT).show();

      // We don't need position of the movie in recyclerview
      // WE NEED THE ID OF THE MOVIE IN ORDER TO GET ALL IT'S DETAILS

      Intent intent = new Intent(this, MovieDetailsActivity.class);
      intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
      startActivity(intent);
  }

  @Override
  public void onCategoryClick(String category) {}
}
