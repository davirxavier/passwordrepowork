package database.fileManagers;

import java.io.IOException;
import java.util.*;

import formatters.Formatter;

/**
 * Interface para as classes de gerenciamento de arquivo de dados.
 */
public interface DBFileManager
{

	/**
	 * @param text
	 * @param clearSecret TODO
	 * @return
	 */
	public void writeToFile(String text, char[] secret, boolean clearSecret) throws Exception;

	/**
	 * @param clearSecret TODO
	 * @return
	 */
	public String readFile(char[] secret, boolean clearSecret) throws Exception;

}