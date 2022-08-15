package com.github.krazune.esstudy.unstructureddocuments.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyInteger
{
	protected int myInteger;

	public MyInteger() {}

	public MyInteger(int myInteger)
	{
		this.myInteger = myInteger;
	}

	@JsonProperty("myInteger")
	public int get()
	{
		return myInteger;
	}

	public void set(int myInteger)
	{
		this.myInteger = myInteger;
	}
}
