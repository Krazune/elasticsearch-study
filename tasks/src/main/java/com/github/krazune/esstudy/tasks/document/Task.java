package com.github.krazune.esstudy.tasks.document;

import java.util.List;

public class Task
{
	private String goal;
	private List<Action> actions;

	public Task() {}

	public Task(String goal, List<Action> actions)
	{
		this.goal = goal;
		this.actions = actions;
	}

	public String getGoal()
	{
		return goal;
	}

	public void setGoal(String goal)
	{
		this.goal = goal;
	}

	public List<Action> getActions()
	{
		return actions;
	}

	public void setActions(List<Action> actions)
	{
		this.actions = actions;
	}

	@Override
	public String toString()
	{
		String taskString = "Goal: " + goal + '\n';

		for (int i = 0; i < actions.size(); ++i)
		{
			taskString += actions.get(i).toString() + '\n';
		}

		return taskString;
	}
}
