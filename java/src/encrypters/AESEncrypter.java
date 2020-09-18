package encrypters;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;

/**
 * Classe que criptografa e descriptografa texto usando o algoritmo AES.
 * Singleton.
 */
public class AESEncrypter implements IEncrypter
{

	private static AESEncrypter instance;
	private static final char FILLCHAR = '\n';
	
	private SecretKeySpecDestroyable secretKey;
	private byte[] key;

	/**
	 * Construtor padrão. Privado.
	 */
	private AESEncrypter()
	{
		secretKey = null;
		key = new byte[1];
	}
	
	/**
	 * Retorna uma instância da classe. Inicializa a classe se ainda for nula.
	 * @return instance
	 */
	public static AESEncrypter getInstance()
	{
		if(instance == null)
		{
			synchronized (AESEncrypter.class)
			{
				if (instance == null)
				{
					instance = new AESEncrypter();
				}
			}
		}
		return instance;
	}

	/**
	 * Define os valores das variáveis responsáveis pela chave secreta.
	 * 
	 * @param secret
	 * @return
	 */
	private void setValues(char[] secret)
	{
		MessageDigest sha = null;
		try
		{
			key = charToByte(secret);
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpecDestroyable(key, "AES");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Limpa os valores das variáveis de classe.
	 * 
	 * @throws DestroyFailedException
	 */
	private void clearValues() throws DestroyFailedException
	{
		secretKey.destroy();
		Arrays.fill(key, (byte) 0);
	}

	/**
	 * Retorna o vetor "chars" em formato de bytes.
	 * 
	 * @param chars
	 * @return
	 */
	private byte[] charToByte(char[] chars)
	{
		ByteBuffer buf = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
		byte[] array = new byte[buf.limit()];
		buf.get(array);
		return array;
	}

	/**
	 * Retorna uma string de "text" criptografada com o segredo "secret".
	 * 
	 * @param text        - char[]
	 * @param secret      - char[]
	 * @param clearArrays - boolean: Limpar os vetores de texto e segredo depois da
	 *                    criptografia.
	 * @return String
	 * @throws Exception
	 */
	@Override
	public String encrypt(char[] text, char[] secret, boolean clearArrays) throws Exception
	{
		String ret = encryptOperation(text, secret);

		if (clearArrays)
		{
			Arrays.fill(text, FILLCHAR);
			Arrays.fill(secret, FILLCHAR);
		}

		clearValues();
		return ret;
	}

	/**
	 * Implementação do método de criptografia. Método privado por motivos de
	 * segurança.
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private String encryptOperation(char[] text, char[] secret) throws Exception
	{
		setValues(secret);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(charToByte(text)));
	}

	/**
	 * Descriptografa o texto criptografado "text" usando o segredo "secret" e o
	 * retorna.
	 * 
	 * @param text
	 * @param secret
	 * @throws Exception
	 * @return
	 */
	@Override
	public char[] decrypt(String text, char[] secret, boolean clearSecret) throws Exception
	{
		char[] ret = decryptOperation(text, secret);

		if (clearSecret)
		{
			Arrays.fill(secret, FILLCHAR);
		}

		clearValues();
		return ret;
	}

	/**
	 * Implementação do método de descriptografia. Método privado por motivos de
	 * segurança.
	 * 
	 * @param text
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	private char[] decryptOperation(String text, char[] secret) throws Exception
	{
		setValues(secret);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(text));
		char[] chars = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++)
		{
			chars[i] = (char) bytes[i];
		}

		return chars;
	}

}