package main;

import category.Category;
import category.CategoryController;
import category.IView;
import category.Password;
import database.CategoryDAO;
import database.CategoryPasswordConnectionManager;
import database.IConnectionManager;
import database.IDAO;
import database.IDAOInner;
import database.Initializable.UninitializedException;
import database.PasswordDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import res.img.ImagePath;

/**
 * Classe da aplicação, tem os objetos principais da aplicação e inicializa os
 * mesmos. Estende Application para que a interface gráfica JavaFX funcione.
 */
public class Main extends Application
{
	private static final String DATABASE_PATH = "data.db";
	private static final String DATABASE_DB_URL = "jdbc:sqlite:" + DATABASE_PATH;
	private static IView view;
	private static Parent fxRoot;

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
		//Inicializando a janela do JavaFX
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/category/view/CategoryView.fxml"));
		fxRoot = loader.load();
		view = loader.getController();
		
		// Inicializando DAOs e DB
		IConnectionManager manager = new CategoryPasswordConnectionManager();
		manager.setURL(DATABASE_DB_URL);
		manager.initDB();
		IDAO<Category> daoCategory = CategoryDAO.getInstance();
		daoCategory.init(manager);

		IDAOInner<Password, char[]> daoPassword = PasswordDAO.getInstance();
		daoPassword.init(manager);

		// Inicializando view e controladora
		CategoryController controller = new CategoryController(view, daoCategory, daoPassword);
		view.setInputHandler(controller);

		// Iniciar view
		controller.updateView();
		view.startMainLoop();
		
		launch(args);
	}

	/**
	 * Inicializa a aplicação JavaFX
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setScene(new Scene(fxRoot));
		primaryStage.setTitle(TextConstants.nameShort + " - " + TextConstants.nameLong);
		primaryStage.setTitle("EPR - Encrypted Password Repository");
		primaryStage.getIcons().add(new Image(ImagePath.APPICON.getPath()));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}