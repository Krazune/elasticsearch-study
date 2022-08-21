package com.github.krazune.esstudy.products.random.product;

import com.github.krazune.esstudy.products.product.Product;
import com.github.krazune.esstudy.random.text.RandomGibberishGenerator;
import com.github.krazune.esstudy.random.text.RandomThingGenerator;

import java.util.Random;

public class RandomProductGenerator
{
	private final static int MIN_WORDS = 3;
	private final static int MAX_WORDS = 13;
	private final static int MIN_SENTENCES = 1;
	private final static int MAX_SENTENCES = 3;
	private final static int MIN_SALES = 0;
	private final static int MAX_SALES = 1000;

	public static Product getProduct()
	{
		return new Product(
			RandomThingGenerator.getThing(),
			RandomGibberishGenerator.getGibberish(
				MIN_WORDS,
				MAX_WORDS,
				MIN_SENTENCES,
				MAX_SENTENCES
			),
			new Random().nextInt(MIN_SALES, MAX_SALES + 1),
			RandomProductTagsGenerator.getTags()
		);
	}
}
