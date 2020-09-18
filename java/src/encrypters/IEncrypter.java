package encrypters;

/**
 * Interface para objetos usados para criptografar e descriptografar texto.
 */
public interface IEncrypter
{

	/**
	 * Criptografa o texto "text" com o segredo "secret". Pode limpar os vetores de
	 * entrada se "clearArrays" for especificada com true.
	 * 
	 * @param text        - char[]
	 * @param secret      - char[]
	 * @param clearArrays - boolean
	 * @return
	 * @throws Exception
	 */
	public String encrypt(char[] text, char[] secret, boolean clearArrays) throws Exception;

	/**
	 * Descriptografa o texto "text" com o segredo "secret". Pode limpar os vetores
	 * de entrada se "clearSecret" for especificada com true.
	 * 
	 * @param text        - char[]
	 * @param secret      - char[]
	 * @param clearSecret - boolean
	 * @return
	 * @throws Exception
	 */
	public char[] decrypt(String text, char[] secret, boolean clearSecret) throws Exception;

}