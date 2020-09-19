package database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionManager
{
	public Connection getConnection() throws SQLException;
	public void setURL(String url);
	public void initDB() throws SQLException;
}
