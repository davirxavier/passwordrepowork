package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryPasswordConnectionManager implements IConnectionManager
{
	private String url;
	private Connection connection;
	
	public CategoryPasswordConnectionManager()
	{
		url = "";
	}
	
	@Override
	public void initDB() throws SQLException
	{
		if (connection == null)
		{
			connection = DriverManager.getConnection(url);
		}
		
		List<String> sqls = new ArrayList<>();
		
		sqls.add("");
		
		for (String s : sqls)
		{
			PreparedStatement statement = connection.prepareStatement(s);
			statement.execute();
			statement.close();
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException
	{
		if (connection == null)
		{
			connection = DriverManager.getConnection(url);
		}
		
		return connection;
	}

	@Override
	public void setURL(String url)
	{
		this.url = url;
	}

}
