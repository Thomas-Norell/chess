package game;

import engine.MonteCarloTree;

class Ponder implements Runnable {
    public Thread t;
    boolean suspended = false;
    MonteCarloTree tree;

    Ponder(MonteCarloTree tree) {
        this.tree = tree;
    }

    public void run() {
        try {
            while (true) {
                tree.deepen();
                synchronized(this) {
                    while(suspended) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread (this);
            t.start();
        }
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume(MonteCarloTree tree) {
        suspended = false;
        this.tree = tree;
        notify();
    }
}
