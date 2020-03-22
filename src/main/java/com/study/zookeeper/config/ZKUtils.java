package com.study.zookeeper.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {

    private static ZooKeeper zk;

    private static String addrees = "39.97.227.101:2181";

    private static DefaultWatch watch = new DefaultWatch();

    private static CountDownLatch init  =  new CountDownLatch(1);
    public static ZooKeeper  getZK(){
        System.out.println("ZKUtils.getZK()............start");
        try {
            zk = new ZooKeeper(addrees,1000,watch);
            watch.setCc(init);
            init.await();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ZKUtils.getZK()............end");
        return zk;
    }

}
