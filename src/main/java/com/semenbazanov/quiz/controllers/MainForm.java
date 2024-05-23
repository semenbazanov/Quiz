package com.semenbazanov.quiz.controllers;

import com.semenbazanov.quiz.App;
import com.semenbazanov.quiz.repository.QuizRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainForm {

    public CheckBox checkBoxAnswer;

    public void initialize(){
        Preferences preferences = Preferences.userRoot();
        this.checkBoxAnswer.setSelected(preferences.getBoolean("checkbox", false));
    }

    /**
     * Объект Button с Label “From Internet”. При нажатии кнопки
     * должна запускаться Loading Form (см. пункт 2),
     * реализующая загрузку викторины из интернета.
     */
    public void toLoadForm(ActionEvent actionEvent) {
        try {
            App.openWindow("loadingForm.fxml", "Loading Form", null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Объект Button с Label “From File”. При нажатии кнопки
     * викторина должна будет загружаться из .json или .csv файла.
     * Для этого необходимо использовать объект класса FileChooser.ExtensionFilter.
     */
    public void toLoadFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Preferences preferences = Preferences.userRoot();
        fileChooser.setInitialDirectory(new File(preferences.get("key", "C:\\Users\\sa178")));

        FileChooser.ExtensionFilter extensionFilter1 = new FileChooser.ExtensionFilter
                ("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter1);
        FileChooser.ExtensionFilter extensionFilter2 = new FileChooser.ExtensionFilter
                ("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter2);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            preferences.put("key", file.getParent());
            QuizRepository quizRepository = new QuizRepository(file);
            try {
                App.openWindow("gameForm.fxml", "Game form", quizRepository.getQuiz());
            } catch (IOException e) {
                App.showAlert("Error", "Ошибка обращения к серверу", Alert.AlertType.ERROR);
            }
        }
    }

    public void toCheck(ActionEvent actionEvent) {
        Preferences preferences = Preferences.userRoot();
        preferences.putBoolean("checkbox", this.checkBoxAnswer.isSelected());
    }
}
