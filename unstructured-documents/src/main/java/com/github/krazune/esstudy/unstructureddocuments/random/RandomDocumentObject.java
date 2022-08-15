package com.github.krazune.esstudy.unstructureddocuments.random;

import com.github.krazune.esstudy.unstructureddocuments.document.MyFloat;
import com.github.krazune.esstudy.unstructureddocuments.document.MyInteger;
import com.github.krazune.esstudy.unstructureddocuments.document.MyString;

import java.util.Random;

public class RandomDocumentObject
{
	public static Object getDocumentObject()
	{
		Random random = new Random();
		int randomInteger = random.nextInt(3);

		switch (randomInteger)
		{
			case 0:
				return new MyInteger(random.nextInt());

			case 1:
				return new MyFloat(random.nextFloat());

			case 2:
			default:
				return new MyString("This is a string");
		}
	}
}
