package com.study.zookeeper.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    ZooKeeper zk ;
    MyConf conf ;
    CountDownLatch cc = new CountDownLatch(1);

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }


    public void aWait(){
        System.out.println("WatchCallBack-aWait()..........start");
        zk.exists("/AppConf",this,this ,"ABC");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("WatchCallBack-aWait()..........end");
    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("WatchCallBack-processResult(int rc, String path, Object ctx, byte[] data, Stat stat)..........start");
        if(data != null ){
            String s = new String(data);
            conf.setConf(s);
            cc.countDown();
        }

        System.out.println("WatchCallBack-processResult(int rc, String path, Object ctx, byte[] data, Stat stat)..........end");
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("WatchCallBack-processResult(int rc, String path, Object ctx, Stat stat)..........start");
        if(stat != null){
            zk.getData("/AppConf",this,this,"sdfs");
        }
        System.out.println("WatchCallBack-processResult(int rc, String path, Object ctx, Stat stat)..........end");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("WatchCallBack-process(WatchedEvent event)..........start");
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"sdfs");

                break;
            case NodeDeleted:
                //容忍性
//                conf.setConf("");
//                cc = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"sdfs");

                break;
            case NodeChildrenChanged:
                break;
        }
        System.out.println("WatchCallBack-process(WatchedEvent event)..........end");
    }

}
