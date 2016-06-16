package com.teresazl.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.teresazl.library.ProcessButton;
import com.teresazl.library.impl.LoadingButton;

public class MainActivity extends AppCompatActivity implements ProcessButton.LoadCompleteListener {

    private static final int MSG_UPDATE = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = loadingButton.getProgress();

            if (progress >= 100) {
                mHandler.removeMessages(MSG_UPDATE);
            } else {
                loadingButton.setProgress(++progress);
            }

            mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
        }
    };

    private LoadingButton loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingButton = (LoadingButton) findViewById(R.id.loading_btn);
        loadingButton.setOnLoadCompleteListener(this);

        loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_UPDATE);
            }
        });

    }

    @Override
    public void loadComplete() {
        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
    }
}
