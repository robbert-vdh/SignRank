package me.coolblinger.signrank;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import me.coolblinger.signrank.listeners.SignRankBlockListener;
import me.coolblinger.signrank.listeners.SignRankPlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SignRank extends JavaPlugin{
	public Logger log = Logger.getLogger("Minecraft");
	public boolean permissions3;
	public PermissionHandler permissions;
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
			Plugin permissions3Plugin = pm.getPlugin("Permissions");
			if (permissions3Plugin == null) {
				log.severe("PermissionsBukkit nor Permissions3 could be found. SignRank will disable itself.");
				this.setEnabled(false);
				return;
			} else {
				if (!permissions3Plugin.getDescription().getVersion().contains("3.")) {
					log.severe("PermissionsBukkit nor Permissions3 could be found. SignRank will disable itself.");
					log.warning(permissions3Plugin.getDescription().getVersion());
					this.setEnabled(false);
					return;
				} else {
					permissions3 = true;
					permissions = ((Permissions)permissions3Plugin).getHandler();
				}
			}
		} else {
			permissions3 = false;
			permissionsBukkit.plugin = (PermissionsPlugin)permissionsBukkitPlugin;
		}
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		log.info(pdFile.getName() + " version " + pdFile.getVersion() + " loaded!");
	}
}
