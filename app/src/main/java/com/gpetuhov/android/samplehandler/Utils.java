package com.gpetuhov.android.samplehandler;

import java.util.concurrent.TimeUnit;

public class Utils {
  public static void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
