package com.simple.movie.streaming.simplymovie.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.requests.MovieApiClient;

import java.util.List;

public class MovieRepository {

    // This class is acting as repository
    // This class is used for ViewModel

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;

    private int mPageNumber;

    public static MovieRepository getInstance(){
    if(instance == null){
        instance = new MovieRepository();
    }
    return instance;
    }

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();

    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieApiClient.getMoviesPop();
    }

    // 2 - Calling the method in the Repository
    public void searchMovieApi(String query, int pageNumber){
        movieApiClient.searchMovieApi(query, pageNumber);

        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber){

        mPageNumber = pageNumber;
        movieApiClient.searchMoviePop(pageNumber);
    }

    public void searchNextPage(){
        searchMovieApi(mQuery, mPageNumber+1);
    }
}
