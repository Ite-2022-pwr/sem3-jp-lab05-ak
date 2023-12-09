package ite.jp.ak.lab05.threads;

public class PaintSupplierThread extends ThreadBase {

    public PaintSupplierThread(String name, int delay) {
        super(name, delay);
    }

    @Override
    public void run() {
        while (!isFinished()) {
            try {
                Thread.sleep(getDelay());
                System.out.println(getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
