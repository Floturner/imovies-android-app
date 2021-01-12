package floturner.dev.corp.imovie.callbacks;

import java.util.List;

import floturner.dev.corp.imovie.models.Trailer;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}
