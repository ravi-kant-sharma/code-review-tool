package main;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class CodeReviewUploadHelper
{
  public static Properties getUserProperties()
    throws Exception
  {
    String str = System.getProperty("user.home");
    Properties localProperties = new Properties();
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(str + "/code_review_upload.properties");
      localProperties.load(localFileInputStream);
      if (localProperties.size() == 0) {
        throw new RuntimeException("Unable to load code_review_upload.properties");
      }
    }
    finally
    {
      if (localFileInputStream != null) {
        localFileInputStream.close();
      }
    }
    return localProperties;
  }
  
  public static Map getDirectoriesMap(Properties properties){
	  Map<String,String> map  = new HashMap<String,String>();
	  Iterator localIterator = properties.entrySet().iterator();
	  while(localIterator.hasNext()){
		  Object entry = (Map.Entry)localIterator.next();
		  if (((String)((Map.Entry)entry).getKey()).startsWith("map"))
	        {
	          Object localObject2 = (String)((Map.Entry)entry).getValue();
	          int i = ((String)localObject2).indexOf(";");
	          if (i == -1) {
	            throw new RuntimeException("Invalid map format for property: " + ((Map.Entry)entry).getKey());
	          }
	          map.put(((String)localObject2).substring(0,i), ((String)localObject2).substring(i+1));
	        }
	  }
	  return map;
  }
}
