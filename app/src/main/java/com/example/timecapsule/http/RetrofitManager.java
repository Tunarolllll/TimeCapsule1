package com.example.timecapsule.http;


import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressWarnings("FieldCanBeLocal")
public class RetrofitManager {


    public static final int READ_TIME_OUT = 15 * 1000;
    public static final int WRITE_TIME_OUT = 15 * 1000;
    public static final int CONNECT_TIME_OUT = 15 * 1000;

    private static Retrofit mRetrofit;
    private static HttpServices mApiService;
    private static OkHttpClient mClient;

    public static HttpServices getInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new AllowAllHostnameVerifier())
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getLoggerInterceptor());
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request;
                request = chain.request()
                        .newBuilder()
                        .build();
                return chain.proceed(request);
            }
        });
        mClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(HttpReq.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();
        mApiService = mRetrofit.create(HttpServices.class);

        return mApiService;
    }

    private static HttpLoggingInterceptor getLoggerInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.e("13","ApiUrl------>"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
