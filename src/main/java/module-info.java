module com.semenbazanov.quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires codemodel;
    requires jsonschema2pojo.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.text;


    opens com.semenbazanov.quiz to javafx.fxml;
    opens com.semenbazanov.quiz.controllers to javafx.fxml;

    exports com.semenbazanov.quiz to javafx.graphics;
    exports com.semenbazanov.quiz.controllers to javafx.fxml;
    exports com.semenbazanov.quiz.model to com.fasterxml.jackson.databind;
}