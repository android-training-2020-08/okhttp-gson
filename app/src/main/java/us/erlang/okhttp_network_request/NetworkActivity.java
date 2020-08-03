package us.erlang.okhttp_network_request;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Button button = findViewById(R.id.get_json_data);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJsonData();
            }
        });
    }

    private void getJsonData() {
        Observable.create(observable -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://twc-android-bootcamp.github.io/fake-data/data/default.json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                observable.onNext(json);
                observable.onComplete();
            } catch (IOException e) {
                e.printStackTrace();
                observable.onError(e);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(json -> {
                            Gson gson = new Gson();
                            Wrapper wrapper = gson.fromJson((String)json, Wrapper.class);
                            String firstName =  wrapper.getData().get(0).getName();
                            Toast.makeText(getApplicationContext(), firstName, Toast.LENGTH_LONG).show();
                        },
                        error -> {
                            Toast.makeText(getApplicationContext(), "Failed to get josn data", Toast.LENGTH_LONG).show();
                        });
    }
}