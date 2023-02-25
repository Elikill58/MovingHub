package com.elikill58.movinghub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listeners implements Listener {



	@EventHandler
	public void handleKickEvent(ServerKickEvent e) {
		ProxiedPlayer pp = e.getPlayer();
		Logger log = MovingHub.getInstance().getLogger();
		if (!pp.isConnected()) {
			log.info("Cancelled the redirect of " + pp.getName() + " [No longer connected]");
			return;
		}
		if (MovingHub.getIgnored().getServers().contains(e.getKickedFrom())) {
			log.info("Don't try to redirect " + pp.getName() + " because of bypass server " + e.getKickedFrom().getName());
			return;
		}

		// Detect shutdown.
		String shutdownMsg = BaseComponent.toLegacyText(e.getKickReasonComponent());
		for (String word : new ArrayList<>(MovingHub.getConfig().getStringList("detect-shutdown"))) {
			if (shutdownMsg.contains(word)) {
				List<ServerInfo> allServers = MovingHub.getRedirect().getServers();
				if (e.getKickedFrom() != null)
					allServers.remove(e.getKickedFrom());
				if (allServers.isEmpty()) {
					MovingHub.getInstance().getLogger().info("No server available to redirect " + pp.getName() + ".");
					return;
				}
				ServerInfo srv = allServers.get(new Random().nextInt(allServers.size()));
				MovingHub.getInstance().getLogger().info("Moving " + pp.getName() + " to " + srv.getName() + " (kick reason: '" + shutdownMsg + "')");
				e.setCancelled(true);
				e.setCancelServer(srv);
				return;
			}
		}
		log.info("Can't know if it was a shutdown. " + pp.getName() + " not redirected: " + shutdownMsg);
	}
}
