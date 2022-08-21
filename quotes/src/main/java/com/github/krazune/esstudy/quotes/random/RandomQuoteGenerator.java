package com.github.krazune.esstudy.quotes.random;

import com.github.krazune.esstudy.quotes.document.Quote;
import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.random.text.RandomNameGenerator;

public class RandomQuoteGenerator
{
	private final static int MIN_WORDS = 3;
	private final static int MAX_WORDS = 13;
	private final static int MIN_SENTENCES = 1;
	private final static int MAX_SENTENCES = 2;

	public static Quote getQuote()
	{
		return new Quote(
			RandomGibberishGenerator.getGibberish(
				MIN_WORDS,
				MAX_WORDS,
				MIN_SENTENCES,
				MAX_SENTENCES
			),
			RandomNameGenerator.getName()
		);
	}
}
