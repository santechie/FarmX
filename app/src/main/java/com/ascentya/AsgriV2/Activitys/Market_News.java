package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ascentya.AsgriV2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class Market_News extends AppCompatActivity {
//    RecyclerView market_news;
//    private MarketNews_Adapter varietiesAdapter;
//    private List<MarketNews_Model> Data;

    LinearLayout back;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_news);
        webView = (WebView) findViewById(R.id.help_webview);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://agridoctor.net/");
//        market_news = findViewById(R.id.market_news);
//        Data = new ArrayList<>();
//        market_news.setLayoutManager(new LinearLayoutManager(Market_News.this));
//        market_news.setHasFixedSize(true);
//        getmarket_news();

    }


//    public void getmarket_news() {
//        AndroidNetworking.get("http://agridoctor.valartamilpublications.com/wp-json/wp/v2/posts?per_page=30")
//                .build().getAsJSONArray(new JSONArrayRequestListener() {
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//
//
//                try {
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        MarketNews_Model obj = new MarketNews_Model();
//
//
//                        obj.setDate(jsonArray.getJSONObject(i).optString("date"));
//                        obj.setName(StringEscapeUtils.unescapeJava(jsonArray.getJSONObject(i).getJSONObject("title").optString("rendered")));
//                        obj.setDescription(StringEscapeUtils.unescapeJava(jsonArray.getJSONObject(i).getJSONObject("content").optString("rendered")));
//                        Data.add(obj);
//                    }
//
//
//
//
//                    varietiesAdapter = new MarketNews_Adapter(Market_News.this, Data);
//
//                    market_news.setAdapter(varietiesAdapter);
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//
//            }
//        });
//    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
