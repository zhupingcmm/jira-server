package com.mf.jira.server.base;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZkLock implements Lock {

    private static final String zkServer = "localhost:2181";

    private ZkClient zkClient;
    private String lockPath;

    public ZkLock(String lockPath) {
        zkClient = new ZkClient(zkServer);
        this.lockPath = lockPath;
        zkClient.setZkSerializer(new ZkSerializer());
    }
    @Override
    public void lock() {
        if (!tryLock()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            try {
                IZkDataListener dataListener = new IZkDataListener() {
                    @Override
                    public void handleDataChange(String s, Object o) throws Exception {

                    }

                    @Override
                    public void handleDataDeleted(String s) throws Exception {
                        System.out.println("*** 锁释放了，可以重新获取锁了 ***");
                        countDownLatch.countDown();
                    }
                };
                zkClient.createEphemeralSequential("/a","a");
                //监听 zk lockPath 的变化
                zkClient.subscribeDataChanges(lockPath, dataListener);
                //hang 住直到 监听到 lockPath被删除
                countDownLatch.await();
                //解除之间的监听
                zkClient.unsubscribeDataChanges(lockPath, dataListener);
                //重新去抢锁
                lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public boolean tryLock() {
        try {
            zkClient.createEphemeral(lockPath, Thread.currentThread().getName());
        } catch (ZkNodeExistsException e) {
            return  false;
        }
        return true;
    }

    @Override
    public void unlock() {
        String data = zkClient.readData(lockPath);
        if(StringUtils.equals(data, Thread.currentThread().getName())) {
            zkClient.delete(lockPath);
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    public static void main(String[] args) {
        int size = 20;
        CountDownLatch countDownLatch = new CountDownLatch(size);

        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    ZkLock zkLock = new ZkLock("/lock");
                    zkLock.lock();

                    System.out.println("*** " + Thread.currentThread().getName() + "获得了锁 ***");
                    zkLock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }).start();

            countDownLatch.countDown();
        }
    }
}
