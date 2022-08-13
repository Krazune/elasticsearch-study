package com.github.krazune.esstudy.quotes.documents;

public class Quote
{
	protected String quote;
	protected String author;

	public Quote() {}

	public Quote(String quote, String author)
	{
		this.quote = quote;
		this.author = author;
	}

	public String getQuote()
	{
		return quote;
	}

	public void setQuote(String quote)
	{
		this.quote = quote;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	@Override
	public String toString()
	{
		return "Quote: " + quote + "\nAuthor: " + author;
	}
}
