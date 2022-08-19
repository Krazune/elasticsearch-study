package com.github.krazune.esstudy.quotes.random;

import com.github.krazune.esstudy.quotes.document.Quote;
import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.random.text.RandomNameGenerator;

public class RandomQuoteGenerator
{
	private final static int MINIMUM_WORDS = 3;
	private final static int MAXIMUM_WORDS = 13;
	private final static int MINIMUM_SENTENCES = 1;
	private final static int MAXIMUM_SENTENCES = 2;

	public static Quote getQuote()
	{
		return new Quote(
			RandomGibberishGenerator.getGibberish(
				MINIMUM_WORDS,
				MAXIMUM_WORDS,
				MINIMUM_SENTENCES,
				MAXIMUM_SENTENCES
			),
			RandomNameGenerator.getName()
		);
	}
}
