package com.semenbazanov.quiz.controllers;

import com.semenbazanov.quiz.App;
import com.semenbazanov.quiz.repository.QuizRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class LoadingForm {
    public TextField numberOfQuestions;
    public ComboBox<String> categoryBox;
    public ComboBox<String> difficultyBox;

    private HashMap<String, Integer> categories = new HashMap<>();

    @FXML
    public void initialize() {
        categories.put("Sport", 21);
        categories.put("Geography", 22);
        categories.put("History", 23);
        categories.put("Art", 25);
        this.categoryBox.setItems(FXCollections.observableList(new ArrayList<>(this.categories.keySet())));
        this.difficultyBox.getItems().add("easy");
        this.difficultyBox.getItems().add("medium");
        this.difficultyBox.getItems().add("hard");
    }

    /**
     * Объект Button с Label “Save”, при нажатии которого
     * должен генерироваться API-запрос на страницу https://opentdb.com/api_config.php,
     * где будет создаваться викторина с выбранными ранее параметрами.
     * Далее должно открываться диалоговое окно (File Chooser)
     * для сохранения сгенерированной викторины
     * в одном из двух возможных форматов - .json или .csv.
     * Объекты сначала преобразуются парсером Jackson в объекты Java
     */
    public void toSave(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Preferences preferences = Preferences.userRoot();
        fileChooser.setInitialDirectory(new File(preferences.get("saveKey", "C:\\Users\\sa178")));

        FileChooser.ExtensionFilter extensionFilter1 = new FileChooser.ExtensionFilter("JSON (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter1);
        FileChooser.ExtensionFilter extensionFilter2 = new FileChooser.ExtensionFilter("CSV (*csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter2);

        if (this.numberOfQuestions.getText().isEmpty()){
            App.showAlert("Error", "Не выбрано кол-во вопросов", Alert.AlertType.ERROR);
            return;
        }
        int count = Integer.parseInt(this.numberOfQuestions.getText());
        if (count == 0) {
            App.showAlert("Error", "Не выбрано кол-во вопросов", Alert.AlertType.ERROR);
            return;
        }
        else if(count > 10){
            App.showAlert("Error", "Кол-во вопросов должно быть меньше 10", Alert.AlertType.ERROR);
            return;
        }
        String type = this.categoryBox.getSelectionModel().getSelectedItem();
        if (type.isEmpty()) {
            App.showAlert("Error", "Не выбрана категория вопросов", Alert.AlertType.ERROR);
            return;
        }
        int typeNumb = this.categories.getOrDefault(type, -1);
        if (typeNumb == -1) {
            App.showAlert("Error", "Не выбрана категория вопросов", Alert.AlertType.ERROR);
            return;
        }
        String difficulty = this.difficultyBox.getSelectionModel().getSelectedItem();
        if (difficulty.isEmpty()) {
            App.showAlert("Error", "Не выбрана сложность", Alert.AlertType.ERROR);
            return;
        }
        String link = "https://opentdb.com/api.php?amount=" + count + "&category=" + typeNumb + "&difficulty=" + difficulty;

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            preferences.put("saveKey", file.getParent());
            QuizRepository quizRepository = new QuizRepository(link);
            quizRepository.save(file);
            App.showAlert("Success", "Файл успешно сохранен", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Объект Button с Label “Start”, при нажатии кнопки должна открываться Game Form,
     * которая берет данные с API.  Форма не должна открываться,
     * если какие-либо параметры викторины выбраны неверно или не выбраны.
     * Вместо этого должно отображаться AlertMessage.
     */
    public void toStart(ActionEvent actionEvent) {
        if (this.numberOfQuestions.getText().isEmpty()){
            App.showAlert("Error", "Не выбрано кол-во вопросов", Alert.AlertType.ERROR);
            return;
        }
        int count;
        try {
           count = Integer.parseInt(this.numberOfQuestions.getText());
        } catch (NumberFormatException e) {
            App.showAlert("Error", "Введите число", Alert.AlertType.ERROR);
            return;
        }
        if (count == 0) {
            App.showAlert("Error", "Не выбрано кол-во вопросов", Alert.AlertType.ERROR);
            return;
        }
        else if(count > 10){
            App.showAlert("Error", "Кол-во вопросов должно быть меньше 10", Alert.AlertType.ERROR);
            return;
        }
        String type = this.categoryBox.getSelectionModel().getSelectedItem();
        if (type.isEmpty()) {
            App.showAlert("Error", "Не выбрана категория вопросов", Alert.AlertType.ERROR);
            return;
        }
        int typeNumb = this.categories.getOrDefault(type, -1);
        if (typeNumb == -1) {
            App.showAlert("Error", "Не выбрана категория вопросов", Alert.AlertType.ERROR);
            return;
        }
        String difficulty = this.difficultyBox.getSelectionModel().getSelectedItem();
        if (difficulty.isEmpty()) {
            App.showAlert("Error", "Не выбрана сложность", Alert.AlertType.ERROR);
            return;
        }
        String link = "https://opentdb.com/api.php?amount=" + count + "&category=" + typeNumb + "&difficulty=" + difficulty;
        try {
            QuizRepository quizRepository = new QuizRepository(link);
            App.openWindow("gameForm.fxml", "Game Form", quizRepository.getQuiz());
        } catch (Exception e) {
            App.showAlert("Error", "Ошибка обращения к серверу", Alert.AlertType.ERROR);
        }
    }
}

