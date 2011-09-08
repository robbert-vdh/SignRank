package me.coolblinger.signrank;

import com.nijiko.permissions.PermissionHandler;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import me.coolblinger.signrank.listeners.SignRankBlockListener;
import me.coolblinger.signrank.listeners.SignRankPlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class SignRank extends JavaPlugin{
	public Logger log = Logger.getLogger("Minecraft");
	public String pluginName;
	public PermissionHandler permissions;
	private SignRankPlayerListener playerListener = new SignRankPlayerListener(this);
	private SignRankBlockListener blockListener = new SignRankBlockListener(this);
	public SignRankPermissionsBukkit permissionsBukkit = new SignRankPermissionsBukkit();
	public SignRankPermissionsBukkitYML signRankPermissionsBukkitYML = new SignRankPermissionsBukkitYML(this);

	public void onDisable() {
		log.info("SignRank has been disabled.");
	}

	public void onEnable() {
		PluginDescriptionFile pdFile = this.getDescription();
		PluginManager pm = this.getServer().getPluginManager();
		Plugin permissionsBukkitPlugin = pm.getPlugin("PermissionsBukkit");
		Plugin permissions3Plugin = pm.getPlugin("Permissions");
		if (permissionsBukkitPlugin != null) {
			pluginName = "PermissionsBukkit";
			permissionsBukkit.plugin = (PermissionsPlugin) permissionsBukkitPlugin;
		} else if (permissions3Plugin != null) {
			if (permissions3Plugin.getDescription().getVersion().startsWith("3.") && permissions3Plugin instanceof PermissionHandler) {
				pluginName = "Permissions3";
				permissions = (PermissionHandler) permissions3Plugin;
			} else {
				log.severe("No support Permissions plugin has been found, SignRank will support itself.");
			}
		} else {
			log.severe("No support Permissions plugin has been found, SignRank will support itself.");
		}
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		log.info(pdFile.getName() + " version " + pdFile.getVersion() + " loaded!");
		try {
			updateCheck();
		} catch (Exception e) {
			log.severe("SignRank could not check for updates.");
		}
	}

	public void updateCheck() throws IOException {
		URL url = new URL("http://dl.dropbox.com/u/677732/uploads/SignRank.jar");
		int urlSize = url.openConnection().getContentLength();
		File pluginFile = getFile();
		if (!pluginFile.exists()) {
			log.severe("SignRank has not been installed correctly");
			return;
		}
		long pluginFileSize = pluginFile.length();
		if (urlSize != pluginFileSize) {
			BukkitScheduler bScheduler = this.getServer().getScheduler();
			bScheduler.scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					log.warning("There has been an update for SignRank.");
				}
			}, 600);
		}
	}

	public void initConfig() {
		Configuration config = getConfiguration();
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

	public String readString(String path) {
		Configuration config = getConfiguration();
		config.load();
		return config.getString(path);
	}

	public boolean readBoolean(String path) {
		Configuration config = getConfiguration();
		config.load();
		return config.getBoolean(path, false);
	}

	public boolean hasPermission(Player player, String permission) {
		if (pluginName.equals("Permissions3")) {
			return permissions.has(player, permission);
		} else if (pluginName.equals("PermissionsBukkit")) {
			return player.hasPermission(permission);
		} else {
			return false;
		}
	}
}
