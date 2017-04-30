package es.uma.goingonapp.news_list.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.activities.SingleFragmentActivity;
import es.uma.goingonapp.common.entities.News;
import es.uma.goingonapp.common.entities.NewsThumbnail;
import es.uma.goingonapp.common.entities.User;
import es.uma.goingonapp.common.network.NewsProxy;
import es.uma.goingonapp.log_in.activities.LogInActivity;
import es.uma.goingonapp.news_list.adapter.NewsListAdapter;
import es.uma.goingonapp.news_list.adapter.NewsListNavigationDrawerAdapter;
import es.uma.goingonapp.news_list.holders.NewsListNavigationDrawerItem;
import es.uma.goingonapp.post_news.activities.PostNewsActivity;
import es.uma.goingonapp.register.activities.RegisterActivity;

/**
 * This Fragment will display the list of HotNews using a RecyclerView
 */
public class NewsListFragment extends Fragment implements Response.Listener<News[]>, Response.ErrorListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mNavigationDrawerView;
    private TypedArray mNavigationDrawerIcons;
    private String[] mNavigationDrawerTitles;
    private ArrayList<NewsListNavigationDrawerItem> mNavigationDrawerItems;
    private NewsListNavigationDrawerAdapter mNavigationDrawerAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private User mLoggedInUser;

    // TODO: think about how to structure the application state
    private boolean isUserLoggedIn = false;

    private NewsProxy mNewsProxy;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inflate the view
		View view = inflater.inflate(R.layout.fragment_list_news, container, false);

        this.mRecyclerView = (RecyclerView)view.findViewById(R.id.hot_news_list);
        this.mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        this.setUpRecyclerView();
        this.setUpMenu();
        this.setUpSwipeRefreshLayout(view);

        this.setUpNavigationDrawer(inflater, view, this.isUserLoggedIn);
        this.setUpActionBarDrawerToggle(view);

        this.mNewsProxy = new NewsProxy(this.getActivity());
        this.mNewsProxy.getHotNews(this, this);

        // TODO: move somewhere else
        ((SingleFragmentActivity)this.getActivity()).getSupportActionBar().setTitle("GoingOnApp - HotNews");

		return view;
	}

    @Override
    public void onStop() {
        super.onStop();

        this.mNewsProxy.cancelRequests();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_actionbar_hot_news, menu);
        menu.getItem(0).setVisible(this.isUserLoggedIn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mActionBarDrawerToggle.isDrawerIndicatorEnabled() &&
                this.mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_item_post_news:
                Intent intent = new Intent(this.getActivity(), PostNewsActivity.class);
                this.startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LogInActivity.LOG_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                User user = (User) data.getSerializableExtra(LogInActivity.USER_EXTRA);
                String toastMessage = "The user " + user.getmNickname()  + " has logged in successfully";
                Toast.makeText(this.getActivity(), toastMessage, Toast.LENGTH_LONG).show();

                // TODO: do something with the user data
                // TODO: POST news will need it

                this.mLoggedInUser = user;

                this.isUserLoggedIn = true;
                refreshNavigationDrawer();

                ((SingleFragmentActivity)this.getActivity()).getSupportActionBar().setSubtitle(user.getmNickname() + " is online");
            }
        } else if (requestCode == RegisterActivity.REGISTER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                User user = (User) data.getSerializableExtra(RegisterActivity.USER_EXTRA);
                String toastMessage = "The user " + user.getmNickname()  + " has been created in successfully";
                Toast.makeText(this.getActivity(), toastMessage, Toast.LENGTH_LONG).show();

                this.mLoggedInUser = user;

                // TODO: do something with the user data
                // TODO: POST news will need it

                this.isUserLoggedIn = true;
                refreshNavigationDrawer();
            }
        }
    }

    @Override
    public void onResponse(News[] response) {
        this.finishRefreshAnimation();

        for (News news : response) {
            NewsThumbnail newsThumbnail = new NewsThumbnail(news.getTitle(), news.getContent(), news.getSelfLink().getHref().toString());

            if (!this.mNewsListAdapter.containsItem(newsThumbnail)) {
                this.mNewsListAdapter.addItem(0, newsThumbnail);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("DEBUG", "There was an error during the request: " + error.getMessage());

        this.finishRefreshAnimation();

        Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        if (this.mNewsProxy == null) {
            this.mNewsProxy = new NewsProxy(this.getActivity());
        }

        this.mNewsProxy.getHotNews(this, this);
    }

    private void finishRefreshAnimation() {
        if (this.mSwipeRefreshLayout == null) {
            this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.getView().findViewById(R.id.swipe_refresh_layout);
        }

        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    // region UI SetUp

    private void setUpRecyclerView() {
        if (this.mRecyclerView != null) {
            //Improves performance when the size is fixed
            this.mRecyclerView.setHasFixedSize(true);

            // Use a linear layout manager since the layout is going to be a vertical list.
            // It will manage the behaviour of the list.
            this.mLayoutManager = new LinearLayoutManager(this.getActivity());
            this.mRecyclerView.setLayoutManager(this.mLayoutManager);

            // Specify an adapter. It will handle cards display and its interaction with the user.
            this.mNewsListAdapter = new NewsListAdapter();
            this.mRecyclerView.setAdapter(this.mNewsListAdapter);
        }
    }

    private void setUpMenu() {
        this.setHasOptionsMenu(true);
    }

    private void setUpSwipeRefreshLayout(View view) {
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpNavigationDrawer(LayoutInflater inflater, View view, boolean userIsLoggedIn) {
        this.mNavigationDrawerView = (ListView) view.findViewById(R.id.drawer_view);
        View navigationDrawerHeader = inflater.inflate(R.layout.navigation_drawer_header, null);
        this.mNavigationDrawerView.addHeaderView(navigationDrawerHeader);

        this.mNavigationDrawerIcons = this.getResources().obtainTypedArray(R.array.navigation_drawer_icons);
        this.mNavigationDrawerTitles = this.getResources().getStringArray(R.array.navigation_drawer_options);

        this.refreshNavigationDrawer();

        this.mNavigationDrawerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity context;
                Intent intent;

                switch (position) {
                    case 1:
                        context = getActivity();
                        intent = new Intent(context, RegisterActivity.class);

                        startActivityForResult(intent, RegisterActivity.REGISTER_REQUEST);
                        break;
                    case 2:
                        if (!isUserLoggedIn) {
                            context = getActivity();
                            intent = new Intent(context, LogInActivity.class);

                            startActivityForResult(intent, LogInActivity.LOG_IN_REQUEST);
                        } else {
                            isUserLoggedIn = false;
                            mLoggedInUser = null;
                            refreshNavigationDrawer();
                            ((SingleFragmentActivity)getActivity()).getSupportActionBar().setSubtitle("");
                        }

                        break;
                }
            }
        });
    }

    private void refreshNavigationDrawer() {
        this.mNavigationDrawerItems = new ArrayList<>();

        this.mNavigationDrawerItems.add(new NewsListNavigationDrawerItem(this.mNavigationDrawerTitles[0], this.mNavigationDrawerIcons.getResourceId(0, -1)));

        if (this.isUserLoggedIn) {
            this.mNavigationDrawerItems.add(new NewsListNavigationDrawerItem(this.mNavigationDrawerTitles[2], this.mNavigationDrawerIcons.getResourceId(2, -1)));
        } else {
            this.mNavigationDrawerItems.add(new NewsListNavigationDrawerItem(this.mNavigationDrawerTitles[1], this.mNavigationDrawerIcons.getResourceId(1, -1)));
        }

        this.mNavigationDrawerItems.add(new NewsListNavigationDrawerItem(this.mNavigationDrawerTitles[3], this.mNavigationDrawerIcons.getResourceId(3, -1)));
        this.mNavigationDrawerItems.add(new NewsListNavigationDrawerItem(this.mNavigationDrawerTitles[4], this.mNavigationDrawerIcons.getResourceId(4, -1)));

        this.mNavigationDrawerAdapter = new NewsListNavigationDrawerAdapter(this.getActivity(), this.mNavigationDrawerItems);

        this.mNavigationDrawerView.setAdapter(mNavigationDrawerAdapter);

        getActivity().invalidateOptionsMenu();

        this.mDrawerLayout.closeDrawers();
    }

    private void setUpActionBarDrawerToggle(View view) {
        if (this.mDrawerLayout == null) {
            this.mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        }

        this.mActionBarDrawerToggle = new ActionBarDrawerToggle(this.getActivity(), this.mDrawerLayout, R.mipmap.ic_launcher, R.string.app_name, R.string.hello_world);

        this.mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        this.mDrawerLayout.setDrawerListener(this.mActionBarDrawerToggle);
    }

    // endregion
}
