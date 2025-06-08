module com.cretaairlines {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cretaairlines to javafx.fxml;
    exports com.cretaairlines;
}