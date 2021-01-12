package floturner.dev.corp.imovie.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import floturner.dev.corp.imovie.R;
import floturner.dev.corp.imovie.activities.MovieDetailsActivity;
import floturner.dev.corp.imovie.adapter.MovieAdapter;
import floturner.dev.corp.imovie.callbacks.OnGetSearchResultsCallback;
import floturner.dev.corp.imovie.callbacks.OnMoviesClickCallback;
import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.server.APIClient;

public class SearchMoviesFragment extends Fragment {
    Activity context;

    private MovieAdapter movieAdapter;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private APIClient apiClient;
    String searchText;

    ConstraintLayout searchRootView;
    EditText searchEditText;
    ImageButton searchButton;
    RecyclerView searchRecyclerView;

    public static SearchMoviesFragment newInstance() {
        return new SearchMoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
        if (context != null) {
            context.setTitle(R.string.nav_search);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_movies, container, false);

        searchRootView = v.findViewById(R.id.search_root_view);
        searchEditText = v.findViewById(R.id.search_text);
        searchButton = v.findViewById(R.id.search_btn);
        searchRecyclerView = v.findViewById(R.id.search_movies_list);

        apiClient = APIClient.getInstance();

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        setupOnScrollListener();

        searchButton.setOnClickListener(v1 -> {
            searchText = searchEditText.getText().toString().trim();
            if (!searchText.isEmpty()) {
                currentPage = 1;
                getSearchResults(currentPage, searchText);
            } else {
                Snackbar.make(searchRootView, R.string.empty_movie_title_control, Snackbar.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getSearchResults(currentPage + 1, searchText);
                    }
                }
            }
        });
    }

    private void getSearchResults(int page, String movieTitle) {
        isFetchingMovies = true;
        apiClient.getSearchResults(page, movieTitle, new OnGetSearchResultsCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movies, onMoviesClickCallback);
                    searchRecyclerView.setAdapter(movieAdapter);
                } else {
                    if (page == 1) {
                        movieAdapter.clearMovies();
                    }
                    movieAdapter.appendMovies(movies);
                }

                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback onMoviesClickCallback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    };

    private void showError() {
        Snackbar.make(searchRootView, R.string.network_troubleshoot, Snackbar.LENGTH_SHORT).show();
    }
}