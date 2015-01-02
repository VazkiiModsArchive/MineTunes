package vazkii.minetunes.gui.playlist;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import scala.collection.parallel.ParIterableLike.Min;
import vazkii.minetunes.MineTunes;
import vazkii.minetunes.player.chooser.action.ActionPlayMp3;
import vazkii.minetunes.playlist.MP3Metadata;
import vazkii.minetunes.playlist.Playlist;
import vazkii.minetunes.playlist.PlaylistList;

public class GuiMusicSlot extends GuiScrollingListMT {

	GuiPlaylistManager parent;

	public GuiMusicSlot(GuiPlaylistManager parent) {
		super(parent.width - 200, parent.height - parent.getTopSize(), parent.getTopSize(), 200, 40);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		int index = parent.getSelectedPlaylist();
		if(index >= PlaylistList.playlistNames.size())
			return 0;
			
		String name = PlaylistList.playlistNames.get(parent.getSelectedPlaylist());
		Playlist playlist = PlaylistList.playlists.get(name);
		return playlist == null ? 0 : playlist.metadataList.size();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		if(doubleclick) {
			String name = PlaylistList.playlistNames.get(parent.getSelectedPlaylist());
			Playlist playlist = PlaylistList.playlists.get(name);
			MP3Metadata metadata = playlist.metadataList.get(i);
			ActionPlayMp3.instance.play(metadata);
		}
		
		parent.selectSong(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return parent.getSelectedSong() == i;
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		String name = PlaylistList.playlistNames.get(parent.getSelectedPlaylist());
		Playlist playlist = PlaylistList.playlists.get(name);
		if(playlist != null) {
			MP3Metadata metadata = playlist.metadataList.get(i);
			if(metadata != null) {
				FontRenderer font = Minecraft.getMinecraft().fontRenderer;
				boolean selected = isSelected(i);
				
				int s = j + 20 - listWidth;

				boolean playing = false;
				float playedFraction = 0;
				boolean paused = false;
				if(MineTunes.musicPlayerThread != null) {
					MP3Metadata playingMeta = MineTunes.musicPlayerThread.getPlayingMetadata();
					if(playingMeta != null && playingMeta.isEqualFile(metadata)) {
						playing = true;
						playedFraction = MineTunes.musicPlayerThread.getFractionPlayed();
						paused = MineTunes.musicPlayerThread.isPaused();
					}
				}
				
				int colorMain = playing ? 0xAAFFFF : 0xFFFFFF;
				int colorSub = playing ? 0x00AAAA : 0xAAAAAA;
				int colorLength = playing ? paused ? 0xFF5555 : 0x55FF55 : 0x555555;
				int colorBg = playing ? paused ? 0x44440000 : 0x44004400 : 0x44000000;
				
				if(!selected)
					parent.drawRect(s - 6, k, s + listWidth, k + 36, colorBg);
				
				font.drawStringWithShadow(metadata.title, s, k + 3, colorMain);
				font.drawStringWithShadow(metadata.artist, s + 4, k + 13, colorSub);
				font.drawStringWithShadow(metadata.album, s + 4, k + 23, colorSub);
				
				GL11.glScalef(2F, 2F, 2F);
				String length = metadata.length;
				
				if(playing)
					length = MP3Metadata.getLengthStr((int) (metadata.lengthMs * playedFraction)) + "/" + length;
				
				font.drawString(length, (j - 2) / 2 - font.getStringWidth(length), (k + 4) / 2, colorLength);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
			}
		}
	}
}