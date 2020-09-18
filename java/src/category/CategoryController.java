package category;

import java.io.IOException;
import java.util.*;

import database.CategoryDAO;
import database.CategoryDAO.UninitializedException;
import exceptions.database.CategoryAlreadyExistsException;
import exceptions.database.IncorrectSecretException;
import database.DAO;

/**
 * 
 */
public class CategoryController implements InputHandler
{
	private DAO<Category> dao;
	private List<Category> categories;
	public View view;

	/**
	 * Pergunta a view pelo segredo do usuário até o mesmo enviar o segredo correto.
	 * Caso envie o segredo correto, pega a lista de categorias do dao. Dao e view
	 * não podem ser nulos.
	 * 
	 * @param view
	 * @param dao
	 * @throws Exception
	 * @throws UninitializedException
	 */
	public CategoryController(View view, DAO<Category> dao) throws UninitializedException, Exception
	{
		this.view = view;
		this.dao = dao;

		while (true)
		{
			char[] secret = askViewForSecret("Insira sua senha mestra...");
			boolean check = dao.checkSecret(secret);
			if (check)
			{
				categories = dao.getAll(secret);
				break;
			}
		}
	}

	/*
	 * Setters e getters, nada de especial neles.
	 */
	/**
	 * @param name
	 * @param category
	 * @param password
	 * @return
	 */
	public void setPasswordUsername(String name, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setUsername(name);
	}

	/**
	 * @param desc
	 * @param category
	 * @param password
	 * @return
	 */
	public void setPasswordDescription(String desc, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setDescription(desc);
	}

	/**
	 * @param encpass
	 * @param category
	 * @param password
	 * @return
	 */
	public void setPasswordEncryptedPassword(String encpass, int category, int password)
	{
		Category c = categories.get(category);
		Password p = c.getPasswords().get(password);
		p.setEncryptedPassword(encpass);
	}

	/**
	 * @param name
	 * @return
	 */
	public void setCategoryName(String name, int catpos)
	{
		Category category = categories.get(catpos);
		category.setName(name);
	}

	public String getCategoryName(int catpos)
	{
		return categories.get(catpos).getName();
	}

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
	/*
	 * Fim dos getters e setters
	 */

	/**
	 * Retorna a lista de categorias como um hashmap de strings. Para uso na view.
	 * 
	 * @return HashMap/String, List/String//
	 */
	public HashMap<String, List<String>> asStringHashMap()
	{
		HashMap<String, List<String>> ret = new LinkedHashMap<>();

		for (Category category : categories)
		{
			int size = category.getPasswords().size();

			List<Password> passwords = category.getPasswords();
			List<String> passwordStrings = new ArrayList<>(size);

			for (Password password : passwords)
			{
				passwordStrings.add(password.toString());
			}

			ret.put(category.getName(), passwordStrings);
		}

		return ret;
	}

	/**
	 * Controla o evento de ver senha enviado pela view. Retorna a senha
	 * descriptografada.
	 * 
	 * @param catpos  - int: Número da categoria.
	 * @param passpos - int: Posição do Password na categoria.
	 * @param secret  - char[]: Segredo para descriptografia.
	 * @return char[]: Senha descriptografada, limpar quando não precisar mais.
	 * @throws Exception
	 */
	@Override
	public char[] handleViewPassword(int catpos, int passpos, char[] secret) throws Exception
	{
		return getDecryptedPassword(catpos, passpos, secret);
	}

	/**
	 * Controla o evento de editar categoria enviado pela view.
	 * 
	 * @param catpos  - int: Número da categoria.
	 * @param newname - String: Novo nome para categoria.
	 * @throws Exception
	 */
	@Override
	public void handleEditCategory(int catpos, String newname, char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
		{
			throw new IncorrectSecretException();
		}
		setCategoryName(newname, catpos);
		callUpdateDatabase(secret);
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
	 * @throws Exception
	 */
	@Override
	public void handleEditPassword(int catpos, int passpos, String passdesc, String passuser, char[] pass,
			char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
		{
			throw new IncorrectSecretException();
		}
		
		boolean changes = false;
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
			Category category = categories.get(catpos);
			category.changeEncryptedPassword(passpos, pass, secret);
			changes = true;
		}

		if (changes)
		{
			callUpdateDatabase(secret);
		}
	}

	/**
	 * Controla o evento de criar uma nova categoria enviado pela view. Utiliza uma
	 * factory.
	 * 
	 * @param newname - String: Nome da nova categoria.
	 * @throws Exception
	 */
	@Override
	public void handleNewCategory(String newname, char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
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
		callUpdateDatabase(secret);
	}

	/**
	 * Controla o evento de criar um novo Password enviado pela view.
	 * 
	 * @param catpos - int
	 * @param desc   - String
	 * @param user   - String
	 * @param pass   - char[]
	 * @param secret - char[]
	 * @throws Exception
	 */
	@Override
	public void handleNewPassword(int catpos, String desc, String user, char[] pass, char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
		{
			throw new IncorrectSecretException();
		}
		
		Category category = categories.get(catpos);
		category.newPassword(desc, user, pass, secret);
		callUpdateDatabase(secret);
	}

	/**
	 * Controla o evento de deletar uma categoria enviado pela view.
	 * 
	 * @param catpos - int: Número da categoria à ser deletada.
	 * @throws Exception
	 */
	@Override
	public void handleDeleteCategory(int catpos, char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
		{
			throw new IncorrectSecretException();
		}
		
		categories.remove(catpos);
		callUpdateDatabase(secret);
	}

	/**
	 * Controla o evento de deletar uma senha de uma categoria enviada pela view.
	 * 
	 * @param catpos  - int: Número da categoria da senha.
	 * @param passpos - int: Posição da senha na categoria.
	 * @throws Exception
	 */
	@Override
	public void handleDeletePassword(int catpos, int passpos, char[] secret) throws Exception
	{
		if(!dao.checkSecret(secret))
		{
			throw new IncorrectSecretException();
		}
		
		Category category = categories.get(catpos);
		category.getPasswords().remove(passpos);
		callUpdateDatabase(secret);
	}

	/**
	 * Chamada de atualização do banco de dados por meio do DAO.
	 * 
	 * @throws IOException
	 * @throws UninitializedException
	 */
	private void callUpdateDatabase(char[] secret) throws UninitializedException, Exception
	{
		dao.insertAll(categories, secret);
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
		view.show(asStringHashMap());
	}
}