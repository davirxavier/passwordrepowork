package category;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import exceptions.database.CategoryAlreadyExistsException;
import exceptions.database.IncorrectSecretException;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Classe de view, n�o documentada porque n�o importa muito pra esse trabalho.
 */
public class CategoryView implements IView
{
	private CategoryInputHandler handler;
	private static Scanner scanner;
	private HashMap<String, List<String>> lastCategoriesHashMap;
	private static final String DIVISOR_STRING = "------------------------------------------------------";

	public CategoryView()
	{
		scanner = new Scanner(System.in);
		lastCategoriesHashMap = new HashMap<>();
	}

	/**
	 * @param categoryPassword
	 * @return
	 */
	@Override
	public void show(HashMap<String, List<String>> categoryPassword)
	{
		cls();

		lastCategoriesHashMap = categoryPassword;

		System.out.println(DIVISOR_STRING);
		System.out.println("| Categorias e senhas");
		System.out.println(DIVISOR_STRING);

		SimpleIntegerProperty catNum = new SimpleIntegerProperty(0);
		categoryPassword.forEach((k, v) ->
		{
			System.out.println("| " + catNum.get() + " - " + k + ":");

			SimpleIntegerProperty passNum = new SimpleIntegerProperty(0);
			v.forEach(s ->
			{
				System.out.println("  |_ " + passNum.get() + " - " + s);
				passNum.set(passNum.get() + 1);
			});

			catNum.set(catNum.get() + 1);
		});
		if (categoryPassword.isEmpty())
		{
			System.out.println("| Vazio");
		}

		System.out.println(DIVISOR_STRING);
	}

	@Override
	public char[] askForSecret(String textToShow)
	{
		System.out.println("Insira a senha mestra:");
		String input = scanner.nextLine();
		return input.toCharArray();
	}

	public void startMainLoop()
	{
		do
		{
			System.out.println(
					"1 - Visualizar Senha\n2 - Excluir senha\n3 - Nova senha\n4 - Nova categoria\n5 - Excluir categoria\n6 - Editar categoria\n7 - Editar senha\n8 - Sair");
			int input = parseNumber("Escolha uma op��o: ");

			input(input);

		} while (true);
	}

