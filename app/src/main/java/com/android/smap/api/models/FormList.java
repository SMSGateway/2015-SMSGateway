package com.android.smap.api.models;

import java.util.List;

import com.activeandroid.Model;

public class FormList extends Model {

	private List<Form>	forms;

	public static class Form {

		private String	name;
		private String	url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Form(String name, String url) {
			this.setName(name);
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public List<Form> getForms() {
		return forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}

}
