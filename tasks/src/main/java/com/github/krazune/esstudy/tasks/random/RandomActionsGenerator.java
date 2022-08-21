package com.github.krazune.esstudy.tasks.random;

import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.tasks.document.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomActionsGenerator
{
	private final static int MIN_WORDS = 3;
	private final static int MAX_WORDS = 13;
	private final static int MIN_SENTENCES = 1;
	private final static int MAX_SENTENCES = 2;
	private final static int MIN_ACTIONS = 1;
	private final static int MAX_ACTIONS = 5;

	public static Action getAction()
	{
		return new Action(
			RandomGibberishGenerator.getGibberish(
				MIN_WORDS,
				MAX_WORDS,
				MIN_SENTENCES,
				MAX_SENTENCES
			),
			new Random().nextBoolean()
		);
	}

	public static List<Action> getActions()
	{
		List<Action> actions = new LinkedList<>();
		int actionCount = new Random().nextInt(MIN_ACTIONS, MAX_ACTIONS + 1);

		for (int i = 0; i < actionCount; ++i)
		{
			actions.add(getAction());
		}

		return actions;
	}
}
