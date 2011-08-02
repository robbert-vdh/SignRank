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
		config.setHeader("#'signText' is the text that has to be on the first line of the sign to in order for it to be a SignRankSign.");
		if (config.getProperty("toGroup") == null) {
			config.setProperty("toGroup", "user");
			config.save();
		}
		if (config.getProperty("signText") == null) {
			config.setProperty("signText", "[SignRank]");
			config.save();
		}
		if (config.getProperty("messages.rankUp") == null) {
			config.setProperty("messages.rankUp", "You've been promoted to 'user'.");
			config.save();
		}
		if (config.getProperty("messages.deny") == null) {
			config.setProperty("messages.deny", "You're already an 'user' or higher.");
			config.save();
		}
	}
}
