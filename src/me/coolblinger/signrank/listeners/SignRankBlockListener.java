package me.coolblinger.signrank.listeners;

import me.coolblinger.signrank.SignRank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignRankBlockListener extends BlockListener {
	private final SignRank plugin;

	public SignRankBlockListener (SignRank instance) {
		plugin = instance;
	}

	public void onSignChange (SignChangeEvent event) {
		if (event.getLine(0).equals(plugin.readString("signText"))) {
			Player player = event.getPlayer();
			if (!plugin.hasPermission(player)) {
				player.sendMessage(ChatColor.RED + "You're not allowed to do this.");
				event.getBlock().setType(Material.AIR);
				event.setCancelled(true);
			}
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		Block eventBlock = event.getBlock();
		BlockState eventBlockState = eventBlock.getState();
		if (eventBlockState instanceof Sign) {
			Sign eventSign = (Sign)eventBlockState;
			if (eventSign.getLine(0).equals(plugin.readString("signText"))) {
				Player player = event.getPlayer();
				if (!plugin.hasPermission(player)) {
					player.sendMessage(ChatColor.RED + "You're not allowed to do this.");
					event.setCancelled(true);
				}
			}
		}
	}
}
