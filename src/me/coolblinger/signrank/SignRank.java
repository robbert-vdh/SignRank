package me.coolblinger.signrank;

import com.platymuus.bukkit.permissions.PermissionsPlugin;
import me.coolblinger.signrank.listeners.SignRankBlockListener;
import me.coolblinger.signrank.listeners.SignRankPlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public class SignRank extends JavaPlugin{
	public Logger log = Logger.getLogger("Minecraft");
	private SignRankPlayerListener playerListener = new SignRankPlayerListener(this);
	private SignRankBlockListener blockListener = new SignRankBlockListener(this);
	public SignRankPermissionsBukkit permissionsBukkit = new SignRankPermissionsBukkit();
	public SignRankConfig signRankConfig = new SignRankConfig();
	public SignRankPermissionsBukkitYML signRankPermissionsBukkitYML = new SignRankPermissionsBukkitYML(this);

	public void onDisable() {
		
	}

	public void onEnable() {
		PluginDescriptionFile pdFile = this.getDescription();
		PluginManager pm = this.getServer().getPluginManager();
		Plugin permissionsBukkitPlugin = pm.getPlugin("PermissionsBukkit");
		if (permissionsBukkitPlugin == null) {
			log.severe("PermissionsBukkit could not be found, SignRank will disable itself.");
			this.setEnabled(false);
			return;
		}
		permissionsBukkit.plugin = (PermissionsPlugin)permissionsBukkitPlugin;
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		log.info(pdFile.getName() + " version " + pdFile.getVersion() + " loaded!");
	}
}
