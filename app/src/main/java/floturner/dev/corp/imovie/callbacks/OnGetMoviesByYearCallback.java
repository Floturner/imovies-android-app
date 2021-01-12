package floturner.dev.corp.imovie.callbacks;

import java.util.List;

import floturner.dev.corp.imovie.models.Movie;

public interface OnGetMoviesByYearCallback {
    void onSuccess(int page, List<Movie> movies);

    void onError();
}
