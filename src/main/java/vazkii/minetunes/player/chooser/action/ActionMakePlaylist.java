package vazkii.minetunes.player.chooser.action;

import java.io.File;

import vazkii.minetunes.MineTunes;
import vazkii.minetunes.playlist.Playlist;
import vazkii.minetunes.playlist.PlaylistList;

public class ActionMakePlaylist implements ISelectorAction {

	public static final ActionMakePlaylist instance = new ActionMakePlaylist();
	String name;
	
	@Override
	public void select(File file) {
		PlaylistList.loadPlaylist(file, name);
		ActionDebug.instance.select(file);
	}
	
	public ISelectorAction withName(String name) {
		this.name = name;
		return this;
	}

}
