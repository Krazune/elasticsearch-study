package com.github.krazune.esstudy.quotes.randoms;

import com.github.krazune.esstudy.quotes.documents.Quote;
import com.github.krazune.esstudy.randoms.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.randoms.text.RandomNameGenerator;

public class RandomQuoteGenerator
{
	protected final static int MINIMUM_WORDS = 3;
	protected final static int MAXIMUM_WORDS = 13;
	protected final static int MINIMUM_SENTENCES = 1;
	protected final static int MAXIMUM_SENTENCES = 2;

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
