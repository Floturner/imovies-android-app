package floturner.dev.corp.imovie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import floturner.dev.corp.imovie.R;
import floturner.dev.corp.imovie.callbacks.OnGetMovieCallback;
import floturner.dev.corp.imovie.callbacks.OnGetReviewsCalback;
import floturner.dev.corp.imovie.callbacks.OnGetTrailersCallback;
import floturner.dev.corp.imovie.models.Company;
import floturner.dev.corp.imovie.models.Genre;
import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.models.Review;
import floturner.dev.corp.imovie.models.Trailer;
import floturner.dev.corp.imovie.server.APIClient;
import floturner.dev.corp.imovie.utils.Utils;

public class MovieDetailsActivity extends AppCompatActivity {
    private APIClient apiClient;
    private int movieId;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%s";
    private static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg";
    public static String MOVIE_ID = "movie_id";

    CoordinatorLayout movieDetailsRootView;
    Toolbar toolbar;
    ImageView movieBackdrop;
    TextView movieTitle, movieNote, movieVoteCount, movieBudget, movieGenres, movieCompanies, movieOverview, movieOverviewLabel, movieReleaseDate, trailersLabel, reviewsLabel;
    RatingBar movieRating;
    LinearLayout movieTrailers, movieReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        apiClient = APIClient.getInstance();

        movieDetailsRootView = findViewById(R.id.movieDetailsRootView);
        toolbar = findViewById(R.id.toolbar);
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieNote = findViewById(R.id.movieDetailsNote);
        movieVoteCount = findViewById(R.id.movieDetailsVoteCount);
        movieBudget = findViewById(R.id.movieDetailsBudget);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieCompanies = findViewById(R.id.movieDetailsCompanies);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        trailersLabel = findViewById(R.id.trailersLabel);
        reviewsLabel = findViewById(R.id.reviewsLabel);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);

        setupToolbar();

        getMovie();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        return true;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void getMovie() {
        apiClient.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) throws ParseException {
                // Titre
                movieTitle.setText(movie.getTitle());

                // Barre de notes
                movieRating.setRating(movie.getRating() / 2);

                // Date
                movieReleaseDate.setText(getResources().getString(R.string.release_date_text, Utils.formateDateString(movie.getReleaseDate())));

                // Note
                movieNote.setText(getResources().getString(R.string.note, movie.getRating()));

                // Nombre de votes
                movieVoteCount.setText(getResources().getString(R.string.num_votes, movie.getVoteCount()));

                // Budget
                movieBudget.setText(getResources().getString(R.string.budget, NumberFormat.getNumberInstance(Locale.US).format(movie.getBudget())));

                // Genres
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setVisibility(View.VISIBLE);
                    movieGenres.setText(getResources().getString(R.string.genres, TextUtils.join(" / ", currentGenres)));
                }

                // Compagnies
                if (movie.getCompanies() != null) {
                    List<String> currentCompanies = new ArrayList<>();
                    for (Company company : movie.getCompanies()) {
                        currentCompanies.add(company.getName());
                    }
                    movieCompanies.setVisibility(View.VISIBLE);
                    movieCompanies.setText(getResources().getString(R.string.companies, TextUtils.join(" / ", currentCompanies)));
                }

                // Résumé
                if (movie.getOverview() != null && !movie.getOverview().isEmpty()) {
                    movieOverviewLabel.setVisibility(View.VISIBLE);
                    movieOverview.setText(movie.getOverview());
                }

                // Image
                if (!isFinishing()) {
                    Glide.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_error)
                            .into(movieBackdrop);
                }

                // Bandes-annonces
                getTrailers(movie);

                // Commentaires
                getReviews(movie);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    private void getTrailers(final Movie movie) {
        apiClient.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                if (trailers != null && !trailers.isEmpty()) {
                    trailersLabel.setVisibility(View.VISIBLE);
                    movieTrailers.removeAllViews();
                    for (final Trailer trailer : trailers) {
                        final View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                        final ImageView thumbnail = parent.findViewById(R.id.thumbnail);

                        thumbnail.requestLayout();
                        thumbnail.setOnClickListener(v -> showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey())));

                        Glide.with(MovieDetailsActivity.this)
                                .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                                .placeholder(R.drawable.ic_placeholder)
                                .error(R.drawable.ic_error)
                                .into(thumbnail);
                        movieTrailers.addView(parent);
                    }
                }
            }

            @Override
            public void onError() {
                trailersLabel.setVisibility(View.GONE);
                showError();
            }
        });
    }

    private void getReviews(Movie movie) {
        apiClient.getReviews(movie.getId(), new OnGetReviewsCalback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                if (reviews != null && !reviews.isEmpty()) {
                    reviewsLabel.setVisibility(View.VISIBLE);
                    movieReviews.removeAllViews();
                    for (Review review : reviews) {
                        View parent = getLayoutInflater().inflate(R.layout.review, movieReviews, false);
                        TextView author = parent.findViewById(R.id.reviewAuthor);
                        TextView content = parent.findViewById(R.id.reviewContent);
                        author.setTextColor(getResources().getColor(R.color.black));
                        author.setAlpha(0.7f);
                        content.setTextColor(getResources().getColor(R.color.black));
                        content.setAlpha(0.7f);

                        author.setText(review.getAuthor());
                        content.setText(review.getContent());

                        movieReviews.addView(parent);
                    }
                }
            }

            @Override
            public void onError() {
                reviewsLabel.setVisibility(View.GONE);
                showError();
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void showError() {
        Snackbar.make(movieDetailsRootView, R.string.network_troubleshoot, Snackbar.LENGTH_SHORT).show();
    }
}