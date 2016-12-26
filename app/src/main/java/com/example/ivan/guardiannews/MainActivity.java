package com.example.ivan.guardiannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ivan.guardiannews.data.NewsAPI;
import com.example.ivan.guardiannews.data.NewsResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView listView;
    NewsAdapter mAdapter;
    Retrofit retrofit;
    NewsAPI api;
    String url = "http://content.guardianapis.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create a Retrofit adapter
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // RxJava Factory
                .addConverterFactory(GsonConverterFactory.create())         // We are using Gson
                .build();

        // Initiate an object of NewsAPI interface.
        api = retrofit.create(NewsAPI.class);

        // Create adapter to convert the array to views
        mAdapter = new NewsAdapter(this, new ArrayList<>());
        // Attach adapter to ListView
        listView.setAdapter(mAdapter);

        // Create an Observable.
        Observable<NewsResponse> call = api.getNews();

        // Subscribe on our Observable that makes our method call.
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())      // From RxAndroid library
                .subscribe(new Subscriber<NewsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsResponse newsResponse) {
                        mAdapter.addAll(newsResponse.getResponse().getResults());
                    }
                });
    }
}
