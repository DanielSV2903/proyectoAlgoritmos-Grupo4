module com.cretaairlines {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.cretaairlines to javafx.fxml;
    exports com.cretaairlines;
    exports controller;
    opens controller to javafx.fxml;
    opens model to com.fasterxml.jackson.databind;
    exports model;
    exports model.datamanagment;
    opens model.datamanagment to com.fasterxml.jackson.databind;

}