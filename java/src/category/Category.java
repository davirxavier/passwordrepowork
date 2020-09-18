package category;

import java.util.ArrayList;
import java.util.List;

import encrypters.AESEncrypter;
import encrypters.IEncrypter;

/**
 * Classe que serve como biblioteca de senhas.
 */
public class Category
{
	public static final int DEFAULT_ID = -1;
	private static IEncrypter encrypter = AESEncrypter.getInstance();
	private List<Password> pwds;
	private int id;
	private String name;

	/**
	 * Constructors
	 */
	public Category(String name)
	{
		pwds = new ArrayList<Password>();
		this.name = name;
		this.id = DEFAULT_ID;
	}
	public Category(String name, int id)
	{
		pwds = new ArrayList<Password>();
		this.name = name;
		this.id = id;
	}
	public Category(List<Password> list, int id, String name)
	{
		pwds = list;
		this.name = name;
		this.id = id;
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
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
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
	
	/**
	 * Retorna um Password na posição especificada em pos.
	 * @param pos
	 * @return
	 */
	public Password getPassword(int pos)
	{
		return pwds.get(pos);
	}
	
	/**
	 * Retorna um Password com o ID especificado ou null caso o mesmo não exista nessa categoria.
	 * @param id
	 * @return
	 */
	public Password getPasswordById(int id)
	{
		Password p = null;
		for (Password password : pwds)
		{
			if (id == password.getId())
			{
				p = password;
			}
		}
		
		return p;
	}

	/**
	 * Métodos de objeto.
	 */
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