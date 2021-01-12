package floturner.dev.corp.imovie.server;

import androidx.annotation.NonNull;

import java.text.ParseException;

import floturner.dev.corp.imovie.callbacks.OnGetMovieCallback;
import floturner.dev.corp.imovie.callbacks.OnGetMoviesByYearCallback;
import floturner.dev.corp.imovie.callbacks.OnGetReviewsCalback;
import floturner.dev.corp.imovie.callbacks.OnGetSearchResultsCallback;
import floturner.dev.corp.imovie.callbacks.OnGetTrailersCallback;
import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.models.MoviesResponse;
import floturner.dev.corp.imovie.models.ReviewResponse;
import floturner.dev.corp.imovie.models.TrailerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "fr-FR";
    private static final String API_KEY = "2355b1b47f82aa9755293fd5df901f19";
    public static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private static APIClient apiClient;
    private final APIService apiService;

    private APIClient(APIService apiService) {
        this.apiService = apiService;
    }

    public static APIClient getInstance() {
        if (apiClient == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiClient = new APIClient(retrofit.create(APIService.class));
        }

        return apiClient;
    }

    public void getSearchResults(int page, String search, final OnGetSearchResultsCallback onGetSearchResultsCallback) {
        apiService.searchMovie(API_KEY, LANGUAGE, search, page)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                onGetSearchResultsCallback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                onGetSearchResultsCallback.onError();
                            }
                        } else {
                            onGetSearchResultsCallback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        onGetSearchResultsCallback.onError();
                    }
                });
    }

    public void getMoviesByYear(int page, final OnGetMoviesByYearCallback onGetMoviesByYearCallback) {
        apiService.searchMoviesByYear(API_KEY, LANGUAGE, "popularity.desc", page, 2020)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            MoviesResponse moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getMovies() != null) {
                                onGetMoviesByYearCallback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                            } else {
                                onGetMoviesByYearCallback.onError();
                            }
                        } else {
                            onGetMoviesByYearCallback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                        onGetMoviesByYearCallback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetMovieCallback onGetMovieCallback) {
        apiService.getMovie(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                try {
                                    onGetMovieCallback.onSuccess(movie);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                onGetMovieCallback.onError();
                            }
                        } else {
                            onGetMovieCallback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                        onGetMovieCallback.onError();
                    }
                });
    }

    public void getTrailers(int movieId, final OnGetTrailersCallback onGetTrailersCallback) {
        apiService.getTrailers(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                onGetTrailersCallback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                onGetTrailersCallback.onError();
                            }
                        } else {
                            onGetTrailersCallback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                        onGetTrailersCallback.onError();
                    }
                });
    }

    public void getReviews(int movieId, final OnGetReviewsCalback onGetReviewsCalback) {
        apiService.getReviews(movieId, API_KEY, LANGUAGE)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null && reviewResponse.getReviews() != null) {
                                onGetReviewsCalback.onSuccess(reviewResponse.getReviews());
                            } else {
                                onGetReviewsCalback.onError();
                            }
                        } else {
                            onGetReviewsCalback.onError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {

                    }
                });
    }
}
