package com.semenbazanov.quiz.repository;

import java.io.*;

public class ResultRepository {
    private String res;

    public ResultRepository(String res) {
        this.res = res;
    }

    public ResultRepository(File file) throws IOException {
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            byte[] bytes = bufferedInputStream.readAllBytes();
            this.res = new String(bytes);
        }
    }

    public void save(File file) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){
            bufferedWriter.write(this.res);
        }
    }

    @Override
    public String toString() {
        return "ResultRepository{" +
                "res='" + res + '\'' +
                '}';
    }
}
