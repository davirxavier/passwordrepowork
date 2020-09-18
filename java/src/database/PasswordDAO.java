package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import category.Password;
import database.DatabaseConstants.CategoryConstants;
import encrypters.AESEncrypter;
import encrypters.IEncrypter;

/**
 * Dao para Passwords. Para documentação de métodos ver IDAOInner.
 * 
 * @author Davi
 */
public class PasswordDAO implements IDAOInner<Password, char[]>
{
	private static final int INITIAL_ID = 1;
	private static final int DEFAULT_ID = Password.DEFAULT_ID;
	private static final IEncrypter ENCRYPTER = AESEncrypter.getInstance();
	private static PasswordDAO instance;
	private IConnectionManager manager;
	private boolean initialized;

	private PasswordDAO()
	{
		initialized = false;
	}

	public static PasswordDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (CategoryDAO.class)
			{
				if (instance == null)
				{
					instance = new PasswordDAO();
				}
			}
		}

		return instance;
	}

	@Override
	public void init(IConnectionManager manager)
	{
		this.manager = manager;
		initialized = true;
	}

	@Override
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public void update(String id, Password p) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "UPDATE " + DatabaseConstants.PasswordConstants.passwordTable + " " + "SET "
				+ DatabaseConstants.PasswordConstants.descriptionColumn + " = ?, "
				+ DatabaseConstants.PasswordConstants.usernameColumn + "= ?, "
				+ DatabaseConstants.PasswordConstants.passwordColumn + "= ? " + "WHERE "
				+ DatabaseConstants.PasswordConstants.idColumn + " = ? LIMIT 1;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);

			statement.setString(1, p.getDescription());
			statement.setString(2, p.getUsername());
			statement.setString(3, p.getEncryptedPassword());
			statement.setString(4, id);

			statement.execute();

			if (statement.getUpdateCount() == 0)
			{
				throw new PasswordNotFoundException(id);
			}
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
	 * Analisa se o segredo passado está correto. Se a tabela de senhas estiver
	 * vazia retorna verdadeiro.
	 * 
	 * @param char[] - secret
	 */
	@Override
	public boolean checkSecret(char[] secret, boolean clearSecret) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "SELECT * FROM " + DatabaseConstants.PasswordConstants.passwordTable + " " + "WHERE "
				+ DatabaseConstants.PasswordConstants.idColumn + " = ?;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setString(1, "1");
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
			{
				return true;
			}

			String encPassword = resultSet.getString(DatabaseConstants.PasswordConstants.passwordColumn.getString());
			try
			{
				ENCRYPTER.decrypt(encPassword, secret, clearSecret);
				return true;
			} catch (Exception e)
			{
				return false;
			}
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
	public void insert(String parentId, Password t) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "INSERT INTO " + DatabaseConstants.PasswordConstants.passwordTable + " ("
				+ DatabaseConstants.PasswordConstants.idColumn + ","
				+ DatabaseConstants.PasswordConstants.descriptionColumn + ","
				+ DatabaseConstants.PasswordConstants.usernameColumn + ","
				+ DatabaseConstants.PasswordConstants.passwordColumn + ")" + " VALUES (?, ?, ?, ?);";

		String sqlRelation = "INSERT INTO " + DatabaseConstants.RelationConstants.relationTable + " ("
				+ DatabaseConstants.RelationConstants.categoryColumn + ", "
				+ DatabaseConstants.RelationConstants.passwordColumn + ")" + " VALUES(?, ?)";

		String sqlId = "SELECT IFNULL(MAX(" + CategoryConstants.idColumn + "), 1) as maxid " + "FROM "
				+ CategoryConstants.categoryTable + ";";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			// Lógica caso algum Password seja fornecido sem um id definido.
			// Pega o maior id da tabela e usa o próximo valor para o id.
			if (t.getId() == PasswordDAO.DEFAULT_ID)
			{
				int newId = PasswordDAO.INITIAL_ID;

				statement = connection.prepareStatement(sqlId);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					newId = resultSet.getInt("maxid") + 1;
				}
				statement.close();

				t.setId(newId);
			}
			////

			statement = connection.prepareStatement(sqlRelation);
			statement.setString(1, parentId);
			statement.setInt(2, t.getId());
			statement.execute();
			statement.close();

			statement = connection.prepareStatement(sql);
			statement.setInt(1, t.getId());
			statement.setString(2, t.getDescription());
			statement.setString(3, t.getUsername());
			statement.setString(4, t.getEncryptedPassword());

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
	public void insertAll(HashMap<String, List<Password>> values) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "REPLACE INTO " + DatabaseConstants.PasswordConstants.passwordTable + " ("
				+ DatabaseConstants.PasswordConstants.idColumn + ","
				+ DatabaseConstants.PasswordConstants.descriptionColumn + ","
				+ DatabaseConstants.PasswordConstants.usernameColumn + ","
				+ DatabaseConstants.PasswordConstants.passwordColumn + ")" + "VALUES (?, ?, ?, ?);";

		String sqlRelation = "REPLACE INTO " + DatabaseConstants.RelationConstants.relationTable + " ("
				+ DatabaseConstants.RelationConstants.categoryColumn + ", "
				+ DatabaseConstants.RelationConstants.passwordColumn + ")" + " VALUES(?, ?)";

		String sqlId = "SELECT IFNULL(MAX(" + CategoryConstants.idColumn + "), 1) as maxid " + "FROM "
				+ CategoryConstants.categoryTable + ";";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		PreparedStatement statementRelation = null;
		try
		{
			// Lógica caso algum Password seja fornecida sem um id definido.
			// Pega o maior id da tabela e usa o próximo valor para o id.
			int newId = PasswordDAO.INITIAL_ID;

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

			Iterator<String> keys = values.keySet().iterator();
			while (keys.hasNext())
			{
				String key = keys.next();
				List<Password> passwords = values.get(key);

				for (Password password : passwords)
				{
					statementRelation = connection.prepareStatement(sqlRelation);
					statementRelation.setString(1, key);
					statementRelation.setString(2, password.getId() + "");
					statementRelation.addBatch();

					// Lógica caso algum Password seja fornecido sem um id definido.
					if (password.getId() == PasswordDAO.DEFAULT_ID)
					{
						password.setId(newId);
						statement.setInt(1, newId);
						newId += 1;
					} else
					{
						statement.setInt(1, password.getId());
					}
					////
					statement.setString(2, password.getDescription());
					statement.setString(3, password.getUsername());
					statement.setString(4, password.getEncryptedPassword());
					statement.addBatch();
				}
			}
			statement.execute();
			statementRelation.execute();

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

	@Override
	public void delete(Password t) throws UninitializedException, Exception
	{
		checkThrowException();

		String sql = "DELETE FROM " + DatabaseConstants.PasswordConstants.passwordTable + " " + "WHERE "
				+ DatabaseConstants.PasswordConstants.idColumn + " = ?;";

		String sqlRelation = "DELETE FROM " + DatabaseConstants.RelationConstants.relationTable + " " + "WHERE "
				+ DatabaseConstants.RelationConstants.passwordColumn + " = ?;";

		Connection connection = manager.getConnection();
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setInt(1, t.getId());
			statement.execute();
			statement.close();

			statement = connection.prepareStatement(sqlRelation);
			statement.setInt(1, t.getId());
			statement.execute();

			if (statement.getUpdateCount() == 0)
			{
				throw new PasswordNotFoundException(t.getId() + "");
			}
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
	 * Exceção lançada quando uma senha buscada não existe.
	 * 
	 * @author Davi
	 */
	public class PasswordNotFoundException extends Exception
	{
		private static final long serialVersionUID = 7837769402553683735L;

		public PasswordNotFoundException(String id)
		{
			super("Password " + id + " not found!");
		}
	}
}
