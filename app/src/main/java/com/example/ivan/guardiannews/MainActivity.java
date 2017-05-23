package com.example.ivan.guardiannews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ivan.guardiannews.data.NewsAPI;
import com.example.ivan.guardiannews.data.NewsResponse;
import com.example.ivan.guardiannews.data.NewsStory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView listView;
    NewsAdapter mAdapter;
    Retrofit retrofit;
    NewsAPI api;
    String url = "http://content.guardianapis.com/";
    Disposable disposable;
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
        Observable<NewsResponse> call = api.getNews();

        // Subscribe on our Observable that makes our method call.
        disposable = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())      // From RxAndroid library
                .subscribeWith(new DisposableObserver<NewsResponse>(){
                    @Override
                    public void onNext(NewsResponse newsResponse) {
                        mAdapter.addAll(newsResponse.getResponse().getResults());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
