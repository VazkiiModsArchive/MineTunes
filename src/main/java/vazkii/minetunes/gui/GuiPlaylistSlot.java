package vazkii.minetunes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import vazkii.minetunes.playlist.Playlist;
import vazkii.minetunes.playlist.PlaylistList;

public class GuiPlaylistSlot extends GuiScrollingListMT {

	GuiPlaylistManager parent;

	public GuiPlaylistSlot(GuiPlaylistManager parent) {
		super(197, parent.height - parent.getTopSize(), parent.getTopSize(), 0, 30);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return PlaylistList.playlists.size();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		parent.selectPlaylist(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return parent.getSelectedPlaylistIndex() == i;
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		String name = PlaylistList.playlistNames.get(i);
		Playlist playlist = PlaylistList.playlists.get(name);

		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		font.drawStringWithShadow(name, j + 20 - listWidth, k + 3, 0xFFFFFF);
		font.drawStringWithShadow(playlist.metadataList.size() + " Songs", j + 25 - listWidth, k + 15, 0xDDDDDD);
	}
}
