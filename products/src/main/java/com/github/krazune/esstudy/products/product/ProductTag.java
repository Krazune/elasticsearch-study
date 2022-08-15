package com.github.krazune.esstudy.products.product;

// These tags have to be weird enough to be different from adjectives generated from the RandomProductNameGenerator.
public enum ProductTag
{
	ALIEN_TECH("alien tech"),
	RADIOACTIVE("radioactive"),
	RARE("rare"),
	ETHEREAL("ethereal"),
	ASTERIAL("asterial"),
	DIVINE("divine");

	final String textRepresentation;

	ProductTag(String textRepresentation)
	{
		this.textRepresentation = textRepresentation;
	}

	@Override
	public String toString()
	{
		return textRepresentation;
	}
}
