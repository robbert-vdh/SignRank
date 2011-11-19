package me.coolblinger.signrank.listeners;

import com.nijiko.permissions.User;
import me.coolblinger.signrank.SignRank;
import org.anjocaido.groupmanager.data.Group;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;

import java.io.File;
import java.util.List;

public class SignRankPlayerListener extends PlayerListener {
	private final SignRank plugin;

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
					} else if (plugin.pluginName.equals("Permissions3")) {
						User user = plugin.permissions.getUserObject(player.getWorld().getName(), player.getName());
						if (plugin.readBoolean("bypassGroupCheck")) {
							com.nijiko.permissions.Group group = plugin.permissions.getGroupObject(user.getWorld(), eventSign.getLine(1));
							if (group != null) {
								user.removeParent(user.getPrimaryGroup());
								user.addParent(group);
								String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
								player.sendMessage(ChatColor.GREEN + rankUp);
							} else {
								player.sendMessage(ChatColor.RED + "The group '" + eventSign.getLine(1) + "' does not exist.");
							}
						} else {
							if (plugin.readString("MultiWorld." + player.getWorld().getName()) == null) {
								player.sendMessage(ChatColor.RED + "SignRank has not been set up for this world.");
							} else {
								if (user.inGroup(user.getWorld(), plugin.permissions.getDefaultGroup(user.getWorld()).getName())) {
									user.removeParent(plugin.permissions.getDefaultGroup(user.getWorld()));
									user.addParent(plugin.permissions.getGroupObject(user.getWorld(), plugin.readString("MultiWorld." + user.getWorld())));
									String rankUp = plugin.readString("messages.rankUp").replace("%group%", plugin.readString("MultiWorld." + user.getWorld()));
									player.sendMessage(ChatColor.GREEN + rankUp);
								} else {
									String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("MultiWorld." + user.getWorld()));
									player.sendMessage(ChatColor.RED + deny);
								}
							}
						}
					} else if (plugin.pluginName.equals("GroupManager")) { // GroupManager has the worst API ever.
						org.anjocaido.groupmanager.data.User user = plugin.gm.getWorldsHolder().getWorldData(player).getUser(player.getName());
						if (plugin.readBoolean("bypassGroupCheck")) {
							Group group = plugin.gm.getWorldsHolder().getWorldData(player).getGroup(eventSign.getLine(1));
							if (group != null) {
								user.setGroup(group);
								String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
								player.sendMessage(ChatColor.GREEN + rankUp);
							} else {
								player.sendMessage(ChatColor.RED + "The group '" + eventSign.getLine(1) + "' does not exist.");
							}
						} else { // I know I should have split this statement, but I was just wondering whether they could make it more complicated.
							if (plugin.readString("MultiWorld." + player.getWorld().getName()) == null) {
								player.sendMessage(ChatColor.RED + "SignRank has not been set up for this world.");
							} else {
								if (user.getGroup() == plugin.gm.getWorldsHolder().getWorldData(player).getDefaultGroup()) {
									Group group = plugin.gm.getWorldsHolder().getWorldData(player).getGroup(plugin.readString("MultiWorld." + player.getWorld().getName()));
									user.setGroup(group);
									String rankUp = plugin.readString("messages.rankUp").replace("%group%", group.getName());
									player.sendMessage(ChatColor.GREEN + rankUp);
								} else {
									String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("MultiWorld." + player.getWorld().getName()));
									player.sendMessage(ChatColor.RED + deny);
								}
							}
						}
					} else if (plugin.pluginName.equals("PermissionsEx")) {
						PermissionUser user = plugin.pex.getUser(player.getName());
						if (plugin.readBoolean("bypassGroupCheck")) {
							PermissionGroup group = plugin.pex.getGroup(eventSign.getLine(1));
							if (group != null) {
								user.setGroups(new PermissionGroup[]{group});
								String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
								player.sendMessage(ChatColor.GREEN + rankUp);
							} else {
								player.sendMessage(ChatColor.RED + "The group '" + eventSign.getLine(1) + "' does not exist.");
							}
						} else {
							if (plugin.readString("MultiWorld." + player.getWorld().getName()) == null) {
								player.sendMessage(ChatColor.RED + "SignRank has not been set up for this world.");
							} else {
								if (user.inGroup(plugin.pex.getDefaultGroup(player.getWorld().getName()))) {
									PermissionGroup group = plugin.pex.getGroup(plugin.readString("MultiWorld." + player.getWorld().getName()));
									user.setGroups(new PermissionGroup[]{group});
									String rankUp = plugin.readString("messages.rankUp").replace("%group%", group.getName());
									player.sendMessage(ChatColor.GREEN + rankUp);
								} else {
									String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("MultiWorld." + player.getWorld().getName()));
									player.sendMessage(ChatColor.RED + deny);
								}
							}
						}
					} else if (plugin.pluginName.equals("bPermissions")) {
						//There is no easy way to retrieve the default group.
						YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/bPermissions/worlds/" + player.getWorld().getName() + ".yml"));
						String defaultGroup = c.getString("default");
						if (plugin.readBoolean("bypassGroupCheck")) {
							for (String group:plugin.bp.getPermissionSet(player.getWorld()).getGroups(player)) {
								plugin.bp.getPermissionSet(player.getWorld()).removeGroup(player, group);
							}
							plugin.bp.getPermissionSet(player.getWorld()).addGroup(player, eventSign.getLine(1));
							String rankUp = plugin.readString("messages.rankUp").replace("%group%", eventSign.getLine(1));
							player.sendMessage(ChatColor.GREEN + rankUp);
						} else {
							if (plugin.readString("MultiWorld." + player.getWorld().getName()) == null) {
								player.sendMessage(ChatColor.RED + "SignRank has not been set up for this world.");
							} else {
								if (plugin.bp.getPermissionSet(player.getWorld()).getGroups(player).contains(defaultGroup)) {
									String group = plugin.readString("MultiWorld." + player.getWorld().getName());
									plugin.bp.getPermissionSet(player.getWorld()).addGroup(player, group);
									plugin.bp.getPermissionSet(player.getWorld()).removeGroup(player, defaultGroup);
									String rankUp = plugin.readString("messages.rankUp").replace("%group%", group);
									player.sendMessage(ChatColor.GREEN + rankUp);
								} else {
									String deny = plugin.readString("messages.deny").replace("%group%", plugin.readString("MultiWorld." + player.getWorld().getName()));
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