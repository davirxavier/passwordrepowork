package category;

/**
 * Interface para intermediação entre o CategoryController e CategoryView.
 */
public interface CategoryInputHandler
{
	/**
	 * Controla o evento de ver senha enviado pela view. Retorna a senha
	 * descriptografada.
	 * 
	 * @param catpos
	 * @param passpos
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public char[] handleViewPassword(int catpos, int passpos, char[] secret) throws Exception;

	/**
	 * Controla o evento de editar categoria enviado pela view.
	 * 
	 * @param catpos  - int: Número da categoria.
	 * @param newname - String: Novo nome para categoria.
	 */
	public void handleEditCategory(int catpos, String newname, char[] secret) throws Exception;

	/**
	 * Controla o evento de editar senha enviado pela view. Passar valor nulo caso
	 * não queira que o mesmo seja editado no Password.
	 * 
	 * @param catpos   - int
	 * @param passpos  - int
	 * @param passdesc - String
	 * @param passuser - String
	 * @param pass     - char[]
	 * @param secret   - char[]
	 */
	public void handleEditPassword(int catpos, int passpos, String passdesc, String passuser, char[] pass,
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
	 * @param catpos - int
	 * @param desc   - String
	 * @param user   - String
	 * @param pass   - char[]
	 * @param secret - char[]
	 */
	public void handleNewPassword(int catpos, String desc, String user, char[] pass, char[] secret) throws Exception;

	/**
	 * Controla o evento de deletar uma categoria enviado pela view.
	 * 
	 * @param catpos - int: Número da categoria à ser deletada.
	 */
	public void handleDeleteCategory(int catpos, char[] secret) throws Exception;

	/**
	 * Controla o evento de deletar uma senha de uma categoria enviada pela view.
	 * 
	 * @param catpos  - int: Número da categoria da senha.
	 * @param passpos - int: Posição da senha na categoria.
	 */
	public void handleDeletePassword(int catpos, int passpos, char[] secret) throws Exception;
	
	/**
	 * Controla o evento de checar se o segredo enviado pela view está correto.
	 * 
	 * @param secret
	 * @return boolean
	 * @throws Exception
	 */
	public boolean handleCheckSecret(char[] secret) throws Exception;
}
