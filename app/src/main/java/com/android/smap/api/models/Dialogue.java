package com.android.smap.api.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.android.smap.models.TextMessage;

import java.util.List;

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

	// dummy fields for the moment
	public int		answers;
	public int		total;
	public String	updatedAt;

	public Dialogue() {

	}

    public Dialogue(Distribution distribution, Contact contact) {
        this.distribution = distribution;
        this.contact = contact;
        this.completed = false;
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
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveData(String data, String answers, int questionNumber) {
        try {
            setSerializedState(data);
            setInstanceXml(answers);
            setQuestionNumber(questionNumber);
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
}
