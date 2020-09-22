package res.img;

public enum ImagePath
{
	CLOSE_BLACK18DP("close_black18dp.png"), ADD_BLOCK_WHITE24DP("add_block_white24dp.png"),
	DELETE_BLACK24dp("delete_black24dp.png"), EDIT_BLACK24DP("edit_black24dp.png"),
	EXPAND_LESS_BLACK24DP("expand_less_black24dp.png"), EXPAND_MORE_BLACK24DP("expand_more_black24dp.png"),
	KEY_WHITE96DP("key_white_96dp.png"), LOGOUT_WHITE24DP("logout_white24dp.png"),
	SEARCH_BLACK18DP("search_black18dp.png"), APPICON("appicon.png");

	private static final String PATH_START = "/res/img/";
	private String path;

	private ImagePath(String path)
	{
		this.path = PATH_START + path;
	}

	public String getPath()
	{
		return path;
	}
}
