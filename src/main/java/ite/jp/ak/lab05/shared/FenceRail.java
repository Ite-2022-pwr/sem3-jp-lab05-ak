package ite.jp.ak.lab05.shared;

import ite.jp.ak.lab05.enums.FenceRailStatus;
import lombok.Getter;

public class FenceRail {
    private FenceRailStatus status;
    private String paintedBy;

    @Getter private final int x;
    @Getter private final int y;

    public FenceRail(int x, int y) {
        this.status = FenceRailStatus.WaitingForPaint;
        this.paintedBy = ".";
        this.x = x;
        this.y = y;
    }

    public synchronized FenceRailStatus getStatus() {
        return this.status;
    }

    public synchronized String getPaintedBy() {
        return this.paintedBy;
    }

    public synchronized void paint(String painter) {
        this.paintedBy = painter;
        this.status = FenceRailStatus.Painted;
    }

    @Override
    public synchronized String toString() {
        return this.paintedBy;
    }
}
