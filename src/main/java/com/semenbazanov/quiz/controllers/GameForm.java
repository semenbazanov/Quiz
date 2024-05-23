package com.semenbazanov.quiz.controllers;

import com.semenbazanov.quiz.App;
import com.semenbazanov.quiz.model.Quiz;
import com.semenbazanov.quiz.model.Result;
import com.semenbazanov.quiz.repository.ResultRepository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

public class GameForm implements ControllerData<Quiz> {
    /**
     * Реализовать форму (Game Form), представляющую собой игровое меню.
     * Оно должно содержать следующие атрибуты:
     * Панель TabPane с вкладками - вопросами викторины.
     * Каждый вопрос викторины представляет собой объект Label,
     * а варианты ответов - RadioButton.
     */
    @FXML
    public TabPane tabPane;
    private List<Result> results = new ArrayList<Result>();

    private HashMap<Integer, String> ans = new HashMap<>();

    @Override
    public void initData(Quiz value) {
        this.results = value.getResults();

        for (int i = 0; i < this.results.size(); i++) {
            Tab tab = new Tab("Q" + (i + 1));
            this.tabPane.getTabs().add(tab);

            VBox vBox = new VBox();

            vBox.getChildren().add(new Label(this.results.get(i).getQuestion()));

            ToggleGroup toggleGroup = new ToggleGroup();
            ArrayList<String> answers = new ArrayList<>(this.results.get(i).getIncorrectAnswers());
            answers.add(this.results.get(i).getCorrectAnswer());
            //TODO накопить правильные и неправильные ответы в список
            Collections.shuffle(answers);
            for (int j = 0; j < answers.size(); j++) {
                RadioButton radioButton = new RadioButton(answers.get(j));
                radioButton.setToggleGroup(toggleGroup);
                int finalI = i;
                int finalJ = j;
                radioButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        //TODO сохоанить номер вкладки и выбранный ответ в словпоь
                        ans.put(finalI, answers.get(finalJ));
                    }
                });
                vBox.getChildren().add(radioButton);
            }

            tab.setContent(vBox);
        }

        Tab tabResult = new Tab("Result");
        this.tabPane.getTabs().add(tabResult);

        VBox vBox = new VBox();
        /**
         * Панель должна содержать объект Button с названием “Check”,
         * при нажатии которого: Проверяется наличие ответов на все вопросы.
         * Если не на все вопросы были даны ответы,
         * то должно отображаться AlertMessage с соответствующим сообщением.
         */
        Button buttonCheck = new Button("Check");
        Button buttonSave = new Button("Save");
        buttonSave.setVisible(false);
        StringBuilder stringBuilder = new StringBuilder();
        buttonCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (ans.keySet().size() != results.size()) {
                    App.showAlert("Error", "Даны ответы не на все вопросы", Alert.AlertType.ERROR);
                } else {
                    int correct = 0;
                    int incorrect = 0;
                    vBox.getChildren().add(new Label("Statistics:"));
                    stringBuilder.append("Statistics:");
                    stringBuilder.append("\n");
                    Preferences preferences = Preferences.userRoot();
                    boolean checkbox_is_selected = preferences.getBoolean("checkbox", false);
                    if (checkbox_is_selected == true){
                        for (int i = 0; i < results.size(); i++) {
                            if (ans.get(i).equals(results.get(i).getCorrectAnswer())){
                                vBox.getChildren().add(new Label("Q " + (i + 1) + "+ " + results.get(i).getCorrectAnswer()));
                                correct++;
                                stringBuilder.append("Q " + (i + 1) + "+ " + results.get(i).getCorrectAnswer());
                                stringBuilder.append("\n");
                            }
                            else {
                                vBox.getChildren().add(new Label("Q " + (i + 1) + "- " + results.get(i).getCorrectAnswer()));
                                incorrect++;
                                stringBuilder.append("Q " + (i + 1) + "- " + results.get(i).getCorrectAnswer());
                                stringBuilder.append("\n");
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < results.size(); i++) {
                            if (ans.get(i).equals(results.get(i).getCorrectAnswer())) {
                                vBox.getChildren().add(new Label("Q " + (i + 1) + "+"));
                                correct++;
                                stringBuilder.append("Q " + (i + 1) + "+");
                                stringBuilder.append("\n");
                            } else {
                                vBox.getChildren().add(new Label("Q " + (i + 1) + "-"));
                                incorrect++;
                                stringBuilder.append("Q " + (i + 1) + "-");
                                stringBuilder.append("\n");
                            }
                        }
                    }
                    vBox.getChildren().add(new Label("Correct/Incorrect " + correct + "/" + incorrect));
                    stringBuilder.append("Correct/Incorrect " + correct + "/" + incorrect);
                    stringBuilder.append("\n");

                    vBox.getChildren().add(new Label("Correct Answer Rate " + (double) correct / (correct + incorrect) * 100 + "%"));
                    stringBuilder.append("Correct Answer Rate " + (double) correct / (correct + incorrect) * 100 + "%");
                    stringBuilder.append("\n");
                    buttonSave.setVisible(true);
                }
            }
        });
        vBox.getChildren().add(buttonCheck);

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter1 = new FileChooser.ExtensionFilter("TXT (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extensionFilter1);

                File file = fileChooser.showSaveDialog(null);
                String s = stringBuilder.toString();
                if (!s.isEmpty()){
                    try {
                        ResultRepository resultRepository = new ResultRepository(s);
                        resultRepository.save(file);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        vBox.getChildren().add(buttonSave);
        tabResult.setContent(vBox);
    }
}
