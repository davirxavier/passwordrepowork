package category;

/**
 * Classe que guarda uma senha encriptada e informa��es referentes � mesma.
 * Usar id = -1 para um valor padr�o.
 */
public class Password
{
	public static final int DEFAULT_ID = -1;
	private int id;
	private String description;
	private String username;
	private String encryptedPassword;
	
	public Password(String description, String username, String encryptedPassword)
	{
		this.description = description;
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		id = DEFAULT_ID;
	}

	public Password(int id, String description, String username, String encryptedPassword)
	{
		this.description = description;
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.id = id;
	}

	/*
	 * Getters e setters, nada de especial neles.
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setEncryptedPassword(String encryptedPassword)
	{
		this.encryptedPassword = encryptedPassword;
	}

	public String getEncryptedPassword()
	{
		return encryptedPassword;
	}

	public void setDescription(String desc)
	{
		this.description = desc;
	}

	public String getDescription()
	{
		return description;
	}
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	/*
	 * 
	 */

	/**
	 * M�todo equal sobescrito para checar cada atributo individualmente.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;

		if (obj instanceof Password)
		{
			Password password = (Password) obj;
			return description.equals(password.description) && encryptedPassword.equals(password.encryptedPassword)
					&& username.equals(password.username) && id == password.getId();
		} else
			return false;
	}

	/**
	 * Retorna todas as informa��es do objeto em forma de string separadas por ','.
	 */
	@Override
	public String toString()
	{
		return description + "," + username + ",\"" + encryptedPassword + "\"";
	}

}