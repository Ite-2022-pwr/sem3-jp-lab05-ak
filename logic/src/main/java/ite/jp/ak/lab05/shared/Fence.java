package ite.jp.ak.lab05.shared;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Fence {

    private static Fence instance = null;

    @Getter private final List<FenceRailGroup> fenceRailGroups = Collections.synchronizedList(new ArrayList<>());

    private Fence() {

    }

    public static synchronized Fence getInstance() { // synchronized na klasie
        if (instance == null) {
            instance = new Fence();
        }
        return instance;
    }

    public synchronized void initializeFence(int fenceRailGroupAmount, int fenceRailsInGroupAmount) {
        this.fenceRailGroups.clear();
        for (int i = 0; i < fenceRailGroupAmount; i++) {
            FenceRailGroup fenceRailGroup = new FenceRailGroup();
            for (int j = 0; j < fenceRailsInGroupAmount; j++) {
                fenceRailGroup.getFenceRails().add(new FenceRail(i, j));
            }
            this.fenceRailGroups.add(fenceRailGroup);
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

    public synchronized FenceRailGroup getNextFenceRailGroup() {
        FenceRailGroup freeRailGroup = fenceRailGroups.stream().filter(g -> !g.isBusy()).findFirst().orElse(null);
        if (freeRailGroup != null) {
            freeRailGroup.setIsBusy(true);
            return freeRailGroup;
        }

        var longestNotPaintedFragment = getLongestNotPaintedFenceRailsFragment();

        if (longestNotPaintedFragment == null) {
            return null;
        }

        var groupID = longestNotPaintedFragment.get(0).getGroupID();
        return fenceRailGroups.get(groupID);
    }

    @Override
    public String toString() {
        return this.fenceRailGroups.stream().map(FenceRailGroup::toString).collect(Collectors.joining(" | "));
    }
}
