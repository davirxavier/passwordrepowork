package formatters;

import java.util.*;

import org.json.JSONObject;

/**
 * 
 */
public interface Formatter<T>
{

	/**
	 * @param l
	 * @return
	 */
	public JSONObject toJson(List<T> l);

	/**
	 * @param obj
	 * @return
	 */
	public List<T> fromJson(JSONObject obj);

}