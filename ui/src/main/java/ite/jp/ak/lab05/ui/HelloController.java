package ite.jp.ak.lab05.ui;

import ite.jp.ak.lab05.shared.Fence;
import ite.jp.ak.lab05.shared.FenceRailGroup;
import ite.jp.ak.lab05.shared.PaintTank;
import ite.jp.ak.lab05.threads.PaintSupplierThread;
import ite.jp.ak.lab05.threads.PainterThread;
import ite.jp.ak.lab05.threads.Triggerable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class HelloController implements Triggerable {

    // Pola tekstowe parametrów
    @FXML protected TextField paintersCount;
    @FXML protected TextField paintersBucketCapacity;
    @FXML protected TextField paintTankCapacity;
    @FXML protected TextField fenceRailsGroupsCount;
    @FXML protected TextField fenceRailsGroupSize;

    // Stan zbiornika z farbą
    @FXML protected Label paintTankLevel;

    // Status symulacji
    @FXML protected Label simulationStatusLabel;

    // Tabela malarzy
    @FXML protected TableView<PainterThread> paintersTableView;
    @FXML protected TableColumn<PainterThread, String> painterNameTableColumn;
    @FXML protected TableColumn<PainterThread, Integer> painterBucketLevelTableColumn;
    @FXML protected TableColumn<PainterThread, Boolean> painterStateTableColumn;

    // Stan płotu
    @FXML protected ListView<Label> fenceListView;

    // Przyciski
    @FXML protected Button startButton;

    // Obiekty symulacji
    private final List<PainterThread> painterThreadList = new ArrayList<>();
    private final Fence fence = Fence.getInstance();
    private final PaintTank paintTank = PaintTank.getInstance(100);
    private PaintSupplierThread paintSupplierThread;

    public void initialize() {
        painterNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("threadName"));
        painterBucketLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("paintBucketLevel"));
        painterStateTableColumn.setCellValueFactory(new PropertyValueFactory<>("finished"));
        simulationStatusLabel.setText("Status symulacji: nierozpoczęta");
    }

    public void onStartButtonClicked(ActionEvent event) {
        start();
    }

    public void start() {
        simulationStatusLabel.setText("Status symulacji: nierozpoczęta");
        painterThreadList.clear();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Błąd");
        try {
            int paintersCount = getPaintersCount();
            int paintersBucketCapacity = getPaintersBucketCapacity();
            int paintTankCapacity = getPaintTankCapacity();
            int fenceRailsGroupsCount = getFenceRailsGroupsCount();
            int fenceRailsGroupSize = getFenceRailsGroupSize();

            if (paintersBucketCapacity > paintTankCapacity) {
                throw new NumberFormatException("Pojemność zbiornika z farbą powinna być większa od wiaderka z farbą pojedynczego malarza");
            }

            startButton.setVisible(false);
            startButton.setDisable(true);

            simulationStatusLabel.setText("Status symulacji: w trakcie");

            paintTank.setCapacity(paintTankCapacity);
            fence.initializeFence(fenceRailsGroupsCount, fenceRailsGroupSize);

            this.paintSupplierThread = new PaintSupplierThread("P", (long)(Math.random() * 1000), this);
            paintSupplierThread.start();

            for (int i = 0; i < paintersCount; i++) {
                PainterThread painterThread = new PainterThread("" + (char)(i + (int)'a'), (long)(Math.random() * 1000), paintersBucketCapacity, this);
                painterThreadList.add(painterThread);
                painterThread.start();
            }

        } catch (NumberFormatException e) {
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void refresh() {
        if (paintSupplierThread.isFinished()) {
            return;
        }
        String paintTankStr = paintTank.toString();
        String fenceStr = fence.toString();
        List<String> railGroups = fence.getFenceRailGroups().stream().map(FenceRailGroup::toString).toList();
        paintTankLevel.setText("Stan zbiornika z farbą: " + paintTankStr);
        System.out.println(paintTankStr);
        System.out.println(fenceStr);
        paintersTableView.getItems().clear();
        paintersTableView.getItems().addAll(painterThreadList);
        fenceListView.getItems().clear();

        for (var railGroup : railGroups) {
            var tempLabel = new Label(railGroup);
            tempLabel.setFont(Font.font(24));
            fenceListView.getItems().add(tempLabel);
        }
        if (painterThreadList.stream().allMatch(PainterThread::isFinished)) {
            paintSupplierThread.setFinished(true);
            cleanup();
        }
    }

    public void cleanup() {
        startButton.setVisible(true);
        startButton.setDisable(false);
        simulationStatusLabel.setText("Status symulacji: zakończona");
    }

    private int getFenceRailsGroupSize() throws NumberFormatException {
        int n = Integer.parseInt(fenceRailsGroupSize.getText());
        if (n < 1) {
            throw new NumberFormatException("Niepoprawna ilość sztachet w segmencie\noczekiwano: liczba całkowita dodatnia");
        }
        return n;
    }

    private int getPaintTankCapacity() throws NumberFormatException {
        int n = Integer.parseInt(paintTankCapacity.getText());
        if (n < 1) {
            throw new NumberFormatException("Niepoprawna pojemność zbiornika z farbą\noczekiwano: liczba całkowita dodatnia");
        }
        return n;
    }

    private int getFenceRailsGroupsCount() throws NumberFormatException {
        int n = Integer.parseInt(fenceRailsGroupsCount.getText());
        if (n < 1) {
            throw new NumberFormatException("Niepoprawna wartość ilość malarzy\noczekiwano: liczba całkowita dodatnia nie większa niż 25");
        }
        return n;
    }

    private int getPaintersBucketCapacity() throws NumberFormatException {
        int n = Integer.parseInt(paintersBucketCapacity.getText());
        if (n < 1) {
            throw new NumberFormatException("Niepoprawna pojemność wiaderka z farbą pojedynczego malarza\noczekiwano: liczba całkowita dodatnia");
        }
        return n;
    }

    private int getPaintersCount() throws NumberFormatException {
        int n = Integer.parseInt(paintersCount.getText());
        if (n < 1 || n > 25) {
            throw new NumberFormatException("Niepoprawna wartość ilość malarzy\noczekiwano: liczba całkowita dodatnia nie większa niż 25");
        }
        return n;
    }

    public void trigger() {
        Platform.runLater(this::refresh);
    }
}