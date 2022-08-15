package com.github.krazune.esstudy.random.text;

import com.github.krazune.esstudy.random.RandomPicker;

import java.util.List;

public class RandomThingGenerator
{
	protected static final List<String> ADJECTIVES = List.of(
		"red",
		"green",
		"blue",
		"cyan",
		"yellow",
		"white",
		"yellow",
		"big",
		"small",
		"medium",
		"weird",
		"hard",
		"soft",
		"tall",
		"short",
		"long",
		"wide",
		"narrow",
		"plain"
	);

	protected static final List<String> THINGS = List.of(
		"mouse",
		"keyboard",
		"laptop",
		"desktop",
		"screen",
		"speakers",
		"microphone",
		"headset",
		"webcam",
		"cable",
		"printer"
	);

	public static String getThing()
	{
		String adjective = RandomPicker.get(ADJECTIVES);
		String thing = RandomPicker.get(THINGS);

		return adjective.substring(0, 1).toUpperCase() + adjective.substring(1) + ' ' + thing;
	}
}
