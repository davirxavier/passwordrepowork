package category;

import java.util.*;

import encrypters.AESEncrypter;
import encrypters.Encrypter;

/**
 * Classe que serve com biblioteca de senhas.
 */
public class Category
{
	private static Encrypter encrypter = AESEncrypter.getInstance();
	private List<Password> pwds;
	private String name;

	/**
	 * Constructors
	 */
	public Category(String name)
	{
		pwds = new ArrayList<Password>();
		this.name = name;
	}
	public Category(List<Password> list, String name)
	{
		pwds = list;
		this.name = name;
	}

	/*
	 * Getters e setters, nada de especial neles.
	 */
	public List<Password> getPasswords()
	{
		return pwds;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	/*
	 * 
	 */

	/**
	 * Criptografa uma senha, cria um objeto Password novo, o adiciona na lista da
	 * categoria e o retorna.
	 * 
	 * @param desc     - String: Descrição da senha.
	 * @param username - String: Nome de usuário relacionado à senha.
	 * @param password - char[]: Senha.
	 * @param secret   - char[]: Segredo para criptografia.
	 * @return
	 * @throws Exception
	 */
	public Password newPassword(String desc, String username, char[] password, char[] secret) throws Exception
	{
		String encryptedPassword = encrypter.encrypt(password, secret, false);
		PasswordFactory factory = new PasswordFactory();
		Password ret = factory.create(desc, username, encryptedPassword);
		pwds.add(ret);
		return ret;
	}

	/**
	 * Retorna uma senha descriptografada usando o segredo. Limpar o vetor depois do
	 * uso.
	 * 
	 * @param pos    - int: Posição da senha desejada na categoria.
	 * @param secret - char[]: Segredo da senha.
	 * @return
	 * @throws Exception
	 */
	public char[] getDecryptedPassword(int pos, char[] secret) throws Exception
	{
		Password password = pwds.get(pos);

		char[] ret = encrypter.decrypt(password.getEncryptedPassword(), secret, true);
		return ret;
	}
	
	/**
	 * Muda a senha criptografada de um Password.
	 * @param pos
	 * @param password
	 * @param secret
	 * @throws Exception 
	 */
	public void changeEncryptedPassword(int pos, char[] password, char[] secret) throws Exception
	{
		Password p = pwds.get(pos);
		String encryptedpassword = encrypter.encrypt(password, secret, true);
		p.setEncryptedPassword(encryptedpassword);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Category)
		{
			Category category = (Category) obj;
			return name.equals(category.getName());
		} else
			return false;
	}

	@Override
	public String toString()
	{
		return name + pwds.toString();
	}
}