package ite.jp.ak.lab05.threads;

import ite.jp.ak.lab05.shared.PaintTank;

public class PaintSupplierThread extends ThreadBase {

    private final PaintTank paintTank = PaintTank.getInstance(100);

    public PaintSupplierThread(String name, long delay, Triggerable triggerable) {
        super(name, delay, triggerable);
    }

    @Override
    public void run() {
        while (!isFinished()) {
            try {
                if (getTriggerable() != null) {
                    getTriggerable().trigger();
                }
                refillPaintTank();
                Thread.sleep(getDelay() + (long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void refillPaintTank() {
        if (!paintTank.isPaintTankFull()) {
            paintTank.refill(this.getThreadName());
        }
    }
}
