module oc.cryptography.notepad2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens oc.cryptography.notepad2 to javafx.fxml;
    exports oc.cryptography.notepad2;
}