package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: Demo1
 * @description: 顺序执行
 * @author: 戴灵飞
 * @create: 2019-08-20 09:29
 **/

public class ConditionSeqExec {

    volatile private static int nextWhole = 1;
    private static ReentrantLock lock = new ReentrantLock();
    final private static Condition conditionA = lock.newCondition();
    final private static Condition conditionB = lock.newCondition();
    final private static Condition conditionC = lock.newCondition();

    public static void main(String[] args) throws Exception {
        Thread[] threadsA = new Thread[5];
        Thread[] threadsB = new Thread[5];
        Thread[] threadsC = new Thread[5];

        for (int j = 0; j < 5; j++) {
            threadsA[j] = new Thread(()->{
                lock.lock();
                try {
                    while (nextWhole != 1) {
                        conditionA.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.println("ThreadA:" + (i + 1));
                    }
                    //唤醒下一个线程
                    nextWhole = 2;
                    conditionB.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
            threadsB[j] = new Thread(()->{
                lock.lock();
                try {
                    while (nextWhole != 2) {
                        conditionB.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.println("ThreadB:" + (i + 1));
                    }
                    //唤醒下一个线程
                    nextWhole = 3;
                    conditionC.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
            threadsC[j] = new Thread(()->{
                lock.lock();
                try {
                    while (nextWhole != 3) {
                        conditionC.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.println("ThreadC:" + (i + 1));
                    }
                    //唤醒下一个线程
                    nextWhole = 1;
                    conditionA.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });

            //开启
            threadsA[j].start();
            threadsB[j].start();
            threadsC[j].start();
        }
    }
}

