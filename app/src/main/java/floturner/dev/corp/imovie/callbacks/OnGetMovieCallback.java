package floturner.dev.corp.imovie.callbacks;

import java.text.ParseException;

import floturner.dev.corp.imovie.models.Movie;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie) throws ParseException;

    void onError();
}
