package com.android.smap.di;

import java.util.List;

import com.android.smap.api.models.Contact;
import com.android.smap.api.models.Distribution;
import com.android.smap.api.models.Survey;

public interface DataManager {

	List<Survey> getSurveys();

	Survey getSurvey(long id);

    Distribution getDistribution(long id);

	List<Contact> getContacts();

	void addContactsToDistribution(List<Contact> contacts, Distribution distribution);

	void removeContactFromDistribution(long contactId, long distributionId);

    List<Distribution> getDistributions();

	void deleteSurveys(List<Survey> surveys);

	void deleteSurvey(Survey survey);

    Contact findContactByPhoneNumber(String phoneNumber);
}
