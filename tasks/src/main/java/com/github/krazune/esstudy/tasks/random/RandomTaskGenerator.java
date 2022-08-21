package com.github.krazune.esstudy.tasks.random;

import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.tasks.document.Task;

public class RandomTaskGenerator
{
	private final static int MIN_WORDS = 3;
	private final static int MAX_WORDS = 13;
	private final static int MIN_SENTENCES = 1;
	private final static int MAX_SENTENCES = 2;

	public static Task getTask()
	{
		return new Task(
			RandomGibberishGenerator.getGibberish(MIN_WORDS, MAX_WORDS, MIN_SENTENCES, MAX_SENTENCES),
			RandomActionsGenerator.getActions()
		);
	}
}
