package ite.jp.ak.lab05.shared;



public class PaintTank {

    private static PaintTank instance = null;

    private int capacity;
    private int paintAmount = 0;

    private String painter = ".";
    private String paintSupplier = ".";

    private PaintTank(int capacity) {
        this.capacity = capacity;
        this.paintAmount = capacity;
    }

    public synchronized void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public synchronized int getCapacity() {
        return this.capacity;
    }

    public static synchronized PaintTank getInstance(int capacity) {
        if (instance == null) {
            instance = new PaintTank(capacity);
        }
        return instance;
    }

    public synchronized void refill(String paintSupplier) {
        this.paintSupplier = paintSupplier;
        try {
            Thread.sleep((long)(Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.paintAmount = this.capacity;
        this.paintSupplier = ".";
        notifyAll();
    }

    public synchronized int getPaint(int amount, String painter) {
        this.painter = painter;
        while (this.paintAmount < amount) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.paintAmount -= amount;
        try {
            Thread.sleep((long)(Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.painter = ".";

        return amount;
    }

    public synchronized boolean isPaintTankEmpty() {
        return this.paintAmount == 0;
    }

    public synchronized boolean isPaintTankFull() {
        return this.paintAmount == this.capacity;
    }

    @Override
    public String toString() {
        return this.paintSupplier + " [" + this.paintAmount + "] " + this.painter;
    }
}
