package category;

import java.util.HashMap;
import java.util.List;

/**
 * Interface para mediação entre o CategoryController e CategoryView.
 */
public interface IView
{

	/**
	 * Mostra as categorias passadas no hashmap.
	 * 
	 * @param c
	 * @return
	 */
	public void show(HashMap<String, List<String>> c);

	/**
	 * Retorna o segredo enviado pelo usuário.
	 * 
	 * @param textToShow
	 * @return
	 */
	public char[] askForSecret(String textToShow);

	/**
	 * Seta o controlador de entrada da view.
	 * 
	 * @param handler
	 */
	public void setInputHandler(CategoryInputHandler handler);

	/**
	 * Começa o loop de ação principal da view.
	 */
	public void startMainLoop();
}