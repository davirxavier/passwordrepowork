package category;

/**
 * Interface para intermedia��o entre o CategoryController e CategoryView.
 */
public interface InputHandler
{
	public char[] handleViewPassword(int catpos, int passpos, char[] secret) throws Exception;

	public void handleEditCategory(int catpos, String newname, char[] secret) throws Exception;

	public void handleEditPassword(int catpos, int passpos, String passdesc, String passuser, char[] pass,
			char[] secret) throws Exception;

	public void handleNewCategory(String newname, char[] secret) throws Exception;
	
	public void handleNewPassword(int catpos, String desc, String user, char[] pass, char[] secret) throws Exception;

	public void handleDeleteCategory(int catpos, char[] secret) throws Exception;

	public void handleDeletePassword(int catpos, int passpos, char[] secret) throws Exception;
}
