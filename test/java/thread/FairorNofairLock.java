package thread;

/**
 * @program: Demo1
 * @description: 公平锁和非公平锁
 * @author: 戴灵飞
 * @create: 2019-08-20 10:48
 **/

import java.util.concurrent.locks.ReentrantLock;

/**
 * 死锁的四个必要条件:
 * 互斥条件
 * 请求和保持条件
 * 不可剥夺条件
 * 循环等待条件
 */
public class FairorNofairLock {

    public static void main(String[] args) throws InterruptedException {
        final Service service = new Service(false);//true为公平锁，false为非公平锁

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("★线程" + Thread.currentThread().getName()
                        + "运行了");
                service.serviceMethod();
            }
        };

        Thread[] threadArray = new Thread[10];
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(runnable);
            threadArray[i] = thread;
        }
        for (int i = 0; i < 10; i++) {
            threadArray[i].start();
        }

    }
    static public class Service {

        private ReentrantLock lock;

        public Service(boolean isFair) {
            super();
            lock = new ReentrantLock(isFair);
        }

        public void serviceMethod() {
            lock.lock();
            try {
                System.out.println("ThreadName=" + Thread.currentThread().getName()
                        + "获得锁定");
            } finally {
                lock.unlock();
            }
        }

    }
}
