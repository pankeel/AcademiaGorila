package com.academia.gorillas.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.academia.gorillas.R;
import com.academia.gorillas.model.Link;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class WebViewActivity extends AppCompatActivity {

    private Link link;

    private WebView webView;
    private CircularProgressIndicator progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        link = (Link) getIntent().getSerializableExtra("link");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(link.getName());

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_circular);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        webView.setWebViewClient(new WebViewActivity.CustomWebViewClient());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link.getLink());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

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
                    shareMessage = shareMessage + link.getLink();
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



    private class CustomWebViewClient extends WebViewClient {
        //Alttaki methodlar??n hepsini kullanmak zorunda deilsiniz
        //Hangisi i??inize yar??yorsa onu kullanabilirsiniz.
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) { //Sayfa y??klenirken ??al??????r
            super.onPageStarted(view, url, favicon);

            progressBar.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {//sayfam??z y??klendi??inde ??al??????yor.
            super.onPageFinished(view, url);

            progressBar.hide();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Bu method a????lan sayfa i??inden ba??ka linklere t??kland??????nda a????lmas??na yar??yor.
            //Bu methodu override etmez yada edip i??ini bo?? b??rak??rsan??z ilk url den a????lan sayfa d??????nda ba??ka sayfaya ge??i?? yapamaz

            view.loadUrl(url);//yeni t??klanan url i a????yor
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
            //BU method webview y??klenirken herhangi bir hatayla kar??ila??il??rsa hata kodu d??n??yor.
            //D??nen hata koduna g??re kullan??c??y?? bilgilendirebilir yada gerekli i??lemleri yapabilirsiniz
            //errorCode ile hatay?? alabilirsiniz
            //	if(errorCode==-8){
            //		Timeout
            //	} ??eklinde kullanabilirsiniz

            //Hata Kodlar?? a??a????dad??r...

	    	/*
	    	 *  /** Generic error
		    public static final int ERROR_UNKNOWN = -1;

		    /** Server or proxy hostname lookup failed
		    public static final int ERROR_HOST_LOOKUP = -2;

		    /** Unsupported authentication scheme (not basic or digest)
		    public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;

		    /** User authentication failed on server
		    public static final int ERROR_AUTHENTICATION = -4;

		    /** User authentication failed on proxy
		    public static final int ERROR_PROXY_AUTHENTICATION = -5;

		    /** Failed to connect to the server
		    public static final int ERROR_CONNECT = -6;

		    /** Failed to read or write to the server
		    public static final int ERROR_IO = -7;

		    /** Connection timed out
		    public static final int ERROR_TIMEOUT = -8;

		    /** Too many redirects
		    public static final int ERROR_REDIRECT_LOOP = -9;

		    /** Unsupported URI scheme
		    public static final int ERROR_UNSUPPORTED_SCHEME = -10;

		    /** Failed to perform SSL handshake
		    public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;

		    /** Malformed URL
		    public static final int ERROR_BAD_URL = -12;

		    /** Generic file error
		    public static final int ERROR_FILE = -13;

		    /** File not found
		    public static final int ERROR_FILE_NOT_FOUND = -14;

		    /** Too many requests during this load
		    public static final int ERROR_TOO_MANY_REQUESTS = -15;
	    	*/

        }
    }
}