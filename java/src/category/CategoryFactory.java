package category;

/**
 * Classe para instanciação de categorias, pode ser expandida no futuro. Não
 * necessita de interfaces, será expandida com métodos novos.
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
