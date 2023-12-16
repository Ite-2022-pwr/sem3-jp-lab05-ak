package ite.jp.ak.lab05.shared;

import ite.jp.ak.lab05.enums.FenceRailStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FenceRail {
    private FenceRailStatus status;
    private String paintedBy;

    private final int groupID;
    private final int indexInGroup;

    public FenceRail(int groupID, int indexInGroup) {
        this.status = FenceRailStatus.WaitingForPaint;
        this.paintedBy = ".";
        this.groupID = groupID;
        this.indexInGroup = indexInGroup;
    }

    public synchronized void paint(String painter) {
        if (this.status == FenceRailStatus.Painted) {
            return;
        }
        this.paintedBy = painter;
        this.status = FenceRailStatus.Painted;
    }

    @Override
    public String toString() {
        return this.paintedBy;
    }
}
