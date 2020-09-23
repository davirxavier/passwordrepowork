package category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.IDAO;
import database.IDAOInner;
import database.Initializable.UninitializedException;
import exceptions.database.CategoryAlreadyExistsException;
import exceptions.database.IncorrectSecretException;

/**
 * Controlador para Categorias.
 * 
 * @implNote Implementa InputHandler para responder requisições da View.
 */
public class CategoryController implements CategoryInputHandler
{
	private IDAO<Category> daoCategory;
	private IDAOInner<Password, char[]> daoPassword;
	private List<Category> categories;
	public IView view;

	/**
	 * Pergunta a view pelo segredo do usuário até o mesmo enviar o segredo correto.
	 * Caso envie o segredo correto, pega a lista de categorias do dao. Dao e view
	 * não podem ser nulos. Dao deve ter sido inicializado anteriormente.
	 * 
	 * @param view
	 * @param dao
	 * @throws Exception
	 * @throws UninitializedException
	 */
	public CategoryController(IView view, IDAO<Category> dao, IDAOInner<Password, char[]> daoPassword)
			throws UninitializedException, Exception
	{
		this.view = view;
		this.daoCategory = dao;
		this.daoPassword = daoPassword;
		this.categories = dao.getAll();

//		while (true)
//		{
//			char[] secret = askViewForSecret("Insira sua senha mestra...");
//			if (secret == null)
//				continue;
//			
//			boolean check = daoPassword.checkSecret(secret, true);
//			if (check)
//			{
//				categories = dao.getAll();
//				break;
//			}
//		}
	}

	/*
	 * Setters e getters, nada de especial neles.
	 */
	public void setPasswordUsername(String name, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setUsername(name);
	}

	public void setPasswordDescription(String desc, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setDescription(desc);
	}

	public void setPasswordEncryptedPassword(String encpass, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setEncryptedPassword(encpass);
	}

	public void setCategoryName(String name, int catpos)
	{
		Category category = categories.get(catpos);
		category.setName(name);
	}

	public String getCategoryName(int catpos)
	{
		return categories.get(catpos).getName();
	}
	/*
	 * Fim dos getters e setters
	 */

	/**
	 * Usa o método da categoria para retornar a senha descriptografada do password
	 * refenciado.
	 * 
	 * @throws Exception: Se o segredo para a senha estiver errado.
	 * 
	 */
	public char[] getDecryptedPassword(int category, int password, char[] secret) throws Exception
	{
		Category c = categories.get(category);
		return c.getDecryptedPassword(password, secret);
	}

	/**
	 * Retorna a lista de categorias como um hashmap de strings. Para uso na view.
	 * 
	 * @return HashMap/String, List/String//
	 */
	public List<CategoryHandlerResponse> asHandlerResponse()
	{
		List<CategoryHandlerResponse> responses = new ArrayList<>(categories.size());

		categories.forEach(c ->
		{
			CategoryHandlerResponse response = new CategoryHandlerResponse();
			response.setCatId(c.getId());
			response.setCategoryName(c.getName());
			c.getPasswords().forEach(p ->
			{
				response.getPasswords().add(p.toString());
			});

			responses.add(response);
		});

		return responses;
	}

	/**
	 * Controla o evento de ver senha enviado pela view. Retorna a senha
	 * descriptografada.
	 * 
	 * @param catpos  - int: Número da categoria.
	 * @param passpos - int: Posição do Password na categoria.
	 * @param secret  - char[]: Segredo para descriptografia.
	 * @return char[]: Senha descriptografada, limpar quando não precisar mais.
	 * @throws InvalidInputException - Lançada quando passpos ou catpos são
	 *                               inválidos.
	 */
	@Override
	public char[] handleViewPassword(int catid, int passid, char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleEditPassword");
		
		Password password = category.getPasswordById(passid);
		if (password == null)
			throw new InvalidInputException("passid", "handleEditPassword");
		
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		return category.getDecryptedPassword(category.getPasswords().indexOf(password), secret);
	}

	/**
	 * Controla o evento de editar categoria enviado pela view.
	 * 
	 * @param catpos  - int: Número da categoria.
	 * @param newname - String: Novo nome para categoria.
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 * @throws InvalidInputException:   Lançada quando passpos ou catpos são
	 *                                  inválidos.
	 */
	@Override
	public void handleEditCategory(int catid, String newname, char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleEditCategory");
		
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		int catpos = categories.indexOf(category);
		setCategoryName(newname, catpos);
		daoCategory.update(category);
		updateView();
	}

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
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 * @throws InvalidInputException:   Lançada quando passpos ou catpos são
	 *                                  inválidos.
	 */
	@Override
	public void handleEditPassword(int catid, int passid, String passdesc, String passuser, char[] pass,
			char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleEditPassword");
		
		Password password = category.getPasswordById(passid);
		if (password == null)
			throw new InvalidInputException("passid", "handleEditPassword");
		
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		boolean changes = false;
		int catpos = categories.indexOf(category);
		int passpos = category.getPasswords().indexOf(password);
		if (passdesc != null)
		{
			setPasswordDescription(passdesc, catpos, passpos);
			changes = true;
		}
		if (passuser != null)
		{
			setPasswordUsername(passuser, catpos, passpos);
			changes = true;
		}
		if (pass != null && secret != null)
		{
			category.changeEncryptedPassword(passpos, pass, secret);
			changes = true;
		}

		if (changes)
		{
			daoPassword.update(password.getId() + "", password);
			updateView();
		}
	}

