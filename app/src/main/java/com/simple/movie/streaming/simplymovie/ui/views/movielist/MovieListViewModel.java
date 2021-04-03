package com.simple.movie.streaming.simplymovie.ui.views.movielist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    //ViewModel
    private MovieRepository mMovieRepository;

    // Constructor
    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return mMovieRepository.getPopularMovies();
    }


    // 3- Calling method in View-Model
    public void searchMovieApi(String query, int pageNumber){
        mMovieRepository.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        mMovieRepository.searchMoviePop(pageNumber);
    }

    public void searchNextpage(){
      mMovieRepository.searchNextPage();
    }

}
