package com.gpetuhov.android.samplehandler;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

  // By default Handler uses Looper from the thread, from which it is created,
  // so here Handler is using Looper from the UI thread.
  private Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();

    // Create background thread
    Thread myThread = new Thread(new Runnable() {
      @Override
      public void run() {
        sleep();

        Runnable task = new Runnable() {
          @Override
          public void run() {
            Toast.makeText(MainActivity.this, "Message from background thread", Toast.LENGTH_SHORT).show();
          }
        };

        // We cannot show Toast message from background thread directly, so we use our handler
        handler.post(task);

        sleep();

        // Or we can create new handler and pass looper from the main thread into it
        new Handler(Looper.getMainLooper()).post(task);
      }
    });

    myThread.start();
  }

  private void sleep() {
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
