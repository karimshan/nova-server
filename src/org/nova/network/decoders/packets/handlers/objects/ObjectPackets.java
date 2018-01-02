package org.nova.network.decoders.packets.handlers.objects;

import org.nova.Constants;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.map.DynamicRegion;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.map.MapUtils;
import org.nova.game.map.Region;
import org.nova.game.player.CoordsEvent;
import org.nova.game.player.Player;
import org.nova.kshan.content.skills.construction.RoomChunk;
import org.nova.network.stream.InputStream;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectPackets {
	
	/**
	 * The different option ids
	 */
	public static final int EXAMINE = 0, OPTION_1 = 1, OPTION_2 = 2, 
			OPTION_3 = 3, OPTION_4 = 4, OPTION_5 = 5;
	
	/**
	 * 
	 * @param player
	 * @param stream
	 * @param option
	 */
	public static void process(final Player player, InputStream stream, int option) {
		if(player.cantInteract())
			return;
		@SuppressWarnings("unused")
		final boolean forceRun = stream.readUnsignedByte128() == 1;
		int x = stream.readUnsignedShort128();
		final int id = stream.readInt();
		int y = stream.readUnsignedShortLE();
		final Location tile = new Location(x, y, player.getZ());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		GlobalObject mapObject = Game.getRegion(regionId).getObject(id, tile);
		ObjectDefinition defs = ObjectDefinition.get(id);
		if (player.getHouse().isAtHouse()
				&& player.getHouse().getMapChunks() != null) {
			RoomChunk roomChunk = player.getHouse().getCurrentRoom(player);
			if (roomChunk == null)
				roomChunk = player.getHouse().getCurrentRoom(tile);
			int boundX = player.getHouse().getMapChunks()[0] * 8
					+ roomChunk.getX() * 8;
			int boundY = player.getHouse().getMapChunks()[1] * 8
					+ roomChunk.getY() * 8;
			int realChunkX = roomChunk.getRoom().getChunkX();
			int realChunkY = roomChunk.getRoom().getChunkY();
			Region region = Game.getRegion(MapUtils.encode(
					MapUtils.Structure.REGION, realChunkX / 8, realChunkY / 8));
			for (int nx = 0; nx < 8; nx++) {
				for (int ny = 0; ny < 8; ny++) {
					GlobalObject[] objects = region.getObjects(
							roomChunk.getZ(), (realChunkX & 0x7) * 8 + nx,
							(realChunkY & 0x7) * 8 + ny);
					if (objects != null) {
						for (GlobalObject object : objects) {
							if (object == null)
								continue;
							GlobalObject spawning = new GlobalObject(object);
							int[] coords = DynamicRegion.translate(nx, ny,
									roomChunk.getRotation(), defs.sizeX,
									defs.sizeY, object.getRotation());
							spawning.setType(object.getType());
							spawning.setCoords(new Location(boundX + coords[0],
									boundY + coords[1], roomChunk.getZ()));
							spawning.setRotation((spawning.getRotation() + roomChunk
									.getRotation()) & 0x3);
							for (GlobalObject o : player.getHouse()
									.getTemporaryObjects()) {
								if (o.matches(tile) && o.getId() == id)
									mapObject = o;
								else if (spawning.getId() == id
										&& spawning.matches(tile))
									mapObject = spawning;
							}
						}
					}
				}
			}
		} else {
			if(mapObject == null || mapObject.getId() != id) {
				player.getDialogue().sendMsg("This object is experiencing an error.", "Try to "+(option == EXAMINE ? "examine" : 
					"click")+" the "+ObjectDefinition.get(id).name+" again.", 
						"If this problem persists, please let an administrator know. Thank you!");
				return;
			}
			mapObject = !player.isAtDynamicRegion() ? mapObject : 
				new GlobalObject(id, mapObject.getType(), 
					mapObject.getRotation(), x, y, player.getZ());
		}
		final GlobalObject object = mapObject;
		if(Constants.DEVELOPER_MODE)
			System.out.println("[Object Packets] Object Option "+option+": "+object.getInformation());
		if(option == EXAMINE) {
			ExamineObject.examine(player, object);
			return;
		}
		player.stopAll(false);
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				switch(option) {
					case 1:
						ObjectOption1.process(player, object);
						return;
					case 2:
						ObjectOption2.process(player, object);
						return;
					case 3:
						ObjectOption3.process(player, object);
						return;
					case 4:
						ObjectOption4.process(player, object);
						return;
					case 5:
						ObjectOption5.process(player, object);
						return;
				}
			}
		}, defs.getSizeX(), defs.getSizeY(), object.getRotation()));
	}
}