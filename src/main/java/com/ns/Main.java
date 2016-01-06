package com.ns;

import java.util.concurrent.TimeUnit;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;

import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.BucketSettings;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;


public class Main {
  public static void main(String[] args) throws Exception {
    CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
              .connectTimeout(TimeUnit.SECONDS.toMillis(10))
              .requestBufferSize(1024)
              .build();

    Cluster cluster = CouchbaseCluster.create(env, args[0]);
    Bucket bucket = cluster.openBucket("default");

    int threadNum = Integer.valueOf(args[2]);
    int opt = 100;
    int i = 0;

    if (args.length > 3) {
      opt = Integer.valueOf(args[3]);
    }

    switch (args[1]) {
      case "insert":
      InsertThread it[] = new InsertThread[threadNum];
      for (i = 0; i < threadNum; i++) {
        it[i] = new InsertThread("cb-thread-" + String.valueOf(i));
        it[i].setBucket(bucket);
        it[i].setSleepTime(opt);
        it[i].start();
      }
      break;

      case "view":
      ViewThread vt[] = new ViewThread[threadNum];
      for (i = 0; i < threadNum; i++) {
        vt[i] = new ViewThread("cb-thread-" + String.valueOf(i));
        vt[i].setBucket(bucket);
        vt[i].setLoopNum(opt);
        vt[i].start();
      }
      break;

      case "sql":
      SqlThread st[] = new SqlThread[threadNum];
      for (i = 0; i < threadNum; i++) {
        st[i] = new SqlThread("cb-thread-" + String.valueOf(i));
        st[i].setBucket(bucket);
        st[i].setLoopNum(opt);
        st[i].start();
      }
      break;
    }

//    bucket.close();
//    cluster.disconnect();
  }
}
