package com.android.smap.api.models;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.smap.models.TextMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "dialogues")
public class Dialogue extends Model {

    @Column(name = "distribution_id", onDelete = ForeignKeyAction.CASCADE)
    private Distribution distribution;

    @Column(name = "contact_id", onDelete = ForeignKeyAction.CASCADE)
    public Contact contact;

    /**
     * Serialized JavaRosa converser
     */
    @Column
    private String serializedState;

    @Column
    private String instanceXml;

    @Column
    private boolean completed;

    @Column
    private int questionNumber;

    @Column
    private boolean submitted;

    @Column
    private String startAt;

    // dummy fields for the moment
    public int answers;
    public int total;
    public String updatedAt;

    public Dialogue() {

    }

    public Dialogue(Distribution distribution, Contact contact) {
        this.distribution = distribution;
        this.contact = contact;
        this.completed = false;
        this.submitted = false;
    }

    public void logMessage(TextMessage message) {
        ActiveAndroid.beginTransaction();
        try {
            LogMessage newMessage = new LogMessage(this, message.text, message.number);
            newMessage.save();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static Dialogue findByDistributionAndContactIds(long distributionId, long contactId) {

        return new Select()
                .from(Dialogue.class)
                .where("distribution_id = ?", distributionId)
                .where("contact_id = ?", contactId)
                .executeSingle();
    }

    public List<LogMessage> getMessages() {
        return getMany(LogMessage.class, "dialogue_id");
    }

    public void markAsComplete() {
        try {
            completed = true;
            contact.setActiveDialogue(null);
            contact.save();
            addTimeToAnswer();
            Log.i("completed", this.getInstanceXml());
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean haveAnswered() {
        if (this.instanceXml == null || this.instanceXml.equals("") || this.questionNumber <= 1) {
            return false;
        } else {
            return true;
        }
    }

    public void saveData(String data, String answers, int questionNumber) {
        try {
            if (questionNumber == 1)
                setStartAt(new Date().toString());

            setQuestionNumber(questionNumber);
            setSerializedState(data);
            setInstanceXml(answers);
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getters and setters
    public String getFormXML() {
        return getSurvey().getFormXml();
    }

    public Survey getSurvey() {
        return distribution.getSurvey();
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getSerializedState() {
        return serializedState;
    }

    public void setSerializedState(String serializedState) {
        this.serializedState = serializedState;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getAnswers() {
        return answers;
    }

    public int getTotal() {
        return total;
    }

    public String getInstanceXml() {
        return instanceXml;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public void setInstanceXml(String instanceXml) {
        this.instanceXml = instanceXml;
    }

    public String getPhoneNumber() {
        return this.contact.getNumber();
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public static List<Dialogue> findAll() {
        return new Select().from(Dialogue.class).execute();
    }

    public static List<Dialogue> findAllCompletedDialogue() {
        List<Dialogue> dialogues = new ArrayList<Dialogue>();
        for (Dialogue dialogue : findAll()) {
            if (dialogue.isCompleted())
                dialogues.add(dialogue);
        }
        Collections.sort(dialogues, new Comparator<Dialogue>() {
            @Override
            public int compare(Dialogue dialogue, Dialogue dialogue2) {
                return dialogue.contact.getName().compareTo(dialogue2.contact.getName());
            }
        });
        return dialogues;
    }

    public void addTimeToAnswer() {
        String subStr1 = "<_start />";
        String subStr2 = "<_end />";
        String subStr3 = "<instanceID />";

        this.instanceXml = this.instanceXml.replaceFirst(subStr1, "<_start>" + this.startAt + "</_start>");
        this.instanceXml = this.instanceXml.replaceFirst(subStr2, "<_end>" + (new Date().toString()) + "</_end>");
        this.instanceXml = this.instanceXml.replaceFirst(subStr3, "<instanceID>uuid:" + String.valueOf(UUID.randomUUID()) + "</instanceID>");

    }
}
