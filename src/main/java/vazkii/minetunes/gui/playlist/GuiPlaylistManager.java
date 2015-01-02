package vazkii.minetunes.gui.playlist;

import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.gui.GuiMineTunes;
import vazkii.minetunes.playlist.Playlist;
import vazkii.minetunes.playlist.PlaylistList;

public class GuiPlaylistManager extends GuiMineTunes {

	public volatile static int currentPlaylist = 0;
	public volatile static int currentSong = 0;
	
	static int selectedPlaylist = 0;
	static int selectedSong = 0;
	
	GuiPlaylistSlot playlistSlot;
	GuiMusicSlot musicSlot;

	@Override
	public void initGui() {
		playlistSlot = new GuiPlaylistSlot(this);
		musicSlot = new GuiMusicSlot(this);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		musicSlot.drawScreen(mx, my, partialTicks);
		playlistSlot.drawScreen(mx, my, partialTicks);

		drawRect(0, 0, width, getTopSize(), 0xFF000000);
		drawRect(197, getTopSize(), 200, height, 0xFF000000);

		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopSize() {
		return 80;
	}
	
	public void selectPlaylist(int playlist) {
		selectedPlaylist = playlist;
		selectedSong = 0;
	}
	
	public int getSelectedPlaylistIndex() {
		return selectedPlaylist;
	}
	
	public void selectSong(int song) {
		selectedSong = song;
	}
	
	public int getSelectedSong() {
		return selectedSong;
	}
	
	public Playlist getSelectedPlaylist() {
		return getPlaylist(getSelectedPlaylistIndex());
	}
	
	public static void selectCurrentPlaylist(int playlist, int song) {
		currentPlaylist = playlist;
		currentSong = song;
	}
	
	public static int getCurrentPlaylistIndex() {
		return currentPlaylist;
	}
	
	public static void selectCurrentSong(int song) {
		currentSong = song;
	}
	
	public static int getCurrentSong() {
		return currentSong;
	}
	
	public static Playlist getCurrentPlaylist() {
		return getPlaylist(getCurrentPlaylistIndex());
	}
	
	public static Playlist getPlaylist(int index) {
		if(index >= PlaylistList.playlistNames.size())
			return null;
		
		String name = PlaylistList.playlistNames.get(index);
		return PlaylistList.playlists.get(name);
	}
	
}
