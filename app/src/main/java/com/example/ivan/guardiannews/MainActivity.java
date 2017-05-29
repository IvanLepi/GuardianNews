package com.example.ivan.guardiannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.ivan.guardiannews.data.NewsAPI;
import com.example.ivan.guardiannews.data.NewsResponse;
import com.example.ivan.guardiannews.data.NewsStory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   // RxJava Factory
                .addConverterFactory(GsonConverterFactory.create())         // We are using Gson
                .build();

        // Initiate an object of NewsAPI interface.
        api = retrofit.create(NewsAPI.class);

        // Create adapter to convert the array to views
        mAdapter = new NewsAdapter(this, new ArrayList<NewsStory>());
        // Attach adapter to ListView
        listView.setAdapter(mAdapter);

        // Create an Observable.
        Single<NewsResponse> call = api.getNews();

        // Subscribe on our Observable that makes our method call.
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())      // From RxAndroid library
                .subscribe(new SingleObserver<NewsResponse>(){
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(LOG_TAG,"onSubscribe " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(@NonNull NewsResponse newsResponse) {
                        mAdapter.addAll(newsResponse.getResponse().getResults());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(LOG_TAG,"onError: "+ e.getMessage());
                    }
                });
    }
}
