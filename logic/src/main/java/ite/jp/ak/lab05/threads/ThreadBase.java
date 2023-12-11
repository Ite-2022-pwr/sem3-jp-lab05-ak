package ite.jp.ak.lab05.threads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThreadBase extends Thread {
    private final String threadName;
    private final long delay;
    private boolean finished;

    public ThreadBase(String name, long delay) {
        super(name);
        this.threadName = name;
        this.delay = delay;
        this.finished = false;
    }

    @Override
    public String toString() {
        return this.threadName;
    }
}
