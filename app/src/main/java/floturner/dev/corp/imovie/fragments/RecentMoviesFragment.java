package floturner.dev.corp.imovie.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import floturner.dev.corp.imovie.R;
import floturner.dev.corp.imovie.activities.MovieDetailsActivity;
import floturner.dev.corp.imovie.adapter.MovieAdapter;
import floturner.dev.corp.imovie.callbacks.OnGetMoviesByYearCallback;
import floturner.dev.corp.imovie.callbacks.OnMoviesClickCallback;
import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.server.APIClient;

public class RecentMoviesFragment extends Fragment {
    Activity context;

    private MovieAdapter movieAdapter;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private APIClient apiClient;

    SwipeRefreshLayout recentSwipeRefreshLayout;
    RecyclerView moviesRecyclerView;

    public static RecentMoviesFragment newInstance() {
        return new RecentMoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
        if (context != null) {
            context.setTitle(R.string.nav_recent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent_movies, container, false);

        recentSwipeRefreshLayout = v.findViewById(R.id.recent_swipe_refresh);
        moviesRecyclerView = v.findViewById(R.id.recent_movies_recycler_view);

        apiClient = APIClient.getInstance();

        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        setupOnScrollListener();

        recentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageResults();
            }
        });

        recentSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.pink_400));

        getFirstPageResults();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            getFirstPageResults();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getFirstPageResults() {
        currentPage = 1;
        getMoviesByYear(currentPage);
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        moviesRecyclerView.setLayoutManager(linearLayoutManager);
        moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = linearLayoutManager.getItemCount();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMoviesByYear(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getMoviesByYear(int page) {
        isFetchingMovies = true;
        recentSwipeRefreshLayout.setRefreshing(true);
        apiClient.getMoviesByYear(page, new OnGetMoviesByYearCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movies, onMoviesClickCallback);
                    moviesRecyclerView.setAdapter(movieAdapter);
                } else {
                    if (page == 1) {
                        movieAdapter.clearMovies();
                    }
                    movieAdapter.appendMovies(movies);
                }

                currentPage = page;
                isFetchingMovies = false;
                recentSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError() {
                showError();
                recentSwipeRefreshLayout.setRefreshing(false);
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
        Snackbar.make(recentSwipeRefreshLayout, R.string.network_troubleshoot, Snackbar.LENGTH_SHORT).show();
    }
}