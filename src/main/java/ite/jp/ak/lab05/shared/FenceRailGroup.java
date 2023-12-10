package ite.jp.ak.lab05.shared;

import ite.jp.ak.lab05.enums.FenceRailStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FenceRailGroup {

    private final List<FenceRail> fenceRails = new ArrayList<>();

    private boolean isBusy = false;

    public synchronized void addFenceRail(FenceRail fenceRail) {
        this.fenceRails.add(fenceRail);
    }

    public synchronized List<FenceRail> getFenceRailsByStatus(FenceRailStatus status) {
        return this.fenceRails.stream().filter(fenceRail -> fenceRail.getStatus() == status).collect(Collectors.toList());
    }

    public synchronized List<FenceRail> getFenceRails() {
        return this.fenceRails;
    }

    public synchronized boolean isBusy() {
        return this.isBusy;
    }

    public synchronized void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public synchronized List<FenceRail> getLongestNotPaintedFenceRailsFragment() {
        List<FenceRail> longestNotPaintedFenceRailsFragment = new ArrayList<>();
        List<FenceRail> notPaintedFenceRails = new ArrayList<>();
        for (FenceRail fenceRail : this.fenceRails) {
            if (fenceRail.getStatus() == FenceRailStatus.WaitingForPaint) {
//                System.out.println("This rail is waiting for painting!");
                notPaintedFenceRails.add(fenceRail);
            } else {
//                System.out.println("Comparing " + notPaintedFenceRails.size() + " to " + longestNotPaintedFenceRailsFragment.size());
                if (notPaintedFenceRails.size() > longestNotPaintedFenceRailsFragment.size()) {
                    longestNotPaintedFenceRailsFragment = new ArrayList<>(notPaintedFenceRails);
//                    System.out.println("Copying to longest (size: " + longestNotPaintedFenceRailsFragment.size() + ")");
                }
                notPaintedFenceRails.clear();
            }
        }
        if (notPaintedFenceRails.size() > longestNotPaintedFenceRailsFragment.size()) {
            longestNotPaintedFenceRailsFragment = new ArrayList<>(notPaintedFenceRails);
//            System.out.println("Copying to longest (size: " + longestNotPaintedFenceRailsFragment.size() + ")");
        }
//        System.out.println("Longest fragment size: " + longestNotPaintedFenceRailsFragment.size());
        return !longestNotPaintedFenceRailsFragment.isEmpty() ? longestNotPaintedFenceRailsFragment : null;
    }

    @Override
    public synchronized String toString() {
        return this.fenceRails.stream().map(FenceRail::toString).collect(Collectors.joining(" "));
    }
}
