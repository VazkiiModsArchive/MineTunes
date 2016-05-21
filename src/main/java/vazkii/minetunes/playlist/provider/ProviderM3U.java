package vazkii.minetunes.playlist.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.playlist.MP3Metadata;
import vazkii.minetunes.playlist.Playlist;

public class ProviderM3U extends PlaylistProvider {

	public static final ProviderM3U instance = new ProviderM3U();

	@Override
	public Playlist provide(File file, IProviderStateCallback callback) {
		foundFiles = 0;
		processedFiles = 0;

		this.callback = callback;
		Set<MP3Metadata> metadataSet = new TreeSet();

		List<String> lines = new ArrayList();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				line = line.trim();

				if(!line.startsWith("#") && line.endsWith(".mp3")) {
					lines.add(line);
					foundFiles++;
					updateState();
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File parent = file.getParentFile();
		for(String line : lines) {
			if(!line.startsWith("/"))
				line = "/" + line;
			
			File mp3 = new File(parent, line);
			if(mp3.exists()) {
				try {
					name = line;
					metadataSet.add(new MP3Metadata(processedFiles, mp3));
					processedFiles++;
					updateState();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else GuiDevTools.debugLog(line + " does not exist, skipping");
		}

		Playlist playlist = new Playlist(file, metadataSet);
		return playlist;
	}

	@Override
	public String getDescription() {
		return "M3U Playlist Provider";
	}

}
