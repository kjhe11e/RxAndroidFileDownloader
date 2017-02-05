package com.example.khe11e.rxdownloadfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    Button downloadImgBtn;
    File destinationFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadImgBtn = (Button) findViewById(R.id.downloadImgBtn);
        downloadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });
    }

    public void downloadImage(){
        RetrofitInterface downloadService = createService(RetrofitInterface.class, "https://assets.wired.com/photos/");
        downloadService.downloadFileByUrlRx("w_1032/wp-content/uploads/2016/01/nasa-risk-sts112-709-073k.jpg")
                .flatMap(processResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResult());
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
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
            @Override
            public void subscribe(ObservableEmitter<File> subscriber) throws Exception {
                String header = response.headers().get("Content-Disposition");
                //String filename = header.replace("attachment; filename=", "test");
                String filename = "RxImage1";
                new File("/data/data/" + getPackageName() + "/images").mkdir();
                destinationFile = new File("/data/data/" + getPackageName() + "/images/" + filename);

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
                Log.i("OnSubscribe", "OnSubscribe");
            }

            @Override
            public void onNext(File file) {
                Log.i("OnNext", "File downloaded to " + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("Error", "Error " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i("OnComplete", "onCompleted");
                Log.i("FileSize (in bytes): ", Long.toString(destinationFile.length()));
            }
        };
    }
}
