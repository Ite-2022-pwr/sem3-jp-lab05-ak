package ite.jp.ak.lab05;

import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.PaintTank;
import ite.jp.ak.lab05.threads.PaintSupplierThread;
import ite.jp.ak.lab05.threads.PainterThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj ilość malarzy (nie więcej niż 25): ");
        int painterCount = Math.abs(scanner.nextInt()) % 25 + 1;

        System.out.print("Podaj pojemność wiaderka z farbą pojedynczego malarza: ");
        int paintBucketCapacity = Math.abs(scanner.nextInt());

        System.out.print("Podaj pojemność zbiornika z farbą: ");
        int paintTankCapacity = Math.abs(scanner.nextInt());

        System.out.print("Podaj ilość segmentów płotu: ");
        int fenceRailGroupCount = Math.abs(scanner.nextInt());

        System.out.print("Podaj ilość segmentów w segmencie: ");
        int fenceRailCount = Math.abs(scanner.nextInt());

        PaintTank paintTank = PaintTank.getInstance(paintTankCapacity);
        Fence fence = Fence.getInstance();
        fence.initializeFence(fenceRailGroupCount, fenceRailCount);
        PaintSupplierThread paintSupplierThread = new PaintSupplierThread("P", (long)(Math.random() * 1000));
        paintSupplierThread.start();
        List<PainterThread> painterThreadList = new ArrayList<>();
        for (int i = 0; i < painterCount; i++) {
            PainterThread painterThread = new PainterThread("" + (char)(i + (int)'a'), (long)(Math.random() * 1000), paintBucketCapacity);
            painterThreadList.add(painterThread);
            painterThread.start();
        }

        while (true) {
            try {
                Thread.sleep(500);
                String paintersNames = painterThreadList.stream().map(PainterThread::getThreadName).collect(Collectors.joining(" "));
                String paintersPaintBucketLevels = painterThreadList.stream().map(painterThread -> "" + painterThread.getPaintBucketLevel()).collect(Collectors.joining(" "));
                System.out.println(paintTank);
                System.out.println(paintersNames);
                System.out.println(paintersPaintBucketLevels);
                System.out.println(fence);
                if (painterThreadList.stream().allMatch(PainterThread::isFinished)) {
                    paintSupplierThread.setFinished(true);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
