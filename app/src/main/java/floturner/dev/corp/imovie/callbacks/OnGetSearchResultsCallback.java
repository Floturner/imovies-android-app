package floturner.dev.corp.imovie.callbacks;

import java.util.List;

import floturner.dev.corp.imovie.models.Movie;

public interface OnGetSearchResultsCallback {
    void onSuccess(int page, List<Movie> movies);

    void onError();
}
