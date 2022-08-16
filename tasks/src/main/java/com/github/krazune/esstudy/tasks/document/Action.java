package com.github.krazune.esstudy.tasks.document;

public class Action
{
	protected String description;
	protected boolean isComplete;

	public Action() {}

	public Action(String description, boolean isComplete)
	{
		this.description = description;
		this.isComplete = isComplete;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isComplete()
	{
		return isComplete;
	}

	public void setComplete(boolean complete)
	{
		isComplete = complete;
	}
}
