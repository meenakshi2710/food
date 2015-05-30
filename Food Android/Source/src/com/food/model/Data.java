package com.food.model;

import android.graphics.Bitmap;

/**
 * The Class Data is a simple Java Bean that is used to hold Name, Detail and
 * image pairs.
 */
public class Data
{

	/** The title1. */
	private String name;

	private int cId;
	
	/** The id. */
	private String id;

	/** The description. */
	private String desc;

	/** The image resource id. */
	private int image1;

	/** The image2. */
	private int image2;

	/** The image3. */
	private Bitmap image3;
	
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
	public Data(String name, String desc, int image1)
	{
		this.name = name;
		this.desc = desc;
		this.image1 = image1;
	}
	

	/**
	 * Instantiates a new data.
	 *
	 * @param title1 the title1
	 * @param image1 the image1
	 * @param image2 the image2
	 */
	public Data(String name, int image1, int image2)
	{
		this.name = name;
		this.image1 = image1;
		this.image2 = image2;
	}
	
	public Data(String id, String name, String desc, int image1)
	{
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.image1 = image1;
		
	}
	
	public Data(int cid, String id, String name, String desc, int image1)
	{
		this.cId = cid;
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.image1 = image1;
		
	}

	public Data(String id, String name, String desc, Bitmap image3)
	{
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.image3 = image3;
	}

	/**
	 * Gets the title1.
	 * 
	 * @return the title1
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the title1.
	 * 
	 * @param title1
	 *            the new title1
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public int getCid()
	{
		return cId;
	}

	public void setCid(int cid)
	{
		this.cId = cid;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id)
	{
		this.id = id;
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

	/**
	 * Gets the image2.
	 * 
	 * @return the image2
	 */
	public int getImage2()
	{
		return image2;
	}

	/**
	 * Sets the image2.
	 * 
	 * @param image2
	 *            the new image2
	 */
	public void setImage2(int image2)
	{
		this.image2 = image2;
	}
	/**
	 * Gets the image3.
	 * 
	 * @return the image3
	 */
	public Bitmap getImage3()
	{
		return image3;
	}

	/**
	 * Sets the image3.
	 * 
	 * @param image3
	 *            the new image3
	 */
	public void setImage3(Bitmap image3)
	{
		this.image3 = image3;
	}

}
