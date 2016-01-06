package com.ns;

import com.couchbase.client.java.Bucket;

import com.couchbase.client.java.document.LegacyDocument;

class InsertThread extends Thread {
  private Bucket bucket;
  private int sleepTime;

  public InsertThread(String name) {
    super(name);
  }

  public void setBucket(Bucket bucket) {
    this.bucket = bucket;
  }

  public void setSleepTime(int time) {
    this.sleepTime = time;
  }

  @Override
  public void run() {
    try {
      while (true) {
        LegacyDocument doc = LegacyDocument.create("example." + Thread.currentThread().getName() + "." + String.valueOf(System.currentTimeMillis()), "{\"thread\":\"" + Thread.currentThread().getName() + "\", \"created_at\":\"" + String.valueOf(System.currentTimeMillis()) + "\"}");
        System.out.println(bucket.upsert(doc));
        sleep(this.sleepTime);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
