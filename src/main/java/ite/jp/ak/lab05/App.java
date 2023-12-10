package ite.jp.ak.lab05;

import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.PaintTank;
import ite.jp.ak.lab05.threads.PaintSupplierThread;
import ite.jp.ak.lab05.threads.PainterThread;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < 3; i++) {
            PainterThread painterThread = new PainterThread("" + (char)(i + (int)'a'), (long)(Math.random() * 100), 3);
            painterThreadList.add(painterThread);
            painterThread.start();
        }

        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println(paintTank);
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
