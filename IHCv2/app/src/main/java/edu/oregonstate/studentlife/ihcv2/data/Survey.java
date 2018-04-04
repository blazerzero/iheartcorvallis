package edu.oregonstate.studentlife.ihcv2.data;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Omeed on 4/3/18.
 */

public class Survey extends AppCompatActivity implements Serializable {

    private String question;
    private ArrayList<String> choices;
    private String response;

    public Survey(String question, ArrayList<String> choices) {
        this.question = question;
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

}
