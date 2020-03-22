package com.study.zookeeper.locks;


import com.study.zookeeper.configurationcenter.DefaultWatch;
import com.study.zookeeper.configurationcenter.ZKConf;
import com.study.zookeeper.configurationcenter.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: 马士兵教育
 * @create: 2019-09-20 16:14
 */
public class TestLock {


    ZooKeeper zk;
    ZKConf zkConf;
    DefaultWatch defaultWatch;

    @Before
    public void conn(){
        zkConf = new ZKConf();
        zkConf.setAddress("39.97.227.101:2181");
        zkConf.setSessionTime(1000);
        defaultWatch = new DefaultWatch();
        ZKUtils.setConf(zkConf);
        ZKUtils.setWatch(defaultWatch);
        zk = ZKUtils.getZK();
    }

    @After
    public void close(){
        ZKUtils.closeZK();
    }

    @Test
    public void testlock(){
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);
                    String name = Thread.currentThread().getName();
                    watchCallBack.setThreadName(name);

                    try {
                        //tryLock
                        watchCallBack.tryLock();
                        System.out.println(name + " at work");
                        watchCallBack.getRootData();
//                        Thread.sleep(1000);
                        //unLock
                        watchCallBack.unLock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
       while(true){

       }
    }
}