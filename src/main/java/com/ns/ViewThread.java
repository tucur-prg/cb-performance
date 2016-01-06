package com.ns;

import java.util.Iterator;

import com.couchbase.client.java.Bucket;

import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewRow;
import com.couchbase.client.java.view.Stale;

class ViewThread extends Thread {
  private Bucket bucket;
  private int loopNum;

  public ViewThread(String name) {
    super(name);
  }

  public void setBucket(Bucket bucket) {
    this.bucket = bucket;
  }

  public void setLoopNum(int loopNum) {
    this.loopNum = loopNum;
  }

  @Override
  public void run() {
    ViewQuery view = ViewQuery.from("performance", "meta").stale(Stale.FALSE).descending().limit(1);

    long begin = System.currentTimeMillis();

    for (int i = 0; i < loopNum; i++) {
      Iterator<ViewRow> iter = bucket.query(view).rows();
      while (iter.hasNext()) {
        System.out.print(Thread.currentThread().getName() + "|" + String.valueOf(i) + "|");
        System.out.println(iter.next());
      }
    }

    long end = System.currentTimeMillis();

    System.out.println(Thread.currentThread().getName() + "|" + String.valueOf(end - begin) + "ms");
  }
}
