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

    public PainterThread(String name, long delay, int paintBucketCapacity) {
        super(name, delay);
        this.paintBucketCapacity = paintBucketCapacity;
        this.paintBucketLevel = paintBucketCapacity;
    }

    public void paintFence() {
        List<FenceRailGroup> freeFenceRailGroups = fence.getFenceRailGroups().stream().filter(fenceRailGroup -> !fenceRailGroup.isBusy()).collect(java.util.stream.Collectors.toList());
        if (!freeFenceRailGroups.isEmpty()) {
            FenceRailGroup fenceRailGroup = freeFenceRailGroups.get(0);
            if (fenceRailGroup.isBusy()) return;
            fenceRailGroup.setIsBusy(true);
            for (FenceRail fenceRail : fenceRailGroup.getFenceRails()){

                if (fenceRail.getStatus() == FenceRailStatus.Painted){
                    return;
                }

                checkPaintBucket();
                paint(fenceRail);

                getSomeRest();
            }
        } else {
            List<FenceRail> notPaintedFenceRails = fence.getLongestNotPaintedFenceRailsFragment();
            if (notPaintedFenceRails == null) {
//                System.out.println(getThreadName() + ": No more rails to paint!");
                this.setFinished(true);
                return;
            }

            int startPosition = notPaintedFenceRails.size() / 2 + 1;
            for (int i = startPosition; i < notPaintedFenceRails.size(); i++) {
                FenceRail fenceRail = notPaintedFenceRails.get(i);
                checkPaintBucket();
                if (fenceRail.getStatus() == FenceRailStatus.Painted){
                    return;
                }
//                System.out.println(getThreadName() + ": Helping others");

                paint(fenceRail);

                getSomeRest();
            }
        }
    }

    public void getSomeRest() {
        try {
            Thread.sleep(getDelay() + (long)(Math.random() * 1000));
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
            paintFence();
        }
    }

    @Override
    public String toString() {
        return this.getThreadName();
    }
}
