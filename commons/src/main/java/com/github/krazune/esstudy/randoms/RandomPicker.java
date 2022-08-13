package com.github.krazune.esstudy.randoms;

import java.util.List;
import java.util.Random;

public class RandomPicker
{
	public static <T> T get(List<T> list)
	{
		return list.get(new Random().nextInt(0, list.size()));
	}
}
