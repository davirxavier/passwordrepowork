package database;

import java.io.IOException;
import java.util.List;

/**
 * Interface para um DAO gen�rico.
 * 
 * @author Davi
 * @implNote Implementa Initializable com um IConnectionManager.
 * @param <T>: Entidade do DAO.
 */
public interface IDAO<T> extends Initializable<IConnectionManager>
{
	/**
	 * Retorna todos os T do banco de dados.
	 * 
	 * @return Lista de T
	 * @throws IOException
	 * @throws UninitializedException
	 */
	public List<T> getAll() throws UninitializedException, Exception;

	/**
	 * Retorna T do id especificado(String por compatibilidade).
	 * 
	 * @param id
	 * @return Inst�ncia de T
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public T get(String id) throws UninitializedException, Exception;

	/**
	 * Escreve ou sobrescreve todas os T do banco de dados com as
	 * especificadas como par�metro.
	 * 
	 * @param values - List de T: Lista de T � serem escritas.
	 */
	public void insertAll(List<T> values) throws UninitializedException, Exception;

	/**
	 * Insere ou sobrescreve o T especificado no banco de dados.
	 * 
	 * @param t
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void insert(T t) throws UninitializedException, Exception;

	/**
	 * Deleta o T especificado no banco de dados.
	 * 
	 * @param t
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void delete(T t) throws UninitializedException, Exception;

	/**
	 * Atualiza o T especificado no banco de dados(deve ignorar o id de T).
	 * 
	 * @param t
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void update(T t) throws UninitializedException, Exception;
	
}
