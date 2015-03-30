package com.android.smap.api.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import android.telephony.PhoneNumberUtils;

@Table(name = "contacts")
public class Contact extends Model {

	@Column
	private String number;

	@Column
	private String name;

    @Column(name = "active_dialogue_id")
    private Dialogue activeDialogue;

	public Contact() {

	}

	public Contact(String name, String number) {
		this.name = name;
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Contact findById(Long id) {
		return Model.load(Contact.class, id);
	}

	public static List<Contact> findAll() {
		return new Select().from(Contact.class).execute();
	}

	public List<Dialogue> getSurveyContacts() {
		return getMany(Dialogue.class, "contact_id");
	}

    public static Contact findByPhoneNumber(String phoneNumber) {
        List<Contact> allContacts = findAll();
        for (Contact contact : allContacts) {
            //Compare phone numbers, check if they're identical enough for caller ID purposes.
            if(PhoneNumberUtils.compare(phoneNumber,contact.getNumber()))
                return contact;
        }
        return null;
    }

    public Dialogue getActiveDialogue() {
        return activeDialogue;
    }

    public void setActiveDialogue(Dialogue activeDialogue) {
        this.activeDialogue = activeDialogue;
    }

    public boolean isActive() {
        return activeDialogue != null;
    }
}
