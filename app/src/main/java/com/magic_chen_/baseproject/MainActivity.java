package com.magic_chen_.baseproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.magic_chen_.baseproject.response.AdvertiseResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import static com.magic_chen_.baseproject.config.Const.API;

public class MainActivity extends AppCompatActivity {
    public static final String TAG  = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Subscription mSubscription;
        mSubscription = API.getAdvertise("youqing", "zh_CN", 1,"guanwang")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AdvertiseResponse>() {
                    @Override
                    public void onStart() {
                        Log.d(TAG,"  result: start");
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"  result: onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"  result: on error:"+e.getMessage());
                    }

                    @Override
                    public void onNext(AdvertiseResponse response) {
                        Log.d(TAG,"  result data数量:"+response.data.get(0).toString());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}