	/**
	 * Controla o evento de criar uma nova categoria enviado pela view. Utiliza uma
	 * factory.
	 * 
	 * @param newname - String: Nome da nova categoria.
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 */
	@Override
	public void handleNewCategory(String newname, char[] secret) throws Exception
	{
		if (!daoPassword.checkSecret(secret, false))
		{
			throw new IncorrectSecretException();
		}
		for (int i = 0; i < categories.size(); i++)
		{
			if (getCategoryName(i).equalsIgnoreCase(newname))
			{
				throw new CategoryAlreadyExistsException(newname);
			}
		}

		CategoryFactory factory = new CategoryFactory();
		Category category = factory.create(newname);
		categories.add(category);
		daoCategory.insert(category);
		updateView();
	}

	/**
	 * Controla o evento de criar um novo Password enviado pela view.
	 * 
	 * @param catpos - int
	 * @param desc   - String
	 * @param user   - String
	 * @param pass   - char[]
	 * @param secret - char[]
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 * @throws InvalidInputException:   Lançada quando catpos é inválido.
	 */
	@Override
	public void handleNewPassword(int catid, String desc, String user, char[] pass, char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleNewPassword");
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		Password password = category.newPassword(desc, user, pass, secret);
		daoPassword.insert(category.getId() + "", password);
		updateView();
	}

	/**
	 * Controla o evento de deletar uma categoria enviado pela view.
	 * 
	 * @param catpos - int: Número da categoria à ser deletada.
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 * @throws InvalidInputException:   Lançada quando catpos é inválido.
	 */
	@Override
	public void handleDeleteCategory(int catid, char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleDeleteCategory");
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		daoCategory.delete(category);
		categories.remove(category);
		updateView();
	}

	/**
	 * Controla o evento de deletar uma senha de uma categoria enviada pela view.
	 * 
	 * @param catpos  - int: Número da categoria da senha.
	 * @param passpos - int: Posição da senha na categoria.
	 * @throws IncorretSecretException: Jogada quando o segredo é dado como
	 *                                  incorreto.
	 * @throws InvalidInputException:   Lançada quando passpos ou catpos são
	 *                                  inválidos.
	 */
	@Override
	public void handleDeletePassword(int catid, int passid, char[] secret) throws Exception
	{
		Category category = getCategoryById(catid);
		if (category == null)
			throw new InvalidInputException("catid", "handleDeletePassword");
		
		Password password = category.getPasswordById(passid);
		if (password == null)
			throw new InvalidInputException("passid", "handleDeletePassword");
		
		if (!daoPassword.checkSecret(secret, false))
			throw new IncorrectSecretException();

		daoPassword.delete(password);
		category.getPasswords().remove(password);
		updateView();
	}

	/**
	 * Controla o evento de checar se o segredo enviado pela view está correto.
	 * 
	 * @param secret
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean handleCheckSecret(char[] secret) throws Exception
	{
		if (secret == null)
		{
			return false;
		}

		return daoPassword.checkSecret(secret, false);
	}

	/**
	 * Chamado quando a view pede uma atualização.
	 */
	@Override
	public void handleRequestUpdate() throws Exception
	{
		updateView();
	}

	/**
	 * Retorna uma categoria pelo id ou nulo se o id não existir.
	 */
	private Category getCategoryById(int id)
	{
		for (Category c : categories)
		{
			if (c.getId() == id)
			{
				return c;
			}
		}

		return null;
	}

	/**
	 * Chamada de atualização do banco de dados por meio do DAO.
	 * 
	 * @deprecated
	 * @throws IOException
	 * @throws UninitializedException
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void callUpdateDatabase(char[] secret) throws UninitializedException, Exception
	{
		daoCategory.insertAll(categories);
		updateView();
	}

	/**
	 * Pede a senha mestra do usuário e retorna a mesma. Nunca deve retornar sem a
	 * senha.
	 * 
	 * @param text - String: Texto para mostrar ao usuário.
	 * @return
	 */
	private char[] askViewForSecret(String text)
	{
		return view.askForSecret(text);
	}

	/**
	 * Atualiza a view com as informações necessárias. Deve enviar um hashmap criado
	 * pelo método "asStringHashMap".
	 */
	public void updateView()
	{
		view.show(asHandlerResponse());
	}

	/**
	 * Exceção levantada quando alguma entrada é dada como inválida.
	 * 
	 * @author Davi
	 */
	public class InvalidInputException extends Exception
	{
		private static final long serialVersionUID = 5903355793672054387L;

		public InvalidInputException(String input, String where)
		{
			super("Entrada \"" + input + "\" inválida em \n" + where + "\n.");
		}
	}
}