package floturner.dev.corp.imovie.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import floturner.dev.corp.imovie.R;
import floturner.dev.corp.imovie.fragments.FavoriteMoviesFragment;
import floturner.dev.corp.imovie.fragments.RecentMoviesFragment;
import floturner.dev.corp.imovie.fragments.SearchMoviesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private SearchMoviesFragment searchMoviesFragment;
    private FavoriteMoviesFragment favoriteMoviesFragment;
    private RecentMoviesFragment recentMoviesFragment;

    private static final int SEARCH_MOVIES_FRAGMENT = 0;
    private static final int FAVORITE_MOVIES_FRAGMENT = 1;
    private static final int RECENT_MOVIES_FRAGMENT = 2;
    private static final int SHARE_APP_FRAGMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = findViewById(R.id.drawer_layout);

        this.navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();

        this.navigationView.setNavigationItemSelectedListener(this);

        showFirstFragment();
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!searchMoviesFragment.isVisible()) {
                this.navigationView.setCheckedItem(R.id.nav_search);
                this.showFragment(SEARCH_MOVIES_FRAGMENT);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                this.showFragment(SEARCH_MOVIES_FRAGMENT);
                break;

            case R.id.nav_favorite:
                this.showFragment(FAVORITE_MOVIES_FRAGMENT);
                break;

            case R.id.nav_recent:
                this.showFragment(RECENT_MOVIES_FRAGMENT);
                break;

            case R.id.nav_share:
                this.showFragment(SHARE_APP_FRAGMENT);
                break;
        }

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (visibleFragment == null)
            this.showFragment(SEARCH_MOVIES_FRAGMENT);
    }

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case SEARCH_MOVIES_FRAGMENT:
                if (this.searchMoviesFragment == null)
                    this.searchMoviesFragment = SearchMoviesFragment.newInstance();
                this.startTransactionFragment(this.searchMoviesFragment);
                break;

            case FAVORITE_MOVIES_FRAGMENT:
                if (this.favoriteMoviesFragment == null)
                    this.favoriteMoviesFragment = FavoriteMoviesFragment.newInstance();
                this.startTransactionFragment(this.favoriteMoviesFragment);
                break;

            case RECENT_MOVIES_FRAGMENT:
                if (this.recentMoviesFragment == null)
                    this.recentMoviesFragment = RecentMoviesFragment.newInstance();
                this.startTransactionFragment(this.recentMoviesFragment);
                break;

            case SHARE_APP_FRAGMENT:
                Toast.makeText(this, "SHARE APP CLIQUE", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}