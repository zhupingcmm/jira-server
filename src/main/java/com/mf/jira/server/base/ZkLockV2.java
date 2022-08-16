package com.mf.jira.server.base;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
public class ZkLockV2 implements Lock {

    private ZkClient zkClient;

    ThreadLocal<String> currentPath = new ThreadLocal<String>();
    ThreadLocal<String> previousPath = new ThreadLocal<String>();
    private static final String LOCK_PATH = "/lock";

    public ZkLockV2(){
        zkClient = new ZkClient("localhost:2181");
        zkClient.setZkSerializer(new ZkSerializer());
        if (!zkClient.exists(LOCK_PATH)) {
            zkClient.createPersistent(LOCK_PATH);
        }
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

            zkClient.subscribeDataChanges(previousPath.get(), dataListener);
            try {
                countDownLatch.await();
                zkClient.unsubscribeDataChanges(previousPath.get(), dataListener);
                lock();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        if (currentPath.get() == null) {
            String path = zkClient.createEphemeralSequential(LOCK_PATH + "/", Thread.currentThread().getName());
            currentPath.set(path);
        }
        List<String> children = zkClient.getChildren(LOCK_PATH);
        Collections.sort(children);
        if (currentPath.get().equals(LOCK_PATH + "/" + children.get(0))) {
            return true;
        } else {
            int index = children.indexOf(currentPath.get().substring(LOCK_PATH.length() + 1));
            String path = LOCK_PATH + "/" + children.get(index -1);
            previousPath.set(path);
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        System.out.println("*** 准备释放锁 ***");
        zkClient.delete(currentPath.get());
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
                    countDownLatch.await();
                    ZkLockV2 lock = new ZkLockV2();

                    lock.lock();
                    Thread.sleep(100);
                    System.out.println("*** " +Thread.currentThread().getName() + "获得了锁 *****");
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            countDownLatch.countDown();
        }
    }
}
