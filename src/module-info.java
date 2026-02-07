module BLACKBOARD {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.compiler;
    requires java.desktop;
    requires javafx.base;
    requires com.google.gson;

    opens model to com.google.gson;

    exports ui.battleBOSS;
    exports ui.menu;
}