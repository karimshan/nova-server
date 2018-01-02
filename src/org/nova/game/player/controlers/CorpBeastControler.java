package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;

public class CorpBeastControler extends Controller {

	@Override
	public void start() {

	}

	@Override
	public boolean processObjectClick1(GlobalObject object) {
		if (object.getId() == 37929) {
			removeControler();
			player.stopAll();
			player.setLocation(new Location(2970, 4384, 0));
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
	}

	@Override
	public boolean sendDeath() {
		removeControler();
		return true;
	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

}
