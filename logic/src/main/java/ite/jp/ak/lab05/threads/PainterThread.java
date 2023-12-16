package ite.jp.ak.lab05.threads;

import ite.jp.ak.lab05.enums.FenceRailStatus;
import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.FenceRail;
import ite.jp.ak.lab05.shared.FenceRailGroup;
import ite.jp.ak.lab05.shared.PaintTank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PainterThread extends ThreadBase {

    private final int paintBucketCapacity;
    private int paintBucketLevel;

    private final Fence fence = Fence.getInstance();
    private final PaintTank paintTank = PaintTank.getInstance(100);

    public PainterThread(String name, long delay, int paintBucketCapacity, Triggerable triggerable) {
        super(name, delay, triggerable);
        this.paintBucketCapacity = paintBucketCapacity;
        this.paintBucketLevel = paintBucketCapacity;
    }


    public void paintFence() {
        var railGroup = fence.getNextFenceRailGroup();

        if (railGroup == null) {
            this.setFinished(true);
            return;
        }

        var rail = railGroup.getNextFenceRail(this.getThreadName());
        while (rail != null) {
            checkPaintBucket();
            paint(rail);
            getSomeRest();

            rail = railGroup.getNextFenceRail(getThreadName());
        }
    }

    public void getSomeRest() {
        try {
            Thread.sleep(getDelay() + (long)(Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paint(FenceRail fenceRail) {
        fenceRail.paint(this.getThreadName());
        this.paintBucketLevel--;
    }

    public void checkPaintBucket() {
        if (this.isPaintBucketEmpty()) {
            this.paintBucketLevel = paintTank.getPaint(this.paintBucketCapacity, this.getThreadName());
        }
    }

    private boolean isPaintBucketEmpty() {
        return this.paintBucketLevel == 0;
    }

    @Override
    public void run() {
        while (!isFinished()) {
            if (getTriggerable() != null) {
                getTriggerable().trigger();
            }
            paintFence();
        }
    }

    @Override
    public String toString() {
        return this.getThreadName();
    }
}
