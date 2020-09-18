package database;

/**
 * Defini��o para classes que devem ser inicializadas antes de qualquer opera��o
 * das mesmas. Tais classes devem levantar a exce��o "UninitializedException"
 * caso a mesma ainda n�o tenha sido inicializada pelo m�todo init.
 * 
 * @author Davi
 *
 * @param <T>
 */
public interface Initializable<T>
{
	/**
	 * M�todo para inicializa��o da classe, passando um T qualquer necess�rio para o
	 * funcionamento da mesma.
	 * 
	 * @param t
	 */
	public void init(T t);

	/**
	 * Diz se a classe j� foi inicializada corretamente utilizando o m�todo init.
	 * 
	 * @return
	 */
	public boolean isInitialized();

	/**
	 * Exce��o jogada quando os m�todos da classe m�e s�o utilizados antes da mesma
	 * ser inicializada corretamente pelo m�todo init.
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
