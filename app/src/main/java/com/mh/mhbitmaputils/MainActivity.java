package com.mh.mhbitmaputils;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);

        new Thread(new Runnable() {
            @Override
            public void run() {
               final Bitmap b= MHBUtils.url2Bitmap("http://img.hb.aicdn.com/c39799548b3f363b1016365aba28518c447b4a01c825-J1N8XR_fw658");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(MHBUtils.modify2RoundBitmap(b));
                    }
                });
            }
        }).start();
    }
}
