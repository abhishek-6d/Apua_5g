package com.sixdee.imp.parser;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonParser {

	  private static final Gson gson = new Gson();

	  public static String toJson(Object object) {
	    return gson.toJson(object);
	  }

	  public  Object fromJson(String json, Type type)
	  {
	    return gson.fromJson(json, type);
	  }

	  public static void main(String[] args)
	  {
	  }

}
