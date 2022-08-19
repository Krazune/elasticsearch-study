package com.github.krazune.esstudy.unstructureddocuments.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyString
{
	private String myString;

	public MyString() {}

	public MyString(String myString)
	{
		this.myString = myString;
	}

	@JsonProperty("myString")
	public String get()
	{
		return myString;
	}

	public void set(String myString)
	{
		this.myString = myString;
	}
}
