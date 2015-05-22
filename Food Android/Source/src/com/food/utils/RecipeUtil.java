package com.food.utils;

import com.food.R;

public class RecipeUtil {

	public static int setCategoryImage(int categoryId) {
		switch(categoryId) {
		   case 0:
			   return R.drawable.cat2;
		   case 1:
			   return R.drawable.cat3;
		   case 2:
			   return R.drawable.cat1;
		   case 3:
			   return R.drawable.cat6;
		   case 4:
			   return R.drawable.cat5;
		   case 5:
			   return R.drawable.cat4;
		   case 6:
			   return R.drawable.cat2;
		   case 7:
			   return R.drawable.cat1;
		   default: 
			   return R.drawable.cat2;
		}
		

	}
}