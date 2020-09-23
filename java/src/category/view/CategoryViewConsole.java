package category.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import category.CategoryHandlerResponse;
import category.CategoryInputHandler;
import category.IView;
import exceptions.database.CategoryAlreadyExistsException;
import exceptions.database.IncorrectSecretException;

/**
 * Classe de view, não documentada porque não importa muito pra esse trabalho.
 */
public class CategoryViewConsole implements IView
{
	private CategoryInputHandler handler;
	private static Scanner scanner;
	private List<CategoryHandlerResponse> lastResponses;
	private static final String DIVISOR_STRING = "------------------------------------------------------";

	public CategoryViewConsole()
	{
		scanner = new Scanner(System.in);
		lastResponses = new ArrayList<>();
	}

	/**
	 * @param categoryPassword
	 * @return
	 */
	@Override
	public void show(List<CategoryHandlerResponse> responses)
	{
		cls();

		lastResponses = responses;

		System.out.println(DIVISOR_STRING);
		System.out.println("| Categorias e senhas");
		System.out.println(DIVISOR_STRING);

		responses.forEach(v ->
		{
			System.out.println("| " + v.getCatId() + " - " + v.getCategoryName() + ":");

			v.getPasswords().forEach(s ->
			{
				System.out.println("  |_ " + s);
			});
		});
		if (responses.isEmpty())
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
			int input = parseNumber("Escolha uma opção: ");

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
							if (catpos < 0 || catpos >= lastResponses.size())
							{
								System.out.println("Essa categoria não existe. Tente novamente...");
								continue;
							}

							int passpos = parseNumber("Insira a senha desejada: ");
							if (passpos < 0 || passpos >= getPassCount(catpos))
							{
								System.out.println("Essa senha não existe. Tente novamente...");
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
									System.out.println("| A senha descriptografada é: ");
									System.out.println("| " + String.valueOf(passString));
									System.out.println(DIVISOR_STRING);
									waitScanner();
									cls();
									show(lastResponses);
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
						int catpos = parseNumber("Insira o número da categoria desejada:");
						if (catpos < 0 || catpos >= lastResponses.size())
						{
							System.out.println("Essa categoria não existe. Tente novamente...");
							continue;
						}

						int passpos = parseNumber("Insira o número da senha à ser excluida:");
						if (passpos < 0 || passpos >= getPassCount(catpos))
						{
							System.out.println("Essa senha não existe. Tente novamente...");
							continue;
						}

						do
						{
							System.out.println("Tem certeza que deseja apagar a senha " + passpos + "?");
							System.out.println("Se não, digite a letra N, se sim digite qualquer outra coisa.");
							String choice = scanner.nextLine();
							if (!choice.isEmpty() && (choice.charAt(0) == 'n' || choice.charAt(0) == 'N'))
							{
								show(lastResponses);
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
						if (lastResponses.size() == 0)
						{
							System.out.println("Você não criou nenhuma categoria ainda!");
							return;
						}

						System.out.println("Insira uma descrição para a nova senha(200 caracteres): ");
						String desc = scanner.nextLine();
						if (desc.length() > 200)
						{
							System.out.println("Descrição muito grande! Tente novamente...");
							continue;
						}
						System.out.println("Insira um nome de usuário ou e-mail associado com essa senha: ");
						String user = scanner.nextLine();

						System.out.println("Insira a senha à ser guardada(30 caracteres): ");
						char[] pass = scanner.nextLine().toCharArray();

						int catpos = -1;
						do
						{
							System.out.println("Insira o número da categoria desejada: ");
							String catposString = scanner.nextLine();
							try
							{
								catpos = Integer.parseInt(catposString);
								if (catpos < 0 || catpos > lastResponses.size() - 1)
								{
									System.out.println("Categoria inexistente! Tente novamente...");
									continue;
								}
							} catch (NumberFormatException e)
							{
								System.out.println("Número inválido! Tente novamente...");
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
							System.out.println("Essa categoria já existe! Tente novamente...");
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
						int catpos = parseNumber("Insira o número da categoria desejada:");
						if (catpos < 0 || catpos >= lastResponses.size())
						{
							System.out.println("Essa categoria não existe. Tente novamente...");
							continue;
						}

						System.out.println("Tem certeza que deseja apagar a categoria " + catpos
								+ "? Todas as senhas relacionadas a ela serão excluidas.");
						System.out.println("Se não, digite a letra N, se sim digite qualquer outra coisa.");
						String choice = scanner.nextLine();
						if (!choice.isEmpty() && (choice.charAt(0) == 'n' || choice.charAt(0) == 'N'))
						{
							show(lastResponses);
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
						int catpos = parseNumber("Insira o número da categoria que você deseja editar(-1 para cancelar):");
						if (catpos == -1)
						{
							show(lastResponses);
							return;
						}
						if (catpos < 0 || catpos >= lastResponses.size())
						{
							System.out.println("Essa categoria não existe. Tente novamente...");
							continue;
						}
						
						System.out.println("Insira um nome novo para a categoria " + catpos + ":");
						String newname = scanner.nextLine();
						if (newname.isEmpty() || newname.length() > 30)
						{
							System.out.println("Nome inválido. Tente novamente...");
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
						int catpos = parseNumber("Insira o número da categoria desejada(-1 para cancelar):");
						if (catpos == -1) 
						{
							show(lastResponses);
							return;
						}
						if (catpos < 0 || catpos >= lastResponses.size())
						{
							System.out.println("Essa categoria não existe. Tente novamente...");
							continue;
						}
						
						int passpos = parseNumber("Insira o número da senha desejada(-1 para cancelar):");
						if (passpos == -1)
						{
							show(lastResponses);
							return;
						}
						if(passpos < 0 || passpos >= getPassCount(catpos))
						{
							System.out.println("Essa senha não existe. Tente novamente....");
							continue;
						}
						
						System.out.println("Insira uma descrição nova para a senha(deixe vazio se não quiser modificar):");
						String desc = scanner.nextLine();
						if (desc.isEmpty()) desc = null;
						
						System.out.println("Insira um usuário novo para ser relacionado a senha(deixe vazio se não quiser modificar):");
						String user = scanner.nextLine();
						if (user.isEmpty()) user = null;
						
						System.out.println("Insira a nova senha à ser guardada(deixe vazio se não quiser modificar):");
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
				System.out.println("Número inválido! Tente novamente...");
			}
		} while (true);
	}

	@SuppressWarnings("unchecked")
	private int getPassCount(int catpos)
	{
		for (int i = 0; i < lastResponses.size(); i++)
		{
			CategoryHandlerResponse r = lastResponses.get(i);
			if (r.getCatId() == catpos)
			{
				return r.getPasswords().size();
			}
		}
		
		return -1;
	}

	private int getAllPassCount()
	{
		int ret = 0;
		for (CategoryHandlerResponse response : lastResponses)
		{
			ret += lastResponses.size();
		}

		return ret;
	}

	private boolean checkForPasswords()
	{
		if (getAllPassCount() == 0)
		{
			System.out.println("Você não adicionou nenhuma senha ainda!");
			waitScanner();
			show(lastResponses);
			return false;
		}
		return true;
	}
	
	private boolean checkForCategories()
	{
		if (lastResponses.size() == 0)
		{
			System.out.println("Você ainda não adicionou nenhuma categoria!");
			waitScanner();
			show(lastResponses);
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