package com.github.krazune.esstudy.unstructureddocuments.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyFloat
{
	protected float myFloat;

	public MyFloat() {}

	public MyFloat(float myFloat)
	{
		this.myFloat = myFloat;
	}

	@JsonProperty("myFloat")
	public float get()
	{
		return myFloat;
	}

	public void set(float myFloat)
	{
		this.myFloat = myFloat;
	}
}
