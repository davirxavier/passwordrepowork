package category;

/**
 * Classe para instancia��o de categorias, pode ser expandida no futuro. N�o
 * necessita de interfaces, ser� expandida com m�todos novos.
 */
public class CategoryFactory
{
	public Category create(String name, int id)
	{
		return new Category(name, id);
	}
	
	public Category create(String name)
	{
		return new Category(name);
	}
}
