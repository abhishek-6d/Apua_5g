package com.sixdee.lms.util.parser;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonParser {

	  private static final Gson gson = new Gson();

	  public static String toJson(Object object) {
	    return gson.toJson(object);
	  }

	  public static Object fromJson(String json, Type type)
	  {
	    return gson.fromJson(json, type);
	  }

	  public static void main(String[] args)
	  {
	  }

}