	private void input(final int input)
	{
		if (handler == null)
		{
			return;
		}

		do
		{
			switch (input)
			{
				case 1:
					do
					{
						if (!checkForPasswords())
							return;
						try
						{
							int catpos = parseNumber("Insira a categoria onde a senha se encontra: ");
							if (catpos < 0 || catpos >= lastCategoriesHashMap.size())
							{
								System.out.println("Essa categoria n�o existe. Tente novamente...");
								continue;
							}

							int passpos = parseNumber("Insira a senha desejada: ");
							if (passpos < 0 || passpos >= getPassCount(catpos))
							{
								System.out.println("Essa senha n�o existe. Tente novamente...");
								continue;
							}

							System.out.println("Insira sua senha mestra: ");
							char[] passString = "".toCharArray();
							do
							{
								try
								{
									char[] secret = scanner.nextLine().toCharArray();

									passString = handler.handleViewPassword(catpos, passpos, secret);
									System.out.println(DIVISOR_STRING);
									System.out.println("| A senha descriptografada �: ");
									System.out.println("| " + String.valueOf(passString));
									System.out.println(DIVISOR_STRING);
									waitScanner();
									cls();
									show(lastCategoriesHashMap);
									return;
								} catch (IncorrectSecretException e)
								{
									System.out.println("Senha mestra incorreta! Tente novamente...");
									continue;
								} catch (Exception e)
								{
									return;
								}
							} while (true);

						} catch (Exception e1)
						{
							e1.printStackTrace();
						}

					} while (true);

				case 2:
					if (!checkForPasswords())
						return;
					do
					{
						int catpos = parseNumber("Insira o n�mero da categoria desejada:");
						if (catpos < 0 || catpos >= lastCategoriesHashMap.size())
						{
							System.out.println("Essa categoria n�o existe. Tente novamente...");
							continue;
						}

						int passpos = parseNumber("Insira o n�mero da senha � ser excluida:");
						if (passpos < 0 || passpos >= getPassCount(catpos))
						{
							System.out.println("Essa senha n�o existe. Tente novamente...");
							continue;
						}

						do
						{
							System.out.println("Tem certeza que deseja apagar a senha " + passpos + "?");
							System.out.println("Se n�o, digite a letra N, se sim digite qualquer outra coisa.");
							String choice = scanner.nextLine();
							if (!choice.isEmpty() && (choice.charAt(0) == 'n' || choice.charAt(0) == 'N'))
							{
								show(lastCategoriesHashMap);
								return;
							}

							System.out.println("Insira sua senha mestra: ");
							char[] secret = scanner.nextLine().toCharArray();
							try
							{
								handler.handleDeletePassword(catpos, passpos, secret);
								return;
							} catch (IncorrectSecretException e)
							{
								System.out.println("Senha mestra incorreta. Tente novamente...");
								continue;
							} catch (Exception e)
							{
							}
						} while (true);
					} while (true);

				case 3:
					do
					{
						if (lastCategoriesHashMap.size() == 0)
						{
							System.out.println("Voc� n�o criou nenhuma categoria ainda!");
							return;
						}

						System.out.println("Insira uma descri��o para a nova senha(200 caracteres): ");
						String desc = scanner.nextLine();
						if (desc.length() > 200)
						{
							System.out.println("Descri��o muito grande! Tente novamente...");
							continue;
						}
						System.out.println("Insira um nome de usu�rio ou e-mail associado com essa senha: ");
						String user = scanner.nextLine();

						System.out.println("Insira a senha � ser guardada(30 caracteres): ");
						char[] pass = scanner.nextLine().toCharArray();

						int catpos = -1;
						do
						{
							System.out.println("Insira o n�mero da categoria desejada: ");
							String catposString = scanner.nextLine();
							try
							{
								catpos = Integer.parseInt(catposString);
								if (catpos < 0 || catpos > lastCategoriesHashMap.size() - 1)
								{
									System.out.println("Categoria inexistente! Tente novamente...");
									continue;
								}
							} catch (NumberFormatException e)
							{
								System.out.println("N�mero inv�lido! Tente novamente...");
								continue;
							}
							break;
						} while (true);

						do
						{
							System.out.println("Insira sua senha mestra: ");
							char[] secret = scanner.nextLine().toCharArray();

							try
							{
								handler.handleNewPassword(catpos, desc, user, pass, secret);
								return;
							} catch (IncorrectSecretException e)
							{
								System.out.println("Senha mestra incorreta! Tente novamente...");
								continue;
							} catch (Exception e)
							{
								e.printStackTrace();
							}
							break;
						} while (true);
						break;
					} while (true);
					break;

				case 4:
					do
					{
						System.out.print("Insira o nome da categoria nova(menos de 30 caracteres): ");
						String inputString = scanner.nextLine();
						if (inputString.length() > 30)
						{
							System.out.println("Nome muito grande! Tente novamente.");
							continue;
						}

						System.out.print("Insira sua senha mestra: ");
						String secret = scanner.nextLine();

						try
						{
							handler.handleNewCategory(inputString, secret.toCharArray());
							return;
						} catch (IncorrectSecretException e)
						{
							System.out.println("Senha mestra incorreta! Tente novamente...");
							continue;
						} catch (CategoryAlreadyExistsException e)
						{
							System.out.println("Essa categoria j� existe! Tente novamente...");
							continue;
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						break;
					} while (true);
					break;

				case 5:
					if (!checkForCategories()) return;
					do
					{
						int catpos = parseNumber("Insira o n�mero da categoria desejada:");
						if (catpos < 0 || catpos >= lastCategoriesHashMap.size())
						{
							System.out.println("Essa categoria n�o existe. Tente novamente...");
							continue;
						}

						System.out.println("Tem certeza que deseja apagar a categoria " + catpos
								+ "? Todas as senhas relacionadas a ela ser�o excluidas.");
						System.out.println("Se n�o, digite a letra N, se sim digite qualquer outra coisa.");
						String choice = scanner.nextLine();
						if (!choice.isEmpty() && (choice.charAt(0) == 'n' || choice.charAt(0) == 'N'))
						{
							show(lastCategoriesHashMap);
							return;
						}

						do
						{
							System.out.println("Insira sua senha mestra:");
							char[] secret = scanner.nextLine().toCharArray();

							try
							{
								handler.handleDeleteCategory(catpos, secret);
								return;
							} catch (IncorrectSecretException e)
							{
								System.out.println("Senha mestra incorreta. Tente novamente...");
								continue;
							} catch (Exception e)
							{
							}
						} while (true);
					} while (true);

				case 6:
					if (!checkForCategories()) return;
					do
					{
						int catpos = parseNumber("Insira o n�mero da categoria que voc� deseja editar(-1 para cancelar):");
						if (catpos == -1)
						{
							show(lastCategoriesHashMap);
							return;
						}
						if (catpos < 0 || catpos >= lastCategoriesHashMap.size())
						{
							System.out.println("Essa categoria n�o existe. Tente novamente...");
							continue;
						}
						
						System.out.println("Insira um nome novo para a categoria " + catpos + ":");
						String newname = scanner.nextLine();
						if (newname.isEmpty() || newname.length() > 30)
						{
							System.out.println("Nome inv�lido. Tente novamente...");
							continue;
						}
						
						do
						{
							System.out.println("Insira sua senha mestra:");
							char[] secret = scanner.nextLine().toCharArray();
							try
							{
								handler.handleEditCategory(catpos, newname, secret);
								return;
							}
							catch (IncorrectSecretException e) 
							{
								System.out.println("Senha mestra incorreta. Tente novamente...");
								continue;
							}
							catch (Exception e)
							{
							}
						} while (true);
					} while (true);

				case 7:
					if (!checkForPasswords()) return;
					do
					{
						int catpos = parseNumber("Insira o n�mero da categoria desejada(-1 para cancelar):");
						if (catpos == -1) 
						{
							show(lastCategoriesHashMap);
							return;
						}
						if (catpos < 0 || catpos >= lastCategoriesHashMap.size())
						{
							System.out.println("Essa categoria n�o existe. Tente novamente...");
							continue;
						}
						
						int passpos = parseNumber("Insira o n�mero da senha desejada(-1 para cancelar):");
						if (passpos == -1)
						{
							show(lastCategoriesHashMap);
							return;
						}
						if(passpos < 0 || passpos >= getPassCount(catpos))
						{
							System.out.println("Essa senha n�o existe. Tente novamente....");
							continue;
						}
						
						System.out.println("Insira uma descri��o nova para a senha(deixe vazio se n�o quiser modificar):");
						String desc = scanner.nextLine();
						if (desc.isEmpty()) desc = null;
						
						System.out.println("Insira um usu�rio novo para ser relacionado a senha(deixe vazio se n�o quiser modificar):");
						String user = scanner.nextLine();
						if (user.isEmpty()) user = null;
						
						System.out.println("Insira a nova senha � ser guardada(deixe vazio se n�o quiser modificar):");
						char[] pass = scanner.nextLine().toCharArray();
						if (pass.length == 0) pass = null;
						
						do
						{
							System.out.println("Insira sua senha mestra:");
							char[] secret = scanner.nextLine().toCharArray();
							try
							{
								handler.handleEditPassword(catpos, passpos, desc, user, pass, secret);
								return;
							}
							catch (IncorrectSecretException e)
							{
								System.out.println("Senha mestra incorreta. Tente novamente...");
								continue;
							}
							catch (Exception e)
							{
							}
						} while (true);
					} while (true);

				case 8:
					System.exit(0);
					break;

				default:
					return;
			}
		} while (true);
	}

	public void setInputHandler(CategoryInputHandler handler)
	{
		this.handler = handler;
	}

	private void cls()
	{
		try
		{
			final String os = System.getProperty("os.name");

			if (os.toLowerCase().contains("windows"))
			{
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else
			{
				Runtime.getRuntime().exec("clear");
			}
		} catch (InterruptedException | IOException e)
		{
		}
	}

	private int parseNumber(String text)
	{
		do
		{
			try
			{
				System.out.println(text);

				String numberString = scanner.nextLine();
				int num = Integer.parseInt(numberString);
				return num;
			} catch (NumberFormatException e)
			{
				System.out.println("N�mero inv�lido! Tente novamente...");
			}
		} while (true);
	}

	private int getPassCount(int catpos)
	{
		return ((List<String>) lastCategoriesHashMap.values().toArray()[catpos]).size();
	}

	private int getAllPassCount()
	{
		int ret = 0;
		Object[] array = lastCategoriesHashMap.values().toArray();
		for (int i = 0; i < array.length; i++)
		{
			List<String> list = (List<String>) array[i];
			ret += list.size();
		}

		return ret;
	}

	private boolean checkForPasswords()
	{
		if (getAllPassCount() == 0)
		{
			System.out.println("Voc� n�o adicionou nenhuma senha ainda!");
			waitScanner();
			show(lastCategoriesHashMap);
			return false;
		}
		return true;
	}
	
	private boolean checkForCategories()
	{
		if (lastCategoriesHashMap.size() == 0)
		{
			System.out.println("Voc� ainda n�o adicionou nenhuma categoria!");
			waitScanner();
			show(lastCategoriesHashMap);
			return false;
		}
		return true;
	}

	private void waitScanner()
	{
		System.out.println("Pressione ENTER para continuar...");
		scanner.nextLine();
	}
}