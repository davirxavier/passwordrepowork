package database;

import java.util.HashMap;
import java.util.List;

/**
 * Classe para um DAO g�nerico para uma entidade que � filho num relacionamento
 * de pai e filho. Alguns m�todos da mesma necessitam do ID do pai.
 * 
 * @author Davi
 * @implNote Implementa Initializable com um IConnectionManager.
 * @param <T>
 * @param <S>
 */
public interface IDAOInner<T, S> extends Initializable<IConnectionManager>
{
	/**
	 * Atualiza o T especificado como par�metro no banco de dados usando o id do
	 * mesmo.
	 * 
	 * @param id
	 * @param t
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void update(String id, T t) throws UninitializedException, Exception;

	/**
	 * Insere ou sobrescreve o T especificado no banco de dados. Necessita do ID do
	 * objeto pai.
	 * 
	 * @param t        - T: Objeto � ser escrito.
	 * @param parentId - String: ID do objeto pai.
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void insert(String parentId, T t) throws UninitializedException, Exception;

	/**
	 * Escreve ou sobrescreve todas os T do banco de dados com as especificadas como
	 * par�metro.
	 * 
	 * @param values - HashMap de String com Listas de T: Map com listas de T que
	 *               s�o relacionadas com uma String qualquer. Essa string
	 *               representa o ID do objeto pai de todos dessa lista.
	 */
	public void insertAll(HashMap<String, List<T>> values) throws UninitializedException, Exception;

	/**
	 * Analisa se o segredo passado est� correto. Se a tabela de senhas estiver
	 * vazia retorna verdadeiro.
	 * 
	 * @param s     - S: Segredo(tipo definido na classe).
	 * @param clear - boolean: Se o segredo pode ser limpado ap�s a checagem.
	 * @return boolean: Verdadeiro se correto.
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public boolean checkSecret(S s, boolean clear) throws UninitializedException, Exception;

	/**
	 * Deleta o T referenciado do banco de dados.
	 * 
	 * @param t
	 * @throws UninitializedException
	 * @throws Exception
	 */
	public void delete(T t) throws UninitializedException, Exception;
}
