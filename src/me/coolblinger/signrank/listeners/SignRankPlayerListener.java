package me.coolblinger.signrank.listeners;

import me.coolblinger.signrank.SignRank;
import me.coolblinger.signrank.SignRankPermissionsBukkitYML;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.ArrayList;
import java.util.List;

public class SignRankPlayerListener extends PlayerListener {
	SignRank plugin;
	public SignRankPlayerListener (SignRank instance) {
		plugin = instance;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block eventBlock = event.getClickedBlock();
			BlockState eventBlockState = eventBlock.getState();
			Player player = event.getPlayer();
			if (eventBlockState instanceof Sign) {
				Sign eventSign = (Sign) eventBlockState;
				if (eventSign.getLine(0).equals(plugin.signRankConfig.getString("signText"))) {
					List<String> groups = plugin.signRankPermissionsBukkitYML.getGroups(player);
					if (groups.contains("default")) {
						plugin.signRankPermissionsBukkitYML.changeGroup(player, "default", plugin.signRankConfig.getString("toGroup"));
						player.sendMessage(ChatColor.GREEN + plugin.signRankConfig.getString("messages.rankUp"));
					} else {
						player.sendMessage(ChatColor.RED + plugin.signRankConfig.getString("messages.deny"));
					}
				}
			}
		}
	}
}
