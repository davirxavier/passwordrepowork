package database;

import java.io.IOException;
import java.util.List;

import database.CategoryDAO.UninitializedException;
import database.fileManagers.DBFileManager;
import formatters.Formatter;

public interface DAO<T>
{
	/**
	 * @param fileManager
	 * @param formatter
	 */
	public void init(DBFileManager fileManager, Formatter<T> formatter);

	/**
	 * 
	 * @return
	 * @throws IOException 
	 * @throws UninitializedException 
	 */
	public List<T> getAll(char[] secret) throws UninitializedException, Exception;

	/**
	 * 
	 * @param values
	 * @throws IOException 
	 * @throws UninitializedException 
	 */
	public void insertAll(List<T> values, char[] secret) throws UninitializedException, Exception;
	
	/**
	 * 
	 * @param secret
	 * @return
	 */
	public boolean checkSecret(char[] secret);
	
	/**
	 * 
	 * @return
	 */
	public boolean isInitialized();
}
