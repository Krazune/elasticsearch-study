package com.github.krazune.esstudy.products.random.product;

import com.github.krazune.esstudy.products.product.Product;
import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.random.text.RandomThingGenerator;

import java.util.Random;

public class RandomProductGenerator
{
	private final static int MINIMUM_WORDS = 3;
	private final static int MAXIMUM_WORDS = 13;
	private final static int MINIMUM_SENTENCES = 1;
	private final static int MAXIMUM_SENTENCES = 3;
	private final static int MINIMUM_SALES = 0;
	private final static int MAXIMUM_SALES = 1000;

	public static Product getProduct()
	{
		return new Product(
			RandomThingGenerator.getThing(),
			RandomGibberishGenerator.getGibberish(
				MINIMUM_WORDS,
				MAXIMUM_WORDS,
				MINIMUM_SENTENCES,
				MAXIMUM_SENTENCES
			),
			new Random().nextInt(MINIMUM_SALES, MAXIMUM_SALES + 1),
			RandomProductTagsGenerator.getTags()
		);
	}
}
