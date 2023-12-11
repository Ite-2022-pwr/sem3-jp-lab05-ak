module ite.jp.ak.lab05.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires ite.jp.ak.lab05.logic;


    opens ite.jp.ak.lab05.ui to javafx.fxml;
    exports ite.jp.ak.lab05.ui;
}