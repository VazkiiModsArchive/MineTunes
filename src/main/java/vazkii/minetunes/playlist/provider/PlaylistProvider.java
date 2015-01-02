package vazkii.minetunes.playlist.provider;

import java.io.File;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
				EnumChatFormatting format = EnumChatFormatting.RED;
				if(percent > 50)
					format = EnumChatFormatting.YELLOW;
				if(percent > 90)
					format = EnumChatFormatting.GREEN;
				
				return String.format(StatCollector.translateToLocal("minetunes.playlist.generator.processing"), processedFiles, foundFiles) + format + " (" + percent + "%)";
			} else return String.format(StatCollector.translateToLocal("minetunes.playlist.generator.scanning"), foundFiles);
 		} else return StatCollector.translateToLocal("minetunes.playlist.generator.starting");
	}
	
}
