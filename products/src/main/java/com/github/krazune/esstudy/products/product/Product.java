package com.github.krazune.esstudy.products.product;

import java.util.List;

public class Product
{
	protected String name;
	protected String description;
	protected int sales;
	protected List<ProductTag> tags;

	public Product() {}

	public Product(String name, String description, int sales, List<ProductTag> tags)
	{
		this.name = name;
		this.description = description;
		this.sales = sales;
		this.tags = tags;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getSales()
	{
		return sales;
	}

	public void setSales(int sales)
	{
		this.sales = sales;
	}

	public List<ProductTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ProductTag> tags)
	{
		this.tags = tags;
	}

	@Override
	public String toString()
	{
		String productString = "Name: " + name + "\nDescription: " + description + "\nSales: " + sales + "\nTags: ";

		for (int i = 0; i < tags.size(); ++i)
		{
			if (i != 0)
			{
				productString += ", ";
			}

			productString += tags.get(i).toString();
		}

		return productString;
	}
}
