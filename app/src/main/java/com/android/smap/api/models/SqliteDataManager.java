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
			
			Survey birdsSurvey = new Survey("Birds", "<h:html xmlns=\"http://www.w3.org/2002/xforms\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\">\n" +
                    "  <h:head>\n" +
                    "    <h:title>String only form</h:title>\n" +
                    "    <model>\n" +
                    "      <instance>\n" +
                    "        <data id=\"build_String-only-form_1406426192\">\n" +
                    "          <meta>\n" +
                    "            <instanceID/>\n" +
                    "          </meta>\n" +
                    "          <textFieldVanilla/>\n" +
                    "          <textFieldRequired/>\n" +
                    "          <textFieldLength/>\n" +
                    "        </data>\n" +
                    "      </instance>\n" +
                    "      <itext>\n" +
                    "        <translation lang=\"eng\">\n" +
                    "          <text id=\"/data/textFieldVanilla:label\">\n" +
                    "            <value>Text Field Vanilla</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldVanilla:hint\">\n" +
                    "            <value>no restrictions on this field</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:label\">\n" +
                    "            <value>Text Field Required</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:hint\">\n" +
                    "            <value>this is a required field</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:constraintMsg\">\n" +
                    "            <value>Text Field Required</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:label\">\n" +
                    "            <value>Text Field Length</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:hint\">\n" +
                    "            <value>Length &gt; 5 &amp;&amp; &lt; 10</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:constraintMsg\">\n" +
                    "            <value>Text Field Length</value>\n" +
                    "          </text>\n" +
                    "        </translation>\n" +
                    "      </itext>\n" +
                    "      <bind nodeset=\"/data/meta/instanceID\" type=\"string\" readonly=\"true()\" calculate=\"concat('uuid:', uuid())\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldVanilla\" type=\"string\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldRequired\" type=\"string\" required=\"true()\" jr:constraintMsg=\"jr:itext('/data/textFieldRequired:constraintMsg')\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldLength\" type=\"string\" constraint=\"(regex(., &quot;^.{5,10}$&quot;))\" jr:constraintMsg=\"jr:itext('/data/textFieldLength:constraintMsg')\"/>\n" +
                    "    </model>\n" +
                    "  </h:head>\n" +
                    "  <h:body>\n" +
                    "    <input ref=\"/data/textFieldVanilla\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldVanilla:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldVanilla:hint')\"/>\n" +
                    "    </input>\n" +
                    "    <input ref=\"/data/textFieldRequired\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldRequired:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldRequired:hint')\"/>\n" +
                    "    </input>\n" +
                    "    <input ref=\"/data/textFieldLength\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldLength:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldLength:hint')\"/>\n" +
                    "    </input>\n" +
                    "  </h:body>\n" +
                    "</h:html>\n");
            birdsSurvey.save();
            Distribution summerBirds = birdsSurvey.createDistribution("Summer Birds");

			Survey householdSurvey = new Survey("Household Survey", "<h:html xmlns=\"http://www.w3.org/2002/xforms\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:jr=\"http://openrosa.org/javarosa\">\n" +
                    "  <h:head>\n" +
                    "    <h:title>String only form</h:title>\n" +
                    "    <model>\n" +
                    "      <instance>\n" +
                    "        <data id=\"build_String-only-form_1406426192\">\n" +
                    "          <meta>\n" +
                    "            <instanceID/>\n" +
                    "          </meta>\n" +
                    "          <textFieldVanilla/>\n" +
                    "          <textFieldRequired/>\n" +
                    "          <textFieldLength/>\n" +
                    "        </data>\n" +
                    "      </instance>\n" +
                    "      <itext>\n" +
                    "        <translation lang=\"eng\">\n" +
                    "          <text id=\"/data/textFieldVanilla:label\">\n" +
                    "            <value>Text Field Vanilla</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldVanilla:hint\">\n" +
                    "            <value>no restrictions on this field</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:label\">\n" +
                    "            <value>Text Field Required</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:hint\">\n" +
                    "            <value>this is a required field</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldRequired:constraintMsg\">\n" +
                    "            <value>Text Field Required</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:label\">\n" +
                    "            <value>Text Field Length</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:hint\">\n" +
                    "            <value>Length &gt; 5 &amp;&amp; &lt; 10</value>\n" +
                    "          </text>\n" +
                    "          <text id=\"/data/textFieldLength:constraintMsg\">\n" +
                    "            <value>Text Field Length</value>\n" +
                    "          </text>\n" +
                    "        </translation>\n" +
                    "      </itext>\n" +
                    "      <bind nodeset=\"/data/meta/instanceID\" type=\"string\" readonly=\"true()\" calculate=\"concat('uuid:', uuid())\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldVanilla\" type=\"string\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldRequired\" type=\"string\" required=\"true()\" jr:constraintMsg=\"jr:itext('/data/textFieldRequired:constraintMsg')\"/>\n" +
                    "      <bind nodeset=\"/data/textFieldLength\" type=\"string\" constraint=\"(regex(., &quot;^.{5,10}$&quot;))\" jr:constraintMsg=\"jr:itext('/data/textFieldLength:constraintMsg')\"/>\n" +
                    "    </model>\n" +
                    "  </h:head>\n" +
                    "  <h:body>\n" +
                    "    <input ref=\"/data/textFieldVanilla\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldVanilla:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldVanilla:hint')\"/>\n" +
                    "    </input>\n" +
                    "    <input ref=\"/data/textFieldRequired\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldRequired:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldRequired:hint')\"/>\n" +
                    "    </input>\n" +
                    "    <input ref=\"/data/textFieldLength\">\n" +
                    "      <label ref=\"jr:itext('/data/textFieldLength:label')\"/>\n" +
                    "      <hint ref=\"jr:itext('/data/textFieldLength:hint')\"/>\n" +
                    "    </input>\n" +
                    "  </h:body>\n" +
                    "</h:html>\n");
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
