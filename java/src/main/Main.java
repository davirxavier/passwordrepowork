package main;

import java.util.*;

import category.Category;
import category.CategoryController;
import category.CategoryView;
import category.InputHandler;
import category.Password;
import category.IView;
import database.CategoryPasswordConnectionManager;
import database.CategoryDAO;
import encrypters.AESEncrypter;
import encrypters.IEncrypter;
import database.IDAO;
import database.IDAOInner;
import database.PasswordDAO;
import database.Initializable.UninitializedException;
import database.IConnectionManager;

/**
 * Classe da aplicação, tem os objetos principais da aplicação e inicializa os
 * mesmos.
 */
public class Main
{
	private static final String DATABASE_PATH = "data.bin";
	private static final String DATABASE_DB_URL = "jdbc:sqlite:" + DATABASE_PATH;
	
	/**
	 * Inicializa as classes e o processo de troca de informações.
	 * 
	 * @param args
	 * @return
	 * @throws Exception 
	 * @throws UninitializedException 
	 */
	public static void main(String[] args) throws UninitializedException, Exception
	{
		//Neccesários para o DAO
		IEncrypter encrypter = AESEncrypter.getInstance();
		
		//Inicializando DAO
		IConnectionManager manager = new CategoryPasswordConnectionManager();
		IDAO<Category> daoCategory = CategoryDAO.getInstance();
		daoCategory.init(manager);
		
		IDAOInner<Password, char[]> daoPassword = PasswordDAO.getInstance();
		daoPassword.init(manager);
		
		//Inicializando view e controladora
		IView view = new CategoryView();
		CategoryController controller = new CategoryController(view, daoCategory, daoPassword);
		view.setInputHandler(controller);
		
		//Iniciar view
		controller.updateView();
		view.startMainLoop();
	}
}