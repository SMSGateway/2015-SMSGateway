package com.android.smap.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;

import com.android.smap.GatewayApp;
import com.android.smap.api.models.FormList;
import com.android.smap.api.models.FormList.Form;
import com.android.smap.api.requests.FormListRequest;
import com.android.smap.api.requests.SmapRawRequest;
import com.android.smap.api.requests.SurveyDefinitionRequest;

public class FormListController extends
		RawRequestController<FormList> {

	private static final String	XML_NAME	= "name";
	private static final String	XML_URL		= "downloadUrl";

	private FormList			mModel;

	public FormListController(Context context,
			ControllerListener listener,
			ControllerErrorListener errorListener) {
		super(context, listener, errorListener);
		mListener = listener;
		mErrorListener = errorListener;
		mModel = new FormList();
	}

	@Override
	protected SmapRawRequest getRequest() {
		return new FormListRequest(this, this);
	}

	@Override
	protected FormList addResponseToDatabase(String rawXML) {

		mModel.setForms(valuesFromTag(rawXML));
		return mModel;
	}

	private List<Form> valuesFromTag(String xml) {

		ArrayList<Form> list = new ArrayList<Form>();

		try {
			Document doc = loadXMLFromString(xml);
			NodeList nodeNameList = doc.getElementsByTagName(XML_NAME);
			NodeList nodeUrlList = doc.getElementsByTagName(XML_URL);
			for (int i = 0; i < nodeNameList.getLength(); i++) {
				String name = nodeNameList.item(i).getTextContent();
				String url = nodeUrlList.item(i).getTextContent();
				Form form = new Form(name, url);
				list.add(form);
			}
			return list;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Document loadXMLFromString(String xml) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

}
