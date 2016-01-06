package com.ns;

import java.util.Iterator;

import com.couchbase.client.java.Bucket;

import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;

class SqlThread extends Thread {
  private Bucket bucket;
  private int loopNum;

  public SqlThread(String name) {
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
    String sql = "SELECT COUNT(*) FROM default";
    N1qlQuery query = N1qlQuery.simple(sql);

    long begin = System.currentTimeMillis();

    for (int i = 0; i < loopNum; i++) {
      Iterator<N1qlQueryRow> iter = bucket.query(query).rows();
      while (iter.hasNext()) {
        System.out.print(Thread.currentThread().getName() + "|" + String.valueOf(i) + "|");
        System.out.println(iter.next());
      }
    }

    long end = System.currentTimeMillis();

    System.out.println(Thread.currentThread().getName() + "|" + String.valueOf(end - begin) + "ms");
  }
}
