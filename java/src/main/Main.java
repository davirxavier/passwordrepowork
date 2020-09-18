package main;

import java.util.*;

import category.Category;
import category.CategoryController;
import category.CategoryView;
import category.InputHandler;
import category.View;
import database.CategoryDAO;
import database.CategoryDAO.UninitializedException;
import database.fileManagers.DBFileManager;
import database.fileManagers.DatabaseFileManager;
import encrypters.AESEncrypter;
import encrypters.Encrypter;
import formatters.CategoryFormatter;
import formatters.Formatter;
import database.DAO;

/**
 * Classe da aplicação, tem os objetos principais da aplicação e inicializa os
 * mesmos.
 */
public class Main
{
	private static final String DATABASE_PATH = "data.bin";
	
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
		Encrypter encrypter = AESEncrypter.getInstance();
		DBFileManager fileManager = new DatabaseFileManager(DATABASE_PATH, encrypter);
		Formatter<Category> formatter = new CategoryFormatter();
		
		//Inicializando DAO
		DAO<Category> dao = CategoryDAO.getInstance();
		dao.init(fileManager, formatter);
		
		//Inicializando view e controladora
		View view = new CategoryView();
		CategoryController controller = new CategoryController(view, dao);
		view.setInputHandler(controller);
		
		//Iniciar view
		controller.updateView();
		view.startMainLoop();
	}
}