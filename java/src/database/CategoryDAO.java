package database;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import category.Category;
import database.fileManagers.DBFileManager;
import database.fileManagers.DatabaseFileManager;
import formatters.CategoryFormatter;
import formatters.Formatter;

/**
 * DAO para inser��o e leitura de categorias do banco de dados. Por enquanto
 * somente l� e sobrescreve todos as categorias de uma vez s� por conta do
 * processo de criptografia.
 */
public class CategoryDAO implements DAO<Category>
{

	private static CategoryDAO instance;
	private DBFileManager fileManager;
	private Formatter<Category> formatter;

	/**
	 * Usada para checagem da inicializa��o do singleton com as depend�ncias
	 * necess�rias.
	 */
	private boolean initialized;

	private CategoryDAO()
	{
		initialized = false;
	}

	/**
	 * Instancia��o da classe.
	 */
	public static CategoryDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (CategoryDAO.class)
			{
				if (instance == null)
				{
					instance = new CategoryDAO();
				}
			}
		}

		return instance;
	}

	/**
	 * Deve ser chamado antes da utiliza��o dos m�todos "getAll" e "insertAll".
	 * 
	 * @param DBFileManager
	 * @param Formatter<Category
	 */
	public void init(DBFileManager fileManager, Formatter<Category> formatter)
	{
		this.fileManager = fileManager;
		this.formatter = formatter;

		if (fileManager != null && formatter != null)
			initialized = true;
	}

	/**
	 * Retorna uma lista de todas as categorias do banco de dados.
	 * 
	 * @param secret - char[]: segredo para descriptografia do banco de dados.
	 * @throws UninitializedException, Exception
	 */
	public List<Category> getAll(char[] secret) throws UninitializedException, Exception
	{
		if (!initialized)
			throw new UninitializedException();

		String fileString = fileManager.readFile(secret, true);
		List<Category> categories = formatter.fromJson(new JSONObject(fileString));

		return categories;
	}

	/**
	 * Sobrescreve todas as categorias do banco de dados com as especificadas como
	 * par�metro.
	 * 
	 * @param categories - List<Category: Lista de categorias � serem escritas.
	 * @param secret - char[]: Segredo para criptografia do banco de dados.
	 */
	public void insertAll(List<Category> categories, char[] secret) throws UninitializedException, Exception
	{
		if (!initialized)
			throw new UninitializedException();

		String categorieString = formatter.toJson(categories).toString();
		fileManager.writeToFile(categorieString, secret, true);
	}

	/**
	 * Exce��o jogada quando os m�todos da classe m�e s�o utilizados antes da mesma
	 * ser inicializada corretamente pelo m�todo init.
	 */
	public class UninitializedException extends RuntimeException
	{
		private static final long serialVersionUID = 3079315354917669671L;

		public UninitializedException()
		{
			super("CategoryDAO was not initilized using \"init\" method!");
		}
	}
	
	/**
	 * Checa se o segredo passado est� correto.
	 * @param secret - char[]: Segredo a ser testado
	 * @return true se sim.
	 */
	@Override
	public boolean checkSecret(char[] secret)
	{
		try
		{
			String fileString = fileManager.readFile(secret, false);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Diz se o dao est� inicializado.
	 */
	@Override
	public boolean isInitialized()
	{
		return initialized;
	}
}