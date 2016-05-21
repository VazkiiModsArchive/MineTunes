package vazkii.minetunes.player.chooser.filter;

public class PlaylistFilter extends Filter {

	public static final PlaylistFilter instance = new PlaylistFilter();

	public PlaylistFilter() {
		super("minetunes.player.chooser.filterPlaylist", ".m3u");
	}
	
}
