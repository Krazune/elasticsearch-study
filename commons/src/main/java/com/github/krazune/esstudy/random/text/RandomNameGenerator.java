package com.github.krazune.esstudy.random.text;

import com.github.krazune.esstudy.random.RandomPicker;

import java.util.List;

public class RandomNameGenerator
{
	private static final List<String> FIRST_NAMES = List.of(
		"Michael", "John", "Peter", "Terry", "Marie", "David", "Rachel",
		"Emma", "Sophia"
	);
	private static final List<String> SECOND_NAMES = List.of(
		"Brown", "Jones", "Smith", "Davis", "Johnson", "Williams",
		"Taylor", "Lewis"
	);

	public static String getFirstName()
	{
		return RandomPicker.get(FIRST_NAMES);
	}

	public static String getSecondName()
	{
		return RandomPicker.get(SECOND_NAMES);
	}

	public static String getName()
	{
		return getFirstName() + " " + getSecondName();
	}
}
