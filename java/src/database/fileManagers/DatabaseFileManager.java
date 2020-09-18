package database.fileManagers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import encrypters.Encrypter;

/**
 * Classe respons�vel pelo gerenciamento do arquivo de dados do aplicativo.
 */
public class DatabaseFileManager implements DBFileManager
{

	private File file;
	private Encrypter encrypter;

	/**
	 * Construtor. Cria o arquivo especificado em "filePath" se o mesmo n�o existir.
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public DatabaseFileManager(String filePath, Encrypter encrypter)
	{
		file = new File(filePath);
		this.encrypter = encrypter;
	}

	/**
	 * Escreve a string "text" no arquivo.
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public void writeToFile(String text, char[] secret, boolean clearSecret) throws Exception
	{
		if (!file.exists())
			file.createNewFile();

		String encryptedString = encrypter.encrypt(text.toCharArray(), secret, clearSecret);

		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(encryptedString);
		fileWriter.close();
	}

	/**
	 * Retorna o conte�do completo do arquivo. Deve levantar uma exe��o caso o
	 * segredo esteja errado.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String readFile(char[] secret, boolean clearSecret) throws Exception
	{
		if (!file.exists())
			file.createNewFile();
		
		FileReader fileReader = new FileReader(file);
		String ret = "";

		int c = -1;
		while ((c = fileReader.read()) != -1)
		{
			char cha = (char) c;
			ret += cha;
		}
		fileReader.close();
		
		if (ret.length() == 0)
		{
			writeToFile("{}", secret, clearSecret);
			return "{}";
		}
		String decriptedString = String.valueOf(encrypter.decrypt(ret, secret, clearSecret));
		/*
		 * Checa se a string descriptografada � um JSON v�lido, sen�o o mesmo vai
		 * levantar uma exce��o, infomando que o segredo est� errado.
		 */
		try
		{
			if (decriptedString.charAt(0) == '[')
			{
				new JSONArray(decriptedString);
			} else if (decriptedString.charAt(0) == '{')
			{
				new JSONObject(decriptedString);
			}
		}
		catch (Exception e)
		{
			throw new Exception("JSON is not valid. Secret is probably wrong.");
		}
		
		return decriptedString;
	}
}