package ite.jp.ak.lab05;

import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.PaintTank;
import ite.jp.ak.lab05.threads.PaintSupplierThread;
import ite.jp.ak.lab05.threads.PainterThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        PaintTank paintTank = PaintTank.getInstance(10);
        Fence fence = Fence.getInstance();
        fence.initializeFence(5, 10);
        PaintSupplierThread paintSupplierThread = new PaintSupplierThread("P", 2137);
        paintSupplierThread.start();
        List<PainterThread> painterThreadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PainterThread painterThread = new PainterThread("" + (char)(i + (int)'a'), (long)(Math.random() * 1000), 3);
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
