package me.coolblinger.signrank.listeners;

import com.nijiko.permissions.User;
import me.coolblinger.signrank.SignRank;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.List;

public class SignRankPlayerListener extends PlayerListener {
	SignRank plugin;

	public SignRankPlayerListener(SignRank instance) {
		plugin = instance;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block eventBlock = event.getClickedBlock();
			BlockState eventBlockState = eventBlock.getState();
			Player player = event.getPlayer();
			if (eventBlockState instanceof Sign) {
				Sign eventSign = (Sign) eventBlockState;
				if (eventSign.getLine(0).equals(plugin.readString("signText"))) {
					if (plugin.pluginName.equals("PermissionsBukkit")) {
						List<String> groups = plugin.signRankPermissionsBukkitYML.getGroups(player);
						if (plugin.readBoolean("bypassGroupCheck")) {
							plugin.signRankPermissionsBukkitYML.changeGroup(player, eventSign.getLine(1));
							String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
							player.sendMessage(ChatColor.GREEN + rankUp);
						} else {
							if (groups.contains("default")) {
								plugin.signRankPermissionsBukkitYML.changeGroup(player, plugin.readString("PermissionsBukkit.toGroup"));
								String rankUp = plugin.readString("messages.rankUp").replace("%group%", plugin.readString("PermissionsBukkit.toGroup"));
								player.sendMessage(ChatColor.GREEN + rankUp);
							} else {
								String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("PermissionsBukkit.toGroup"));
								player.sendMessage(ChatColor.RED + deny);
							}
						}
					} else {
						User user = plugin.permissions.getUserObject(player.getWorld().getName(), player.getName());
						if (plugin.readBoolean("bypassGroupCheck")) {
							if (plugin.permissions.getGroupObject(user.getWorld(), eventSign.getLine(1)) != null) {
								user.removeParent(user.getPrimaryGroup());
								user.addParent(plugin.permissions.getGroupObject(user.getWorld(), eventSign.getLine(1)));
								String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
								player.sendMessage(ChatColor.GREEN + rankUp);
							} else {
								player.sendMessage(ChatColor.RED + "The group '" + eventSign.getLine(1) + "' does not exist.");
							}
						} else if (plugin.pluginName.equals("Permissions3")) {
							if (plugin.readString("Permissions3." + user.getWorld()) == null) {
								player.sendMessage(ChatColor.RED + "SignRank has not been set up for this world.");
							} else {
								if (user.inGroup(user.getWorld(), plugin.permissions.getDefaultGroup(user.getWorld()).getName())) {
									user.removeParent(plugin.permissions.getDefaultGroup(user.getWorld()));
									user.addParent(plugin.permissions.getGroupObject(user.getWorld(), plugin.readString("Permissions3." + user.getWorld())));
									String rankUp = plugin.readString("messages.rankUp").replace("%group%", plugin.readString("Permissions3." + user.getWorld()));
									player.sendMessage(ChatColor.GREEN + rankUp);
								} else {
									String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("Permissions3." + user.getWorld()));
									player.sendMessage(ChatColor.RED + deny);
								}
							}
						}
					}
				}
			}
		}
	}
}
