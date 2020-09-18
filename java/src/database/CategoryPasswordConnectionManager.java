package database;

import java.sql.Connection;

public class CategoryPasswordConnectionManager implements IConnectionManager
{
	private String url;
	private Connection connection;
	
	public CategoryPasswordConnectionManager()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public Connection getConnection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setURL(String url)
	{
		this.url = url;
	}

}
