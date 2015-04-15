package com.food.model;

/**
 * The Class Data is a simple Java Bean that is used to hold Name, Detail and
 * image pairs.
 */
public class Music
{
    private int id;
    
	/** The title */
	private String title;

	/** The URL */
	private String URL;

	/** The description. */
	private String desc;

	/** The image resource id. */
	private int image1;


	/**
	 * Instantiates a new feed class.
	 * 
	 * @param title1
	 *            the title1
	 * @param desc
	 *            the desc
	 * @param image1
	 *            the image1
	 */
	public Music(int id, String title, String URL, String desc, int image1)
	{
		this.id = id;
		this.title = title;
		this.URL = URL;
		this.desc = desc;
		this.image1 = image1;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title1
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	/**
	 * Gets the URL.
	 * 
	 * @return the URL
	 */
	public String getURL()
	{
		return URL;
	}

	/**
	 * Sets the title1.
	 * 
	 * @param title1
	 *            the new title1
	 */
	public void setURL(String URL)
	{
		this.URL = URL;
	}

	/**
	 * Gets the desc.
	 * 
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * Sets the desc.
	 * 
	 * @param desc
	 *            the new desc
	 */
	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	/**
	 * Gets the image1.
	 * 
	 * @return the image1
	 */
	public int getImage1()
	{
		return image1;
	}

	/**
	 * Sets the image1.
	 * 
	 * @param image1
	 *            the new image1
	 */
	public void setImage1(int image1)
	{
		this.image1 = image1;
	}

}
