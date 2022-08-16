package com.github.krazune.esstudy.tasks.random;

import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.tasks.document.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomActionsGenerator
{
	protected final static int MINIMUM_WORDS = 3;
	protected final static int MAXIMUM_WORDS = 13;
	protected final static int MINIMUM_SENTENCES = 1;
	protected final static int MAXIMUM_SENTENCES = 2;
	protected final static int MINIMUM_ACTIONS = 1;
	protected final static int MAXIMUM_ACTIONS = 5;

	public static Action getAction()
	{
		return new Action(
			RandomGibberishGenerator.getGibberish(MINIMUM_WORDS, MAXIMUM_WORDS, MINIMUM_SENTENCES, MAXIMUM_SENTENCES),
			new Random().nextBoolean()
		);
	}

	public static List<Action> getActions()
	{
		List<Action> actions = new LinkedList<>();
		int actionCount = new Random().nextInt(MINIMUM_ACTIONS, MAXIMUM_ACTIONS + 1);

		for (int i = 0; i < actionCount; ++i)
		{
			actions.add(getAction());
		}

		return actions;
	}
}
