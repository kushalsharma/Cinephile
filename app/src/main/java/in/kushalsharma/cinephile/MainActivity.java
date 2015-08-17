package in.kushalsharma.cinephile;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;

import java.util.ArrayList;
import java.util.List;

import in.kushalsharma.adapters.MovieTitleListAdapter;
import in.kushalsharma.fragments.FavouriteListFragment;
import in.kushalsharma.fragments.HighestRatedListFragment;
import in.kushalsharma.fragments.MovieDetailActivityFragment;
import in.kushalsharma.fragments.PopularListFragment;


public class MainActivity extends AppCompatActivity implements MovieTitleListAdapter.OnAdapterItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private SearchBox search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (SearchBox) findViewById(R.id.search_box);
        search.enableVoiceRecognition(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new PopularListFragment(), getString(R.string.most_popular));
        adapter.addFragment(new HighestRatedListFragment(), getString(R.string.highest_rated));
        adapter.addFragment(new FavouriteListFragment(), getString(R.string.favourites));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                openSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if (search.getSearchText().isEmpty()) toolbar.setTitle("");
    }

    public void openSearch() {
        search.setLogoText("");
        search.revealFromMenuItem(R.id.action_search, this);

        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
                toolbar.setTitle(getResources().getString(R.string.app_name));
            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(MainActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSearchCleared() {

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(String id) {
        MovieDetailActivityFragment displayFrag = (MovieDetailActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (displayFrag == null) {
            // DisplayFragment (Fragment B) is not in the layout (handset layout),
            // so start DisplayActivity (Activity B)
            // and pass it the info about the selected item
            Intent mMovieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
            mMovieDetailIntent.putExtra("id", id);
            startActivity(mMovieDetailIntent);
        } else {
            // DisplayFragment (Fragment B) is in the layout (tablet layout),
            // so tell the fragment to update
            displayFrag.updateContent(id);
        }

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
