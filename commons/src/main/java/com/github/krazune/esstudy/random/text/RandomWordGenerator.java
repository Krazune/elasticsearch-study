package com.github.krazune.esstudy.random.text;

import com.github.krazune.esstudy.random.RandomPicker;

import java.util.List;

public class RandomWordGenerator
{
	private static final List<String> WORDS = List.of(
		"lawgiver", "there", "save", "destroy", "art", "thou", "neighbor",
		"god", "alone", "law", "power", "message", "build", "up", "down",
		"allowed", "beneficial", "everything", "trespasses", "father",
		"forgive", "others", "heavenly", "sin", "earth", "lord", "gift",
		"good", "bad", "above", "light", "perfect", "shadow", "heart",
		"selfish", "give", "sacrifice", "voice", "shall", "grace",
		"show", "more", "course", "since", "have", "known", "everlasting",
		"never", "weak", "measure", "blame", "do", "not", "believe"
	);

	public static String getWord()
	{
		return RandomPicker.pick(WORDS);
	}
}
