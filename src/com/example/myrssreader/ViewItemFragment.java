package com.example.myrssreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ViewItemFragment extends Fragment {
	private String title;
	private String link;
	private String pubDate;
	private String description;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.view_item_fragment, container, false);
        Bundle args = getArguments();
        title = args.getString("title");
        link = args.getString("link");
        pubDate = args.getString("pubDate");
        description = args.getString("description");
        //TODO: add nice appearance
        String html_text = "<h4>" + title + "</h4>" + description;
        WebView  rss_view = (WebView) rootView.findViewById(R.id.rss_view);
        rss_view.loadDataWithBaseURL(null, html_text, "text/html", "utf-8", null);
        return rootView;
    }
}