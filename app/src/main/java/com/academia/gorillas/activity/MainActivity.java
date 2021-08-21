package com.academia.gorillas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.academia.gorillas.Config;
import com.academia.gorillas.R;
import com.academia.gorillas.model.Link;
import com.academia.gorillas.model.Page;
import com.academia.gorillas.service.AppController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    private NavigationView navigationView;
    private List<Page> mPageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_tag, R.id.navigation_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);


        BottomNavigationView navView = findViewById(R.id.nav_view_tab);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_tag, R.id.navigation_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navControllerTab = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navControllerTab, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navControllerTab);

        getPages();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        Link link;
        for(Page page : mPageList){
            if (item.getItemId() == page.getId()){
                intent = new Intent(getApplicationContext(), WebViewActivity.class);
                // link = new Link(page.getTitle(),page.getLink());
                intent.putExtra("link", page);
                startActivity(intent);
            }
        }
        navigationView.setCheckedItem(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void getPages() {
        String url = Config.URL_PAGES ;

// Sağlanan URL’den bir dize yanıtı istenmektedir.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectPost = jsonArray.getJSONObject(i);

                                int id = jsonObjectPost.getInt("id");
                                String title = jsonObjectPost.getJSONObject("title").getString("rendered");
                                String excerpt = jsonObjectPost.getJSONObject("excerpt").getString("rendered");
                                String content = jsonObjectPost.getJSONObject("content").getString("rendered");
                                String date = jsonObjectPost.getString("date");
                                String link = jsonObjectPost.getString("link");
                                String featuredMedia = null;
                                if(!jsonObjectPost.getJSONObject("_embedded").isNull("wp:featuredmedia"))
                                {
                                    if(!jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).isNull("source_url")) {
                                        featuredMedia = jsonObjectPost.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url");
                                    }
                                }

                                Page page = new Page(id, title,excerpt,content,featuredMedia,date,link);

                                Menu menu = navigationView.getMenu(); // navView is NavigationView reference.
                                menu.add(0,page.getId(),i,page.getTitle());
                                mPageList.add(page);
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                // mTextView.setText("That didn't work!");
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }



}