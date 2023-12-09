package ite.jp.ak.lab05.threads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PainterThread extends ThreadBase {

    private boolean isPaintBucketEmpty;
    private int paintBucketCapacity;

    public PainterThread(String name, int delay, int paintBucketCapacity) {
        super(name, delay);
        this.isPaintBucketEmpty = false;
        this.paintBucketCapacity = paintBucketCapacity;
    }

    @Override
    public void run() {
        while (!isFinished()) {
            try {
                Thread.sleep(getDelay());
                System.out.println(getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
