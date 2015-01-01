package vazkii.minetunes.player.chooser.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.minecraft.util.StatCollector;

public class PlaylistFilter extends Filter {

	public PlaylistFilter() {
		super("minetunes.player.chooser.filterPlaylist", ".m3u");
	}
	
}
