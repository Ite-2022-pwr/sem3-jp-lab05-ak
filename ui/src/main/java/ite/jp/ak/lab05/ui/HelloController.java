package ite.jp.ak.lab05.ui;

import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.FenceRailGroup;
import ite.jp.ak.lab05.shared.PaintTank;
import ite.jp.ak.lab05.threads.PaintSupplierThread;
import ite.jp.ak.lab05.threads.PainterThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    // Pola tekstowe parametrów
    @FXML protected TextField paintersCount;
    @FXML protected TextField paintersBucketCapacity;
    @FXML protected TextField paintTankCapacity;
    @FXML protected TextField fenceRailsGroupsCount;
    @FXML protected TextField fenceRailsGroupSize;

    // Stan zbiornika z farbą
    @FXML protected Label paintTankLevel;

    // Tabela malarzy
    @FXML protected TableView<PainterThread> paintersTableView;
    @FXML protected TableColumn<PainterThread, String> painterNameTableColumn;
    @FXML protected TableColumn<PainterThread, Integer> painterBucketLevelTableColumn;
    @FXML protected TableColumn<PainterThread, Boolean> painterStateTableColumn;

    // Stan płotu
    @FXML protected ListView<String> fenceListView;

    // Przyciski
    @FXML protected Button startButton;

    // Obiekty symulacji
    private final List<PainterThread> painterThreadList = new ArrayList<>();
    private final Fence fence = Fence.getInstance();
    private final PaintTank paintTank = PaintTank.getInstance(100);
    private final PaintSupplierThread paintSupplierThread = new PaintSupplierThread("P", (long)(Math.random() * 1000));

    public void initialize() {
        painterNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("threadName"));
        painterBucketLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("paintBucketLevel"));
        painterStateTableColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));
    }

    public void onStartButtonClicked(ActionEvent event) {
        start();
    }

    public void start() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Błąd");
        try {
            int paintersCount = getPaintersCount();
            int paintersBucketCapacity = getPaintersBucketCapacity();
            int paintTankCapacity = getPaintTankCapacity();
            int fenceRailsGroupsCount = getFenceRailsGroupsCount();
            int fenceRailsGroupSize = getFenceRailsGroupSize();

            startButton.setVisible(false);
            startButton.setDisable(true);

            paintTank.setCapacity(paintTankCapacity);
            fence.initializeFence(fenceRailsGroupsCount, fenceRailsGroupSize);

            paintSupplierThread.start();

            for (int i = 0; i < paintersCount; i++) {
                PainterThread painterThread = new PainterThread("" + (char)(i + (int)'a'), (long)(Math.random() * 1000), paintersBucketCapacity);
                painterThreadList.add(painterThread);
                painterThread.start();
            }

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refresh();
                if (painterThreadList.stream().allMatch(PainterThread::isFinished)) {
                    paintSupplierThread.setFinished(true);
                    cleanup();
                    break;
                }
            }
        } catch (NumberFormatException e) {
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void refresh() {
        paintTankLevel.setText("Stan zbiornika z farbą: " + paintTank);
        System.out.println(paintTank);
        System.out.println(fence);
        paintersTableView.getItems().clear();
        paintersTableView.getItems().addAll(painterThreadList);
        fenceListView.getItems().clear();
        fenceListView.getItems().addAll(fence.getFenceRailGroups().stream().map(FenceRailGroup::toString).toList());
    }

    public void cleanup() {
        painterThreadList.clear();
        startButton.setVisible(true);
        startButton.setDisable(false);
    }

    private int getFenceRailsGroupSize() {
        try {
            int n = Integer.parseInt(fenceRailsGroupSize.getText());
            if (n < 1) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Niepoprawna ilość sztachet w segmencie\noczekiwano: liczba całkowita dodatnia");
        }
    }

    private int getPaintTankCapacity() {
        try {
            int n = Integer.parseInt(paintTankCapacity.getText());
            if (n < 1) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Niepoprawna pojemność zbiornika z farbą\noczekiwano: liczba całkowita dodatnia");
        }
    }

    private int getFenceRailsGroupsCount() {
        try {
            int n = Integer.parseInt(fenceRailsGroupsCount.getText());
            if (n < 1) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Niepoprawna ilość segmentów płotu\noczekiwano: liczba całkowita dodatnia");
        }
    }

    private int getPaintersBucketCapacity() {
        try {
            int n = Integer.parseInt(paintersBucketCapacity.getText());
            if (n < 1) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Niepoprawna pojemność wiaderka z farbą pojedynczego malarza\noczekiwano: liczba całkowita dodatnia");
        }
    }

    private int getPaintersCount() {
        try {
            int n = Integer.parseInt(paintersCount.getText());
            if (n < 1 || n > 25) {
                throw new NumberFormatException();
            }
            return n;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Niepoprawna wartość ilość malarzy\noczekiwano: liczba całkowita dodatnia nie większa niż 25");
        }
    }
}