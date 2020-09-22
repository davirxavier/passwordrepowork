package category;

import java.util.HashMap;
import java.util.List;

/**
 * Interface para intermediação entre o CategoryController e CategoryView.
 */
public interface CategoryInputHandler
{
	/**
	 * Controla o evento de ver senha enviado pela view. Retorna a senha
	 * descriptografada.
	 * 
	 * @param catid
	 * @param passid
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public char[] handleViewPassword(int catid, int passid, char[] secret) throws Exception;

	/**
	 * Controla o evento de editar categoria enviado pela view.
	 * 
	 * @param catid  - int: Número da categoria.
	 * @param newname - String: Novo nome para categoria.
	 */
	public void handleEditCategory(int catid, String newname, char[] secret) throws Exception;

	/**
	 * Controla o evento de editar senha enviado pela view. Passar valor nulo caso
	 * não queira que o mesmo seja editado no Password.
	 * 
	 * @param catid   - int
	 * @param passid  - int
	 * @param passdesc - String
	 * @param passuser - String
	 * @param pass     - char[]
	 * @param secret   - char[]
	 */
	public void handleEditPassword(int catid, int passid, String passdesc, String passuser, char[] pass,
			char[] secret) throws Exception;

	/**
	 * Controla o evento de criar uma nova categoria enviado pela view. Utiliza uma
	 * factory.
	 * 
	 * @param newname - String: Nome da nova categoria.
	 */
	public void handleNewCategory(String newname, char[] secret) throws Exception;

	/**
	 * Controla o evento de criar um novo Password enviado pela view.
	 * 
	 * @param catid - int
	 * @param desc   - String
	 * @param user   - String
	 * @param pass   - char[]
	 * @param secret - char[]
	 */
	public void handleNewPassword(int catid, String desc, String user, char[] pass, char[] secret) throws Exception;

	/**
	 * Controla o evento de deletar uma categoria enviado pela view.
	 * 
	 * @param catid - int: Número da categoria à ser deletada.
	 */
	public void handleDeleteCategory(int catid, char[] secret) throws Exception;

	/**
	 * Controla o evento de deletar uma senha de uma categoria enviada pela view.
	 * 
	 * @param catid  - int: Número da categoria da senha.
	 * @param passid - int: Posição da senha na categoria.
	 */
	public void handleDeletePassword(int catid, int passid, char[] secret) throws Exception;

	/**
	 * Controla o evento de checar se o segredo enviado pela view está correto.
	 * 
	 * @param secret
	 * @return boolean
	 * @throws Exception
	 */
	public boolean handleCheckSecret(char[] secret) throws Exception;

	/**
	 * Controla o evento da view pedindo uma atualização.
	 * 
	 * @return
	 * @throws Exception
	 */
	public void handleRequestUpdate() throws Exception;
}
