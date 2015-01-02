package vazkii.minetunes.gui.playlist;

import vazkii.minetunes.gui.GuiMineTunes;
import vazkii.minetunes.playlist.PlaylistList;

public class GuiPlaylistManager extends GuiMineTunes {

	public static int selectedPlaylist = 0;
	public static int selectedSong = 0;

	GuiPlaylistSlot playlistSlot;
	GuiMusicSlot musicSlot;

	@Override
	public void initGui() {
		playlistSlot = new GuiPlaylistSlot(this);
		musicSlot = new GuiMusicSlot(this);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		playlistSlot.drawScreen(mx, my, partialTicks);
		musicSlot.drawScreen(mx, my, partialTicks);

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
	
	public int getSelectedPlaylist() {
		return selectedPlaylist;
	}
	
	public void selectSong(int song) {
		selectedSong = song;
	}
	
	public int getSelectedSong() {
		return selectedSong;
	}
	
}
