package ru.profit.multithreading;

import ru.profit.educations.Util;

public class Main {

    static Object key = new Object();
    static volatile int count = 100;

    public static void main(String[] args) {
        System.err.println(Thread.currentThread());
      /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    minus();
                }
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    minus();
                }
            }
        });
        thread2.start();


        try {
            thread.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.err.println(count);

    }

    private static void method() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println(Thread.currentThread() + ": " + i);
        }
    }


    private static void minus() {
        synchronized (key) {
            if (count > 50) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count -= 5;
            }
        }
    }
    */
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        });
        thread3.start();

        Util.getIntegerFromIn("enter 0 ");
        thread3.interrupt();
        Util.getIntegerFromIn("else");

        synchronized (key) {
            key.notifyAll();
        }

    }

    private static void listen(){
        System.err.println("sleep");

        synchronized (key) {
            try {
                key.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.err.println("wake up");
    }

}
