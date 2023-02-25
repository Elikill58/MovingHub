package com.elikill58.movinghub;

import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;

public class ServerGroup {

	private final List<String> names, contains;
	
	public ServerGroup(Configuration config) {
		this.names = config.getStringList("names");
		this.contains = config.getStringList("contains");
	}
	
	public List<String> getContains() {
		return contains;
	}
	
	public List<String> getNames() {
		return names;
	}
	
	public List<ServerInfo> getServers() {
		return ProxyServer.getInstance().getServers().values().stream().filter(si -> {
			if(names.contains(si.getName()))
				return true;
			for(String s : contains)
				if(si.getName().contains(s))
					return true;
			return false;
		}).collect(Collectors.toList());
	}
}
