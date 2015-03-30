package com.android.smap.api.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.android.smap.di.DataManager;
import com.android.smap.utils.IntRange;



public class SqliteDataManager implements DataManager {

	public SqliteDataManager() {

		if (getSurveys().isEmpty()) {
			seedDevData();
		}
	}

	@Override
	public List<Survey> getSurveys() {

		return Survey.findAll();
	}

    @Override
    public List<Distribution> getDistributions() {
        return Distribution.findAll();
    }

	@Override
	public Survey getSurvey(long id) {

		return Survey.findById((long) id);
	}

    @Override
    public Distribution getDistribution(long id) {

        return Distribution.findById((long) id);
    }

	@Override
	public void deleteSurveys(List<Survey> surveys) {
		
		for (Survey survey : surveys) {
			
			if(survey != null) {
				survey.delete();
			}
		}
	}
	
	@Override
	public void deleteSurvey(Survey survey) {
		
		if(survey != null) {
			survey.delete();
		}
	}

	@Override
	public List<Contact> getContacts() {

		return Contact.findAll();
	}

	@Override
	public void addContactsToDistribution(List<Contact> contacts, Distribution distribution) {
        distribution.addDialogues(contacts);
	}

	@Override
	public void removeContactFromDistribution(long contactId, long distributionId) {

		Dialogue dialogue = Dialogue.findByDistributionAndContactIds(
                distributionId, contactId);

		if (dialogue != null) {
			dialogue.delete();
		}
	}

    @Override
    public Contact findContactByPhoneNumber(String phoneNumber) {
        return Contact.findByPhoneNumber(phoneNumber);
    }

    private void seedDevData() {

		ActiveAndroid.beginTransaction();

		try {
			
			Survey birdsSurvey = new Survey("Birds", "parse xml content here");
            birdsSurvey.save();
            Distribution summerBirds = birdsSurvey.createDistribution("Summer Birds");

			Survey householdSurvey = new Survey("Household Survey", "parse xml content here");
            householdSurvey.save();
            Distribution householdDist = householdSurvey.createDistribution("Main Distribution");

			for (int n : IntRange.between(1, 10)) {
				Contact contact = new Contact("Contact " + n, "0123456789");
				contact.save();
				
				Distribution survey = (n % 2 == 0) ? summerBirds: householdDist;
				survey.addDialogue(contact);
			}

			ActiveAndroid.setTransactionSuccessful();
			
		} finally {

			ActiveAndroid.endTransaction();
		}
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}
}
