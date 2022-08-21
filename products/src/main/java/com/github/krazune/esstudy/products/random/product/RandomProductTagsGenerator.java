package com.github.krazune.esstudy.products.random.product;

import com.github.krazune.esstudy.products.product.ProductTag;
import com.github.krazune.esstudy.random.RandomPicker;

import java.util.LinkedList;
import java.util.List;

public class RandomProductTagsGenerator
{
	private static final List<ProductTag> TAGS = List.of(ProductTag.values());

	public static List<ProductTag> getTags()
	{
		List<ProductTag> tags = new LinkedList<>();

		for (int i = 0; i < TAGS.size(); ++i)
		{
			// The chances of adding the tag is 1/n, where "n" is the amount of tags.
			// This is arbitrary, no reason to pick a tag this way, besides already having RandomPicker.
			if (RandomPicker.pick(TAGS) == TAGS.get(i))
			{
				tags.add(TAGS.get(i));
			}
		}

		return tags;
	}
}
