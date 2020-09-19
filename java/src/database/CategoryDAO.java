package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import category.Category;
import category.Password;
import database.DatabaseConstants.CategoryConstants;
import database.DatabaseConstants.PasswordConstants;
import database.DatabaseConstants.RelationConstants;

/**
 * DAO para Category's.
 * Para documentação de métodos ver IDAO.
 */
public class CategoryDAO implements IDAO<Category>
{
	private static final int DEFAULT_ID = Category.DEFAULT_ID;
	private static final int INITIAL_ID = 1;
	private static CategoryDAO instance;
	private static final IDAOInner<Password, char[]> PASSWORD_DAO = PasswordDAO.getInstance();
	private IConnectionManager manager;

	/**
	 * Usada para checagem da inicialização do singleton com as dependências
	 * necessárias.
	 */
	private boolean initialized;

	private CategoryDAO()
	{
		initialized = false;
	}

	/**
	 * Instanciação da classe.
	 */
	public static CategoryDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (CategoryDAO.class)
			{
				if (instance == null)
				{
					instance = new CategoryDAO();
				}
			}
		}

		return instance;
	}

	/**
	 * Deve ser chamado antes da utilização dos métodos da classe.
	 * 
	 * @param DBFileManager
	 * @param Formatter<Category
	 */
	@Override
	public void init(IConnectionManager manager)
	{
		this.manager = manager;
		PASSWORD_DAO.init(manager);
		initialized = true;
	}

	@Override
	public List<Category> getAll() throws UninitializedException, Exception
	{
		checkThrowException();

		List<Category> categories = new ArrayList<>();
		String sql = "SELECT " + PasswordConstants.passwordTable + "." + PasswordConstants.idColumn + ", " 
				+ "" + PasswordConstants.passwordTable + "." + PasswordConstants.descriptionColumn + ", " 
				+ "" + PasswordConstants.passwordTable + "." + PasswordConstants.passwordColumn + " "
				+ "FROM " + CategoryConstants.categoryTable 
					+ ", " + PasswordConstants.passwordTable 
					+ ", " + RelationConstants.relationTable + " " 
				+ "WHERE " + RelationConstants.relationTable + "." + RelationConstants.categoryColumn + " = ? "
				+ "AND " + CategoryConstants.categoryTable + "." + CategoryConstants.idColumn + " = ? "
				+ "AND " + PasswordConstants.passwordTable + "." + PasswordConstants.idColumn + " "
						+ "= " + RelationConstants.relationTable + "." + RelationConstants.passwordColumn + ";";
		String sqlCat = "SELECT * FROM categories;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		PreparedStatement statementCat = null;
		try
		{
			statementCat = connection.prepareStatement(sqlCat);
			ResultSet resultSet = statementCat.executeQuery();
			statement = connection.prepareStatement(sql);

			while (resultSet.next())
			{
				int catid = resultSet.getInt(DatabaseConstants.CategoryConstants.idColumn.getString());
				String catname = resultSet.getString(DatabaseConstants.CategoryConstants.nameColumn.getString());

				statement.setInt(1, catid);
				statement.setInt(2, catid);
				ResultSet resultSetPass = statement.executeQuery();

				List<Password> passwords = new ArrayList<>();
				while (resultSetPass.next())
				{
					int pid = resultSetPass.getInt(DatabaseConstants.PasswordConstants.idColumn.getString());
					String description = resultSetPass
							.getString(DatabaseConstants.PasswordConstants.descriptionColumn.getString());
					String username = resultSetPass
							.getString(DatabaseConstants.PasswordConstants.usernameColumn.getString());
					String ppassword = resultSetPass
							.getString(DatabaseConstants.PasswordConstants.passwordColumn.getString());

					passwords.add(new Password(pid, description, username, ppassword));
				}

				categories.add(new Category(passwords, catid, catname));
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
			if (statementCat != null)
				statementCat.close();
		}

		return categories;
	}

	@Override
	public void insertAll(List<Category> categories) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "REPLACE INTO " + DatabaseConstants.CategoryConstants.categoryTable.getString() + "("
				+ DatabaseConstants.CategoryConstants.idColumn + ", " + DatabaseConstants.CategoryConstants.nameColumn
				+ ") " + "VALUES(?, ?);";

		String sqlRelation = "REPLACE INTO " + DatabaseConstants.RelationConstants.relationTable + "("
				+ DatabaseConstants.RelationConstants.categoryColumn + ", "
				+ DatabaseConstants.RelationConstants.passwordColumn + ") " + "VALUES(?, ?);";

		String sqlId = "SELECT IFNULL(MAX(" + CategoryConstants.idColumn + "), 1) as maxid " + "FROM "
				+ CategoryConstants.categoryTable + ";";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		PreparedStatement statementRelation = null;
		try
		{
			// Lógica caso alguma categoria seja fornecida sem um id definido.
			// Pega o maior id da tabela e usa o próximo valor para o id.
			int newId = CategoryDAO.INITIAL_ID;
			statement = connection.prepareStatement(sqlId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				newId = resultSet.getInt("maxid") + 1;
			}
			statement.close();
			////

			statement = connection.prepareStatement(sql);
			statementRelation = connection.prepareStatement(sqlRelation);

			for (Category category : categories)
			{
				// Lógica caso alguma categoria seja fornecida sem um id definido.
				if (category.getId() == CategoryDAO.DEFAULT_ID)
				{
					category.setId(newId);
					statement.setInt(1, newId);
					newId += 1;
				} else
				{
					statement.setInt(1, category.getId());
				}
				////
				statement.setString(2, category.getName());
				statement.addBatch();

				for (Password password : category.getPasswords())
				{
					statementRelation.setInt(1, category.getId());
					statementRelation.setInt(2, password.getId());
					statementRelation.addBatch();
				}

			}

			statement.execute();
			statementRelation.execute();

			HashMap<String, List<Password>> passwords = new HashMap<>();
			for (Category category : categories)
			{
				passwords.put(category.getId() + "", category.getPasswords());
			}
			PASSWORD_DAO.insertAll(passwords);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
			if (statementRelation != null)
				statementRelation.close();
		}
	}

	/**
	 * Diz se o dao está inicializado.
	 */
	@Override
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public Category get(String id) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "SELECT " + PasswordConstants.passwordTable + "." + PasswordConstants.idColumn + ", " 
				+ "" + PasswordConstants.passwordTable + "." + PasswordConstants.descriptionColumn + ", " 
				+ "" + PasswordConstants.passwordTable + "." + PasswordConstants.passwordColumn + " "
				+ "FROM " + CategoryConstants.categoryTable 
					+ ", " + PasswordConstants.passwordTable 
					+ ", " + RelationConstants.relationTable + " " 
				+ "WHERE " + RelationConstants.relationTable + "." + RelationConstants.categoryColumn + " = ? "
				+ "AND " + CategoryConstants.categoryTable + "." + CategoryConstants.idColumn + " = ? "
				+ "AND " + PasswordConstants.passwordTable + "." + PasswordConstants.idColumn + " "
						+ "= " + RelationConstants.relationTable + "." + RelationConstants.passwordColumn + ";";
		String sqlCat = "SELECT * FROM categories WHERE id = ?;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		PreparedStatement statementCat = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, id);

			statementCat = connection.prepareStatement(sqlCat);
			statementCat.setString(1, id);

			ResultSet resultSet = statementCat.executeQuery();
			if (!resultSet.next())
			{
				throw new CategoryNotFoundException(id);
			}

			int catid = resultSet.getInt(DatabaseConstants.CategoryConstants.idColumn.getString());
			String catname = resultSet.getString(DatabaseConstants.CategoryConstants.nameColumn.getString());

			resultSet.close();
			resultSet = statement.executeQuery();

			List<Password> passwords = new ArrayList<>();
			while (resultSet.next())
			{
				int pid = resultSet.getInt(DatabaseConstants.PasswordConstants.idColumn.getString());
				String description = resultSet
						.getString(DatabaseConstants.PasswordConstants.descriptionColumn.getString());
				String username = resultSet.getString(DatabaseConstants.PasswordConstants.usernameColumn.getString());
				String ppassword = resultSet.getString(DatabaseConstants.PasswordConstants.passwordColumn.getString());

				passwords.add(new Password(pid, description, username, ppassword));
			}

			return new Category(passwords, catid, catname);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
			if (statementCat != null)
				statement.close();
		}
	}

	@Override
	public void insert(Category category) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "INSERT INTO " + CategoryConstants.categoryTable.getString() + "(" + CategoryConstants.idColumn
				+ ", " + CategoryConstants.nameColumn + ") " + "VALUES(?, ?);";

		String sqlRelation = "REPLACE INTO " + DatabaseConstants.RelationConstants.relationTable + "("
				+ DatabaseConstants.RelationConstants.categoryColumn + ", "
				+ DatabaseConstants.RelationConstants.passwordColumn + ") " + "VALUES(?, ?);";

		String sqlId = "SELECT IFNULL(MAX(" + CategoryConstants.idColumn + "), 1) as maxid " + "FROM "
				+ CategoryConstants.categoryTable + ";";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			// Lógica caso a categoria seja fornecida sem um id definido.
			// Pega o maior id da tabela e usa o próximo valor para o id.
			if (category.getId() == CategoryDAO.DEFAULT_ID)
			{
				int newId = CategoryDAO.INITIAL_ID;

				statement = connection.prepareStatement(sqlId);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					newId = resultSet.getInt("maxid") + 1;
				}
				statement.close();

				category.setId(newId);
			}
			////

			statement = connection.prepareStatement(sql);
			statement.setInt(1, category.getId());
			statement.setString(2, category.getName());
			statement.execute();
			statement.close();

			statement = connection.prepareStatement(sqlRelation);
			for (Password password : category.getPasswords())
			{
				statement.setInt(1, category.getId());
				statement.setInt(2, password.getId());
				statement.addBatch();
			}
			statement.execute();

			HashMap<String, List<Password>> passwords = new HashMap<>();
			passwords.put(category.getId() + "", category.getPasswords());
			PASSWORD_DAO.insertAll(passwords);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
		}
	}

	@Override
	public void delete(Category t) throws UninitializedException, Exception
	{
		checkThrowException();

		// Queries diversas
		String sqlDelete = "DELETE FROM " + DatabaseConstants.CategoryConstants.categoryTable + " " + "WHERE "
				+ DatabaseConstants.CategoryConstants.idColumn + " = ?;";
		String sqlRelation = "DELETE FROM " + DatabaseConstants.RelationConstants.relationTable + " " + "WHERE "
				+ DatabaseConstants.RelationConstants.categoryColumn + " = ?;";
		// Será completada mais na frente utilizando a resposta da "sqlQuery"
		String sqlDeletePass = "DELETE FROM " + DatabaseConstants.PasswordConstants.passwordTable + " " + "WHERE "
				+ DatabaseConstants.PasswordConstants.idColumn + " IN (";
		String sqlQuery = "SELECT passwords.id " + "FROM categories, passwords, category_password "
				+ "WHERE category_password.category_id = ? " + "AND categories.id = ? "
				+ "AND passwords.id = category_password.password_id;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sqlDelete);
			statement.setInt(1, t.getId());
			statement.execute();
			if (statement.getUpdateCount() == 0)
			{
				throw new CategoryNotFoundException(t.getId() + "");
			}
			statement.close();

			statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, t.getId());
			statement.setInt(2, t.getId());

			ResultSet resultSet = statement.executeQuery();
			List<Integer> ids = new ArrayList<>();
			while (resultSet.next())
			{
				ids.add(resultSet.getInt(DatabaseConstants.PasswordConstants.idColumn.toString()));
			}

			for (int id : ids)
			{
				sqlDeletePass += "?,";
			}
			sqlDeletePass = sqlDeletePass.substring(0, sqlDeletePass.length()) + "-1);";

			statement.close();
			statement = connection.prepareStatement(sqlDeletePass);
			for (int i = 0; i < ids.size(); i++)
			{
				statement.setInt(i + 1, ids.get(i));
			}
			statement.execute();
			statement.close();

			statement = connection.prepareStatement(sqlRelation);
			statement.setInt(1, t.getId());
			statement.execute();
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
		}
	}

	@Override
	public void update(Category t) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "UPDATE " + CategoryConstants.categoryTable + " " + "SET " + CategoryConstants.nameColumn + " = ? "
				+ "WHERE " + CategoryConstants.idColumn + " = ?;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setString(1, t.getName());
			statement.setInt(2, t.getId());
			statement.execute();
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (statement != null)
				statement.close();
		}
	}

	/**
	 * Checagem de inicialização para diminuir código repetido.
	 */
	private void checkThrowException() throws UninitializedException
	{
		if (!initialized)
		{
			throw new UninitializedException(this.getClass().getName());
		}
	}

	/**
	 * Exceção levantada quando uma busca não acha a categoria especificada.
	 * 
	 * @author Davi
	 *
	 */
	public class CategoryNotFoundException extends Exception
	{
		private static final long serialVersionUID = -8654343115033568905L;

		public CategoryNotFoundException(String id)
		{
			super("Category " + id + " not found.");
		}
	}
}