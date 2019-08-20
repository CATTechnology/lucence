package thread;


/**
 * @program: Demo1
 * @description: 主类
 * @author: 戴灵飞
 * @create: 2019-08-20 09:13
 **/
public class UseMoreConditionWaitNotify {
    public static void main(String[] args) throws InterruptedException {

        MyserviceMoreCondition service = new MyserviceMoreCondition();

        ThreadA a = new ThreadA(service);
        a.setName("A");
        a.start();

        ThreadB b = new ThreadB(service);
        b.setName("B");
        b.start();

        Thread.sleep(3000);

        service.signalAll_A();
        //service.signalAll_B();
    }

    static public class ThreadA extends Thread {
        private MyserviceMoreCondition service;

        public ThreadA(MyserviceMoreCondition service) {
            super();
            this.service = service;
        }

        @Override
        public void run() {
            service.awaitA();
        }
    }

    static public class ThreadB extends Thread {

        private MyserviceMoreCondition service;

        public ThreadB(MyserviceMoreCondition service) {
            super();
            this.service = service;
        }

        @Override
        public void run() {
            service.awaitB();
        }
    }
}

