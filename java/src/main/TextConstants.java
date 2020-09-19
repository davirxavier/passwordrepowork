package main;

public enum TextConstants
{
	nameShort("EPR"),
	nameLong("Encrypted Password Repository");
	
	private String string;
	
	private TextConstants(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString()
	{
		return string;
	}
}
