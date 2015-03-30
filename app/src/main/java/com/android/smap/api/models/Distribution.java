package com.android.smap.api.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Table(name = "distributions")
public class Distribution extends Model {

    @Column
    private String name;

    @Column(name = "survey_id", onDelete = Column.ForeignKeyAction.CASCADE)
    private Survey survey;

    public Distribution() {

    }

    public Distribution(Survey survey, String name) {
        this.survey = survey;
        this.name = name;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<Dialogue> getDialogues() {
        List<Dialogue> dialogues = getMany(Dialogue.class, "distribution_id");
        Collections.sort(dialogues,new Comparator<Dialogue>() {
            @Override
            public int compare(Dialogue dialogue, Dialogue dialogue2) {
                return dialogue.contact.getName().compareTo(dialogue2.contact.getName());
            }
        });
        return dialogues;
    }

    public void addDialogue(Contact contact) {
        ActiveAndroid.beginTransaction();
        try {
            new Dialogue(this, contact).save();

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void addDialogues(List<Contact> contacts) {
        ActiveAndroid.beginTransaction();
        try {

            for (Contact contact : contacts) {
                new Dialogue(this, contact).save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public int getMembersCount() {
        return getDialogues().size();
    }

    public int getCompletedCount() {
        List <Dialogue> dialogueList = getDialogues();

        int completedCount = 0;

        for (Dialogue dialogue : dialogueList) {
            if(dialogue.isCompleted())
                completedCount++;
        }

        return completedCount;
    }

    public int getPartialCount() {
        // TODO delegate this to the distributions
        return 0;
    }

    public float getCompletionPercentage() {
    /* Completion percentage of total answered questions
        List <Dialogue> dialogueList = getDialogues();

        int totalAnswered = 0;
        int totalQuestion = 0;

        for (Dialogue dialogue : dialogueList) {
            totalAnswered += dialogue.getQuestionNumber();
            totalQuestion += dialogue.getTotal();
        }

        return ((float) totalAnswered / totalQuestion) * 100f;
     */
     //Completion percentage of total completed
       List <Dialogue> dialogueList = getDialogues();

       int totalComplete = 0;
       for (Dialogue dialogue : dialogueList) {
           if(dialogue.isCompleted())
               totalComplete ++;
       }

       return ((float) totalComplete / dialogueList.size()) * 100f;

    }

    public static Distribution findById(Long id) {
        return Model.load(Distribution.class, id);
    }

    public static List<Distribution> findAll() {
        return new Select().from(Distribution.class).execute();
    }

    public static List<Distribution> findBySurvey(Survey survey) {
        return survey.getDistributions();
    }
}
