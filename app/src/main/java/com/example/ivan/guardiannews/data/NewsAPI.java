package com.example.ivan.guardiannews.data;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Ivan Lepojevic
 */

public interface NewsAPI {
    @GET("search?q=debates&api-key=test")
        // Using test API key as Guardian requires API key.
    Observable<NewsResponse> getNews();
}
