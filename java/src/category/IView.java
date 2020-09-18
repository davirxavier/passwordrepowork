package category;

import java.util.*;

/**
 * Interface para media��o entre o CategoryController e CategoryView.
 */
public interface IView
{

	/**
	 * Mostra as categorias passadas no hashmap.
	 * @param c
	 * @return
	 */
	public void show(HashMap<String, List<String>> c);
	
	/**
	 * Retorna o segredo enviado pelo usu�rio. 
	 * @param textToShow
	 * @return
	 */
	public char[] askForSecret(String textToShow);

	/**
	 * Seta o controlador de entrada da view.
	 * @param handler
	 */
	public void setInputHandler(InputHandler handler);
	
	/**
	 * Come�a o loop de a��o principal da view.
	 */
	public void startMainLoop();
}