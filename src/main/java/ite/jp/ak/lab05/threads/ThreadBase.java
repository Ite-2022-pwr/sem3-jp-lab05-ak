package ite.jp.ak.lab05.threads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ThreadBase extends Thread {
    private String threadName;
    private int delay;
    private boolean finished;

    public ThreadBase(String name, int delay) {
        this.threadName = name;
        this.delay = delay;
        this.finished = false;
    }
}
