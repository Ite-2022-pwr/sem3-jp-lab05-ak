package ite.jp.ak.lab05.shared;

import ite.jp.ak.lab05.enums.FenceRailStatus;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class FenceRailGroup {

    @Getter private final List<FenceRail> fenceRails = Collections.synchronizedList(new ArrayList<>());

    @Getter private final Map<String, Integer> lastRailPaintedBy = Collections.synchronizedMap(new HashMap<>());
    private boolean isBusy = false;

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

    public synchronized FenceRail getNextFenceRail(String painterName) {
        if (lastRailPaintedBy.containsKey(painterName)) {
            var nextIndex = lastRailPaintedBy.get(painterName) + 1;

            if (nextIndex >= fenceRails.size() || fenceRails.get(nextIndex).getStatus() != FenceRailStatus.WaitingForPaint) {
                return null;
            }

            var rail = fenceRails.get(nextIndex);
            rail.setStatus(FenceRailStatus.InPainting);
            rail.setPaintedBy(painterName);
            lastRailPaintedBy.replace(painterName, nextIndex);
            return rail;
        }

        FenceRail rail;
        if (fenceRails.get(0).getStatus() == FenceRailStatus.WaitingForPaint) {
            rail = fenceRails.get(0);
        } else {
            var fragment = getLongestNotPaintedFenceRailsFragment();

            if (fragment == null) {
                return null;
            }

            var fragmentIndex = fragment.size() / 2;
            rail = fenceRails.get(fragment.get(fragmentIndex).getIndexInGroup());
        }

//        System.out.println(painterName + ": " + rail.getPaintedBy() + " " + rail.getStatus());

        rail.setStatus(FenceRailStatus.InPainting);
        rail.setPaintedBy(painterName);
        lastRailPaintedBy.put(painterName, rail.getIndexInGroup());
        return rail;
    }

    @Override
    public String toString() {
        return this.fenceRails.stream().map(FenceRail::toString).collect(Collectors.joining(" "));
    }
}
