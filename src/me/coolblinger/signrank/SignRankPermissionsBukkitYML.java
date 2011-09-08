package me.coolblinger.signrank;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignRankPermissionsBukkitYML {
	private final File permissionsFile = new File("plugins" + File.separator + "PermissionsBukkit" + File.separator + "config.yml");
	private final SignRank plugin;

	public SignRankPermissionsBukkitYML (SignRank instance) {
		plugin = instance;
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored"})
	private Configuration config() {
		if (!permissionsFile.exists()) {
			try {
				permissionsFile.getParentFile().createNewFile();
				permissionsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Configuration config = new Configuration(permissionsFile);
		config.load();
		return config;
	}

	@SuppressWarnings("unchecked")
	public List<String> getGroups(Player player) {
		Configuration config = config();
		config.load();
		List<String> groups = (List<String>)config.getProperty("users." + player.getName() + ".groups");
		if (groups == null) {
			groups = new ArrayList<String>();
			groups.add("default");
		}
		return groups;
	}

	@SuppressWarnings("unchecked")
	public void changeGroup(Player player, String to) {
		Configuration config = config();
		config.load();
		List<String> groups = new ArrayList<String>();
		groups.add(to);
		config.setProperty("users." + player.getName() + ".groups", groups);
		config.save();
		plugin.permissionsBukkit.reloadPermissions();
	}


}
