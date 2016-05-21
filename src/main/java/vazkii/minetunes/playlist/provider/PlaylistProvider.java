package vazkii.minetunes.playlist.provider;

import java.io.File;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.minetunes.playlist.Playlist;

public abstract class PlaylistProvider {

	IProviderStateCallback callback;
	
	int foundFiles = 0;
	int processedFiles = 0;
	String name = "";
	
	public abstract Playlist provide(File file, IProviderStateCallback callback);
	
	public abstract String getDescription();
	
	void updateState() {
		if(callback != null)
			callback.setState(generateProgressState());
	}
	 	
	String generateProgressState() {
		if(foundFiles > 0) {
			if(processedFiles > 0) {
				int percent = (int) (((float) processedFiles / (float) foundFiles) * 100);
				TextFormatting format = TextFormatting.RED;
				if(percent > 50)
					format = TextFormatting.YELLOW;
				if(percent > 90)
					format = TextFormatting.GREEN;
				
				return I18n.format("minetunes.playlist.generator.processing", processedFiles, foundFiles) + format + " (" + percent + "%)";
			} else return I18n.format("minetunes.playlist.generator.scanning", foundFiles);
 		} else return I18n.format("minetunes.playlist.generator.starting");
	}
	
}
