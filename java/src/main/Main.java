package main;

import category.Category;
import category.CategoryController;
import category.view.CategoryViewGraphical;
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
import javafx.stage.Stage;

/**
 * Classe da aplicação, tem os objetos principais da aplicação e inicializa os
 * mesmos. Estende Application para que a interface gráfica JavaFX funcione.
 */
public class Main extends Application
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
		// Inicializando DAOs
		IConnectionManager manager = new CategoryPasswordConnectionManager();
		manager.setURL(DATABASE_DB_URL);
		IDAO<Category> daoCategory = CategoryDAO.getInstance();
		daoCategory.init(manager);

		IDAOInner<Password, char[]> daoPassword = PasswordDAO.getInstance();
		daoPassword.init(manager);

		// Inicializando view e controladora
		IView view = new CategoryViewGraphical();
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
		Parent root = FXMLLoader.load(getClass().getResource("../category/view/CategoryView.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle(TextConstants.nameShort + " - " + TextConstants.nameLong);
		primaryStage.show();
	}
}