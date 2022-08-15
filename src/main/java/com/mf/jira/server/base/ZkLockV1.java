package com.mf.jira.server.base;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZkLockV1 implements Lock {

    private final String LOCK_PATH = "/lock";
    private ZkClient zkClient;

    public ZkLockV1() {
        zkClient = new ZkClient("localhost:2181");
        zkClient.setZkSerializer(new ZkSerializer());
    }

    @Override
    public boolean tryLock() {
        try {
            zkClient.createEphemeral(LOCK_PATH, Thread.currentThread().getName());
        } catch (ZkNodeExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            IZkDataListener dataListener = new IZkDataListener(){

                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {

                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    System.out.println("*** 锁释放了，可以重新获取锁了 ***");
                    countDownLatch.countDown();

                }
            };

            System.out.println("***"+Thread.currentThread().getName()+" 准备监听 ***");
            zkClient.subscribeDataChanges(LOCK_PATH,dataListener);
            try {
                countDownLatch.await();
                zkClient.unsubscribeDataChanges(LOCK_PATH, dataListener);
                lock();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void unlock() {
        System.out.println("*** 准备释放锁 ***");
        zkClient.delete(LOCK_PATH);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        int size = 10;
        CountDownLatch countDownLatch = new CountDownLatch(size);

        for (int i = 0; i < size; i++) {

            new Thread(() -> {
                try {

                    System.out.println("thread name:::" + Thread.currentThread().getName());
                    countDownLatch.await();
                    ZkLockV1 zkLockV1 = new ZkLockV1();
                    zkLockV1.lock();
                    Thread.sleep(100);
                    System.out.println("*** " +Thread.currentThread().getName() + "获得了锁 *****");
                    zkLockV1.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            System.out.println("i::" + i);
            countDownLatch.countDown();
        }

    }
}
