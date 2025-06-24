module com.cretaairlines {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires jdk.jshell;
    requires jdk.compiler;

    opens com.cretaairlines to javafx.fxml;
    exports com.cretaairlines;
    exports controller;
    opens controller to javafx.fxml;
    opens model to com.fasterxml.jackson.databind, javafx.fxml, javafx.base;
    exports model.tda to com.fasterxml.jackson.databind;
    exports model.datamanagment;
    opens model.datamanagment to com.fasterxml.jackson.databind;
    exports model.serializers to com.fasterxml.jackson.databind;
    opens controller.flightradar;
    exports controller.flightradar to javafx.fxml;
    exports controller.user;
    opens controller.user to javafx.fxml;

}