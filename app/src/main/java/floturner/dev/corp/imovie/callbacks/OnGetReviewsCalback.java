package floturner.dev.corp.imovie.callbacks;

import java.util.List;

import floturner.dev.corp.imovie.models.Review;

public interface OnGetReviewsCalback {
    void onSuccess(List<Review> reviews);

    void onError();
}
