package database;

public abstract class DatabaseConstants
{
	public enum PasswordConstants
	{
		idColumn("id"), descriptionColumn("description"), usernameColumn("username"), passwordColumn("password"),
		passwordTable("passwords");
		
		private String string;
		
		private PasswordConstants(String string)
		{
			this.string = string;
		}
		
		public String getString()
		{
			return string;
		}
		
		@Override
		public String toString()
		{
			return string;
		}
	}
	
	public enum CategoryConstants
	{
		idColumn("id"), nameColumn("name"), categoryTable("categories");
		
		private String string;
		
		private CategoryConstants(String string)
		{
			this.string = string;
		}
		
		public String getString()
		{
			return string;
		}
		
		@Override
		public String toString()
		{
			return string;
		}
	}
	
	public enum RelationConstants
	{
		relationTable("category_passwords"), categoryColumn("category_id"), passwordColumn("password_id");
		
		private String string;
		
		private RelationConstants(String string)
		{
			this.string = string;
		}
		
		public String getString()
		{
			return string;
		}
		
		@Override
		public String toString()
		{
			return string;
		}
	}
}
