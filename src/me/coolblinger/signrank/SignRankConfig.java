package me.coolblinger.signrank;

import org.bukkit.util.config.Configuration;
import sun.security.krb5.Config;

import java.io.File;

public class SignRankConfig {
	File configFile = new File("plugins" + File.separator + "SignRank" + File.separator + "config.yml");

	public SignRankConfig() {
		initConfig();
	}

	public Configuration config() {
		try {
			Configuration config = new Configuration(configFile);
			config.load();
			return config;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getString(String path) {
		Configuration config = config();
		config.load();
		return config.getString(path);
	}

	public boolean getBoolean(String path) {
		Configuration config = config();
		config.load();
		return config.getBoolean(path, false);
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored"})
	public void initConfig() {
		configFile.getParentFile().mkdir();
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Configuration config = config();
		config.setHeader("#'signText' is the text that has to be on the first line of the sign to in order for it to be a SignRankSign.\n" +
				"#You have to manually set the groups for every world when using Permissions3.\n" +
				"#Those groups are ignored when 'bypassGroupCheck' is true, players will then be promoted to the group specified on the second line of the sign.");
		if (config.getProperty("PermissionsBukkit.toGroup") == null) {
			config.setProperty("PermissionsBukkit.toGroup", "user");
			config.setProperty("Permissions3.worldName", "groupName");
			config.save();
		}
		if (config.getProperty("signText") == null) {
			config.setProperty("signText", "[SignRank]");
			config.save();
		}
		if (config.getProperty("bypassGroupCheck") == null) {
			config.setProperty("bypassGroupCheck", false);
			config.save();
		}
		if (config.getProperty("messages.rankUp") == null) {
			config.setProperty("messages.rankUp", "You've been promoted to '%group%'.");
			config.save();
		}
		if (config.getProperty("messages.deny") == null) {
			config.setProperty("messages.deny", "You're already in the '%group%' group.");
			config.save();
		}
	}
}
