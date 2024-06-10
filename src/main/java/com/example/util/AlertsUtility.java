package com.example.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertsUtility {
    
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Ошибка");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
