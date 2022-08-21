package com.github.krazune.esstudy.random.text;

import java.util.Random;

public class RandomGibberishGenerator
{
	public static String getGibberishSentence(int wordCount)
	{
		String sentence = "";

		if (wordCount <= 0)
		{
			return sentence;
		}

		for (int i = 0; i < wordCount; ++i)
		{
			String word = RandomWordGenerator.getWord();

			if (i == 0)
			{
				word = word.substring(0, 1).toUpperCase() + word.substring(1);
			}
			else
			{
				word = ' ' + word;
			}

			sentence += word;
		}

		return sentence + '.';
	}

	public static String getGibberish(int minimumWordPerSentenceCount, int maximumWordPerSentenceCount, int minimumSentenceCount, int maximumSentenceCount)
	{
		String gibberish = "";
		int sentenceCount = new Random().nextInt(minimumSentenceCount, maximumSentenceCount + 1);

		for (int i = 0; i < sentenceCount; ++i)
		{
			String sentence = getGibberishSentence(new Random().nextInt(minimumWordPerSentenceCount, maximumWordPerSentenceCount + 1));

			if (i > 0)
			{
				sentence = ' ' + sentence;
			}

			gibberish += sentence;
		}

		return gibberish;
	}
}
