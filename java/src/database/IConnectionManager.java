package database;

import java.sql.Connection;

public interface IConnectionManager
{
	public Connection getConnection();
	public void setURL(String url);
}
