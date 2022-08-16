package com.github.krazune.esstudy.tasks.random;

import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.tasks.document.Task;

public class RandomTaskGenerator
{
	protected final static int MINIMUM_WORDS = 3;
	protected final static int MAXIMUM_WORDS = 13;
	protected final static int MINIMUM_SENTENCES = 1;
	protected final static int MAXIMUM_SENTENCES = 2;

	public static Task getTask()
	{
		return new Task(
			RandomGibberishGenerator.getGibberish(MINIMUM_WORDS, MAXIMUM_WORDS, MINIMUM_SENTENCES, MAXIMUM_SENTENCES),
			RandomActionsGenerator.getActions()
		);
	}
}
