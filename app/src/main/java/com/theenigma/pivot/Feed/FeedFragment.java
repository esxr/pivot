package com.theenigma.pivot.Feed;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.theenigma.pivot.IOnBackPressed;
import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.R;

import static com.theenigma.pivot.MainActivity.isBack;

/**
 * A simple {@link Fragment} subclass.
 */

public class FeedFragment extends Fragment {

    View feedView;
    public static WebView webView;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        feedView =  inflater.inflate(R.layout.fragment_feed, container, false);
        webView = (WebView) feedView.findViewById(R.id.meduimWeb);

        isBack = true;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getAllowContentAccess();
        webView.getSettings().getAllowUniversalAccessFromFileURLs();

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                isBack = true;
                Log.i("msg", url);

               // if (url.startsWith("http") || url.startsWith("https")) {
                if(url.contains("enigma-icas")){

                    return true;
                }else if(url.equals("https://medium.com")||url.equals("http://medium.com") || url.contains("auth") || url.contains("oauth")) {

                    webView.stopLoading();
                }
                else{
                    webView.stopLoading();
                }
                return false;



            }});


        webView.loadUrl("https://medium.com/enigma-icas");

        return feedView;
    }


    }
