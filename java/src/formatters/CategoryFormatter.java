package formatters;

import java.util.*;

import org.json.JSONObject;

import category.Category;
import category.Password;

/**
 * Formatador de categorias para converter uma lista de categorias para e de JSON.
 */
public class CategoryFormatter implements Formatter<Category>
{

	/**
	 * Serializa uma lista de categorias em um objeto JSON.
	 * 
	 * @param categories
	 * @return JSONObject
	 */
	public JSONObject toJson(List<Category> categories)
	{
		JSONObject object = new JSONObject();

		categories.forEach(c ->
		{
			JSONObject categoryObject = new JSONObject();
			List<Password> passwords = c.getPasswords();

			for (int i = 0; i < passwords.size(); i++)
			{
				JSONObject passwordObject = new JSONObject();
				Password password = passwords.get(i);

				passwordObject.put("description", password.getDescription());
				passwordObject.put("encryptedPassword", password.getEncryptedPassword());
				passwordObject.put("username", password.getUsername());

				categoryObject.put(Integer.toString(i), passwordObject);
			}

			object.put(c.getName(), categoryObject);
		});

		return object;
	}

	/**
	 * Transforma um JSON contendo uma lista de categorias em um arraylist de
	 * categorias.
	 * 
	 * @param obj
	 * @return categoryList
	 */
	public List<Category> fromJson(JSONObject obj)
	{
		List<Category> list = new ArrayList<>(obj.length());

		Iterator<String> iterator = obj.keys();
		iterator.forEachRemaining(k ->
		{
			JSONObject catObject = obj.getJSONObject(k);
			Iterator<String> catIterator = catObject.keys();

			List<Password> passwords = new ArrayList<>(catObject.length());
			catIterator.forEachRemaining(pkey ->
			{
				JSONObject passwordObject = catObject.getJSONObject(pkey);
				Password password = new Password(passwordObject.getString("description"),
						passwordObject.getString("username"), passwordObject.getString("encryptedPassword"));
				passwords.add(password);
			});

			Category category = new Category(passwords, k);
			list.add(category);
		});

		return list;
	}

}