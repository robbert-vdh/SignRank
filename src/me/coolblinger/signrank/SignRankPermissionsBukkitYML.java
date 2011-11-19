package me.coolblinger.signrank;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
	private YamlConfiguration config() {
		if (!permissionsFile.exists()) {
			try {
				permissionsFile.getParentFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(permissionsFile);
		return config;
	}

	@SuppressWarnings("unchecked")
	public List<String> getGroups(Player player) {
		YamlConfiguration config = config();
		List<String> groups = (List<String>)config.getList("users." + player.getName() + ".groups");
		if (groups == null) {
			groups = new ArrayList<String>();
			groups.add("default");
		}
		return groups;
	}

	@SuppressWarnings("unchecked")
	public void changeGroup(Player player, String to) {
		YamlConfiguration config = config();
		List<String> groups = new ArrayList<String>();
		groups.add(to);
		config.set("users." + player.getName() + ".groups", groups);
		try {
			config.save(permissionsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		plugin.permissionsBukkit.reloadPermissions();
	}
}
