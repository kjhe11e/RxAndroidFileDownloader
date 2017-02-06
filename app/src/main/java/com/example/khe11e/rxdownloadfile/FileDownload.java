package com.example.khe11e.rxdownloadfile;

import android.util.Log;
import java.io.File;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class FileDownload {

    File destinationFile;
    String PACKAGE_NAME = MainActivity.PACKAGE_NAME;

    public void downloadImage(){
        RetrofitInterface downloadService = createService(RetrofitInterface.class, "https://assets.wired.com/photos/");
        downloadService.downloadFileByUrlRx("w_1032/wp-content/uploads/2016/01/nasa-risk-sts112-709-073k.jpg")
                .flatMap(processResponse())     // flatMap transforms items emitted by Observable into Observables, then flattens these into single Observable object.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())      // when response returns, the onNext, onComplete, and onError methods are called on the mainThread.
                .subscribe(handleResult());     // handleResult responds to the Observable's stream.
    }

    // Create Retrofit adapter
    public <T> T createService(Class<T> serviceClass, String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();     // Needed for returning an Observable
        return retrofit.create(serviceClass);
    }

    public Function<Response<ResponseBody>, Observable<File>> processResponse(){
        return new Function<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> apply(Response<ResponseBody> responseBodyResponse) throws Exception {
                return saveToDiskRx(responseBodyResponse);
            }
        };
    }

    private Observable<File> saveToDiskRx(final Response<ResponseBody> response){
        return Observable.create(new ObservableOnSubscribe<File>() {

            // ObservableOnSubscribe must implement subscribe(ObservableEmitter<File>).
            @Override
            public void subscribe(ObservableEmitter<File> subscriber) throws Exception {
                String filename = "RxImage1";
                new File("/data/data/" + PACKAGE_NAME + "/images").mkdir();
                destinationFile = new File("/data/data/" + PACKAGE_NAME + "/images/" + filename);

                BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                bufferedSink.writeAll(response.body().source());
                bufferedSink.close();

                subscriber.onNext(destinationFile);
                subscriber.onComplete();
            }
        });
    }

    private Observer<File> handleResult(){
        return new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("OnSubscribe", "Reached OnSubscribe step");
            }

            @Override
            // Called when the REST call receives data.
            public void onNext(File file) {
                Log.i("OnNext", "File downloaded to " + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("Error: ", "Error: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i("OnComplete", "Reached onComplete step");
                Log.i("FileSize (in bytes): ", Long.toString(destinationFile.length()));
            }
        };
    }
}
