package floturner.dev.corp.imovie.server;

import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.models.MoviesResponse;
import floturner.dev.corp.imovie.models.ReviewResponse;
import floturner.dev.corp.imovie.models.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("search/movie")
    Call<MoviesResponse> searchMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("discover/movie")
    Call<MoviesResponse> searchMoviesByYear(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("page") int page,
            @Query("primary_release_year") int year
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}
