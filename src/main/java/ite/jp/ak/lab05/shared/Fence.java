package ite.jp.ak.lab05.shared;

import java.util.ArrayList;
import java.util.List;

public class Fence {

    private static Fence instance = null;

    private final List<FenceRailGroup> fenceRailGroups = new ArrayList<>();

    private Fence() {

    }

    public static synchronized Fence getInstance() {
        if (instance == null) {
            instance = new Fence();
        }
        return instance;
    }

    public synchronized void addFenceRailGroup(FenceRailGroup fenceRailGroup) {
        this.fenceRailGroups.add(fenceRailGroup);
    }

    public synchronized List<FenceRailGroup> getFenceRailGroups() {
        return this.fenceRailGroups;
    }

    public synchronized List<FenceRailGroup> getFreeFenceRailGroups() {
        return this.fenceRailGroups.stream().filter(fenceRailGroup -> !fenceRailGroup.isBusy()).collect(java.util.stream.Collectors.toList());
    }

    public synchronized void initializeFence(int fenceRailGroupAmount, int fenceRailsInGroupAmount) {
        this.fenceRailGroups.clear();
        for (int i = 0; i < fenceRailGroupAmount; i++) {
            FenceRailGroup fenceRailGroup = new FenceRailGroup();
            for (int j = 0; j < fenceRailsInGroupAmount; j++) {
                fenceRailGroup.addFenceRail(new FenceRail(i, j));
            }
            this.addFenceRailGroup(fenceRailGroup);
        }
    }

    public synchronized List<FenceRail> getLongestNotPaintedFenceRailsFragment() {
        List<FenceRail> longestNotPaintedFenceRails = new ArrayList<>();
        for (FenceRailGroup fenceRailGroup : fenceRailGroups) {
            List<FenceRail> notPaintedFenceRails = fenceRailGroup.getLongestNotPaintedFenceRailsFragment();
            if (notPaintedFenceRails == null) {
                continue;
            }
            if (notPaintedFenceRails.size() > longestNotPaintedFenceRails.size()) {
                longestNotPaintedFenceRails = notPaintedFenceRails;
            }
        }
        return !longestNotPaintedFenceRails.isEmpty() ? longestNotPaintedFenceRails : null;
    }

    @Override
    public synchronized String toString() {
        return this.fenceRailGroups.stream().map(FenceRailGroup::toString).collect(java.util.stream.Collectors.joining(" | "));
    }
}
