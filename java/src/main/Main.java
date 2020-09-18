package main;

import category.Category;
import category.CategoryController;
import category.CategoryView;
import category.IView;
import category.Password;
import database.CategoryDAO;
import database.CategoryPasswordConnectionManager;
import database.IConnectionManager;
import database.IDAO;
import database.IDAOInner;
import database.Initializable.UninitializedException;
import database.PasswordDAO;
import encrypters.AESEncrypter;
import encrypters.IEncrypter;

/**
 * Classe da aplica��o, tem os objetos principais da aplica��o e inicializa os
 * mesmos.
 */
public class Main
{
	private static final String DATABASE_PATH = "data.bin";
	private static final String DATABASE_DB_URL = "jdbc:sqlite:" + DATABASE_PATH;

	/**
	 * Inicializa as classes e o processo de troca de informa��es.
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 * @throws UninitializedException
	 */
	public static void main(String[] args) throws UninitializedException, Exception
	{
		// Necces�rios para o DAO
		IEncrypter encrypter = AESEncrypter.getInstance();

		// Inicializando DAO
		IConnectionManager manager = new CategoryPasswordConnectionManager();
		IDAO<Category> daoCategory = CategoryDAO.getInstance();
		daoCategory.init(manager);

		IDAOInner<Password, char[]> daoPassword = PasswordDAO.getInstance();
		daoPassword.init(manager);

		// Inicializando view e controladora
		IView view = new CategoryView();
		CategoryController controller = new CategoryController(view, daoCategory, daoPassword);
		view.setInputHandler(controller);

		// Iniciar view
		controller.updateView();
		view.startMainLoop();
	}
}