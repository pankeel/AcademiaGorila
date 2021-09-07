package com.academia.gorillas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.academia.gorillas.R;
import com.academia.gorillas.helper.DatabaseHelper;
import com.academia.gorillas.model.Post;
import com.academia.gorillas.util.Utils;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class PostDetailActivity extends AppCompatActivity  implements Html.ImageGetter {

    private ImageView imageView;
    private TextView title;
    private TextView date;
    private MaterialButton comment;
    private MaterialButton favorite;
    private WebView content;

    private Post post;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        post = (Post) getIntent().getSerializableExtra("post");

        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        comment = findViewById(R.id.comment);
        favorite = findViewById(R.id.favorite);
        content = findViewById(R.id.content);

        if(post.getFeaturedMedia() != null){
            Picasso.get().load(post.getFeaturedMedia()).into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
        }


        title.setText(Html.fromHtml(post.getTitle()));
        if(Utils.checkDatePattern("yyyy-MM-dd'T'HH:mm:ss", post.getDate())){
            date.setText(Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy",post.getDate()));
        }else if(Utils.checkDatePattern("yyyy-MM-dd HH:mm:ss", post.getDate())){
            date.setText(Utils.formatDate("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy",post.getDate()));
        }else{
            date.setText(post.getDate());
        }
        //content.setText(Html.fromHtml(post.getContent()));

        Spanned spanned = Html.fromHtml(post.getContent(), this, null);
        content.getSettings().setBuiltInZoomControls(true);
        content.getSettings().setUseWideViewPort(true);
        content.getSettings().setLoadWithOverviewMode(true);
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setDefaultFontSize(50);
        content.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        content.setScrollContainer(false);
        System.out.println( post.getContent());
        content.loadDataWithBaseURL(null, "<style>img{display: inline;width:100%;height: auto;max-width: 100%;}iframe{display: block;width:100%;max-width:100%;}</style>"  + post.getContent(), "text/html", "utf-8", null);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(db.isIncludeFavorite(post.getId())){
            favorite.setText(R.string.remove_to_favorites);
            favorite.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_favorite_24));
        }else{
            favorite.setText(R.string.add_to_favorites);
            favorite.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_favorite_border_24));
        }

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this,CommentsActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                if(!db.isIncludeFavorite(post.getId())){
                    favorite.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_favorite_24));
                    favorite.setText(R.string.remove_to_favorites);
                    db.addFavorite(post);
                }else{
                    favorite.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_favorite_border_24));
                    favorite.setText(R.string.add_to_favorites);
                    db.deleteFavorite(post.getId());
                }
            }
        });


//        mInterstitialAd = new InterstitialAd(getApplicationContext());
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_id_1));
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                mInterstitialAd.show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            case R.id.action_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String shareMessage = getString(R.string.share_message);
                    shareMessage = shareMessage + post.getLink();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.logo);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }


    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("TAG", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("TAG", "onPostExecute drawable " + mDrawable);
            Log.d("TAG", "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                //CharSequence t = content.getText();
               // content.setText(t);
            }
        }
    }

}