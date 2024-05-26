package com.semenbazanov.quiz.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenbazanov.quiz.model.Quiz;
import com.semenbazanov.quiz.model.Result;
import com.semenbazanov.util.XOREncoder;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class QuizRepository {
    private Quiz quiz;

    private ObjectMapper objectMapper = new ObjectMapper();

    public QuizRepository(File file) throws IOException {
        this.quiz = this.objectMapper.readValue(new File(String.valueOf(file)), Quiz.class);

        for (Result result : this.quiz.getResults()){
            result.setCorrectAnswer(XOREncoder.encrypt(result.getCorrectAnswer(), "tel"));
            ArrayList<String> incorrectAnswers = new ArrayList<>();
            for (String str : result.getIncorrectAnswers()){
                incorrectAnswers.add(XOREncoder.encrypt(str, "tel"));
            }
            result.setIncorrectAnswers(incorrectAnswers);
        }
    }

    public QuizRepository(String siteUrl) throws IOException {
        URL url = new URL(siteUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream())) {
            this.quiz = this.objectMapper.readValue(bufferedInputStream, Quiz.class);

            for (Result result : this.quiz.getResults()) {
                result.setQuestion(StringEscapeUtils.unescapeHtml4(result.getQuestion()));
                result.setCorrectAnswer(StringEscapeUtils.unescapeHtml4(result.getCorrectAnswer()));
                ArrayList<String> incorrectAnswers = new ArrayList<>();
                for (String s : result.getIncorrectAnswers()) {
                    incorrectAnswers.add(StringEscapeUtils.unescapeHtml4(s));
                }
                result.setIncorrectAnswers(incorrectAnswers);
            }
        }
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void save(File file) throws IOException {
        for (Result result : this.quiz.getResults()){
            result.setCorrectAnswer(XOREncoder.encrypt(result.getCorrectAnswer(), "tel"));
            ArrayList<String> res = new ArrayList<>();
            for (String str : result.getIncorrectAnswers()){
                 res.add(XOREncoder.encrypt(str, "tel"));
            }
            result.setIncorrectAnswers(res);
        }

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            this.objectMapper.writeValue(bufferedOutputStream, this.quiz);
        }
    }
}
