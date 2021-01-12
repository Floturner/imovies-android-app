package floturner.dev.corp.imovie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.List;

import floturner.dev.corp.imovie.R;
import floturner.dev.corp.imovie.callbacks.OnMoviesClickCallback;
import floturner.dev.corp.imovie.models.Movie;
import floturner.dev.corp.imovie.server.APIClient;
import floturner.dev.corp.imovie.utils.Utils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> movies;
    private final OnMoviesClickCallback onMoviesClickCallback;

    public MovieAdapter(List<Movie> movies, OnMoviesClickCallback onMoviesClickCallback) {
        this.movies = movies;
        this.onMoviesClickCallback = onMoviesClickCallback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        try {
            movieViewHolder.bind(movies.get(i));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }

    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        Movie movie;

        TextView releaseDate, title, rating, overview;
        ImageView poster;

        MovieViewHolder(View itemView) {
            super(itemView);

            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            overview = itemView.findViewById(R.id.item_movie_overview);
            poster = itemView.findViewById(R.id.item_movie_poster);

            itemView.setOnClickListener(v -> onMoviesClickCallback.onClick(movie));
        }

        void bind(Movie movie) throws ParseException {
            this.movie = movie;
            releaseDate.setText(itemView.getResources().getString(R.string.release_date_text, Utils.formateDateString(movie.getReleaseDate())));
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            overview.setText(movie.getOverview());

            Glide.with(itemView)
                    .load(APIClient.IMAGE_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(poster);
        }
    }
}
