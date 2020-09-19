package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConstants.CategoryConstants;
import database.DatabaseConstants.PasswordConstants;
import database.DatabaseConstants.RelationConstants;

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
		
		sqls.add("CREATE TABLE IF NOT EXISTS " + CategoryConstants.categoryTable + ""
				+ "("
				+ CategoryConstants.idColumn +  " AUTO INCREMENT INTEGER PRIMARY KEY NOT NULL, "
				+ CategoryConstants.nameColumn + " TEXT NOT NULL"
				+ ");");
		
		sqls.add("CREATE TABLE IF NOT EXISTS " + PasswordConstants.passwordTable + ""
				+ "("
				+ PasswordConstants.idColumn +  " AUTO INCREMENT INTEGER PRIMARY KEY NOT NULL, "
				+ PasswordConstants.descriptionColumn + " TEXT, "
				+ PasswordConstants.usernameColumn + " TEXT NOT NULL, "
				+ PasswordConstants.passwordColumn + " TEXT NOT NULL"
				+ ");");
		
		sqls.add("CREATE TABLE IF NOT EXISTS " + RelationConstants.relationTable + ""
				+ "("
				+ RelationConstants.passwordColumn +  " INTEGER NOT NULL, "
				+ RelationConstants.categoryColumn + " INTEGER NOT NULL"
				+ ");");
		
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
