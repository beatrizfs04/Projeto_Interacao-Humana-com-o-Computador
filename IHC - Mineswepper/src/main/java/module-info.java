module ihc_mineswepper {
    requires javafx.controls;
    requires javafx.fxml;


    opens ihc_mineswepper to javafx.fxml;
    exports ihc_mineswepper;
}