package com.simple.movie.streaming.simplymovie.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.simple.movie.streaming.simplymovie.app.AppExecutors;
import com.simple.movie.streaming.simplymovie.models.MovieModel;
import com.simple.movie.streaming.simplymovie.response.MovieMultipleResponse;
import com.simple.movie.streaming.simplymovie.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    // LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies;

    private static MovieApiClient instance;

    // Making global runnable
    private RetrieveMovieRunnable retrieveMovieRunnable;

    // Live data for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;

    //making popular runnable
    private RetrieveMovieRunnablePop retrieveMovieRunnablePop;




    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    // 1- This method we are going to call through the classes
    public void searchMovieApi(String query, int pageNumber) {
        if (retrieveMovieRunnable != null) {
            retrieveMovieRunnable = null;
        }


        retrieveMovieRunnable = new RetrieveMovieRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().newtworkIO().submit(retrieveMovieRunnable);

        AppExecutors.getInstance().newtworkIO().schedule(new Runnable() {
            @Override
            //canceling retrofit call
            public void run() {
                myHandler.cancel(true);
            }
        }, 2000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviePop(int pageNumber) {
        if (retrieveMovieRunnablePop != null) {
            retrieveMovieRunnablePop = null;
        }


        retrieveMovieRunnablePop = new RetrieveMovieRunnablePop(pageNumber);

        final Future myHandlerPop = AppExecutors.getInstance().newtworkIO().submit(retrieveMovieRunnablePop);

        AppExecutors.getInstance().newtworkIO().schedule(new Runnable() {
            @Override
            //canceling retrofit call
            public void run() {
                myHandlerPop.cancel(true);
            }
        }, 2000, TimeUnit.MILLISECONDS);
    }

    // Retreiving data from RestAPI by runnable class
// We have 2 types of Queries: the id and the search query number
    private class RetrieveMovieRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMovieRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response objects

            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieMultipleResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: new for background thread
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }

                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }
            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieMultipleResponse> getMovies(String query, int pageNumber) {
            return Service.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }


        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMovieRunnablePop implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMovieRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting the response objects

            try {
                Response response = getPopularMovies(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }

                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieMultipleResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: new for background thread
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }

                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieMultipleResponse> getPopularMovies(int pageNumber) {
            return Service.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }


        private void cancelRequest() {
            Log.v("Tag", "Cancelling Popular Request");
            cancelRequest = true;
        }
    }

}
