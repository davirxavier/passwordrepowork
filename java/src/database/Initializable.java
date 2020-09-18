package database;

/**
 * Definição para classes que devem ser inicializadas antes de qualquer operação
 * das mesmas. Tais classes devem levantar a exceção "UninitializedException"
 * caso a mesma ainda não tenha sido inicializada pelo método init.
 * 
 * @author Davi
 *
 * @param <T>
 */
public interface Initializable<T>
{
	/**
	 * Método para inicialização da classe, passando um T qualquer necessário para o
	 * funcionamento da mesma.
	 * 
	 * @param t
	 */
	public void init(T t);

	/**
	 * Diz se a classe já foi inicializada corretamente utilizando o método init.
	 * 
	 * @return
	 */
	public boolean isInitialized();

	/**
	 * Exceção jogada quando os métodos da classe mãe são utilizados antes da mesma
	 * ser inicializada corretamente pelo método init.
	 * 
	 * @author Davi
	 */
	public class UninitializedException extends Exception
	{
		private static final long serialVersionUID = 3079315354917669671L;

		public UninitializedException()
		{
			super("Class was not initilized using \"init\" method!");
		}
		
		public UninitializedException(String clas)
		{
			super("Class "+ clas + " was not initilized using \"init\" method!");
		}
	}
}
