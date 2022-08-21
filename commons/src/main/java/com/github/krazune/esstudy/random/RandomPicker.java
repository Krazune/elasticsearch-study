package com.github.krazune.esstudy.random;

import java.util.List;
import java.util.Random;

public class RandomPicker
{
	public static <T> T pick(List<T> list)
	{
		if (list.isEmpty())
		{
			return null;
		}

		return list.get(new Random().nextInt(0, list.size()));
	}
}
