package com.example;

import com.example.control.ControllerFacade;
import com.example.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.model.GameBoard;

public class Main extends Application {

    private ControllerFacade controller;
    private MainView mainView;

    @Override
    public void start(Stage primaryStage) {
        mainView = new MainView();
        mainView.start(primaryStage);  // Инициализация UI через MainView

        controller = new ControllerFacade(mainView);

        Scene scene = primaryStage.getScene();
        scene.setOnMousePressed(event -> {
            if (mainView.getPrimaryStage().getScene().getRoot() instanceof GameBoard) {
                ((GameBoard) mainView.getPrimaryStage().getScene().getRoot()).startDrag(event);
            }
        });
        scene.setOnMouseDragged(event -> {
            if (mainView.getPrimaryStage().getScene().getRoot() instanceof GameBoard) {
                ((GameBoard) mainView.getPrimaryStage().getScene().getRoot()).drag(event);
            }
        });
        scene.setOnScroll(event -> {
            if (mainView.getPrimaryStage().getScene().getRoot() instanceof GameBoard) {
                ((GameBoard) mainView.getPrimaryStage().getScene().getRoot()).zoom(event);
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
