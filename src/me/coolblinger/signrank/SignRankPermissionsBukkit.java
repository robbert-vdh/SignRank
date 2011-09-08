package me.coolblinger.signrank;

import com.platymuus.bukkit.permissions.PermissionsPlugin;

import java.lang.reflect.Method;

public class SignRankPermissionsBukkit {
	PermissionsPlugin plugin;

	public void reloadPermissions() {
		plugin.getConfiguration().load();
		Class pluginClass = plugin.getClass();
		Method[] methods = pluginClass.getDeclaredMethods();
		for (Method method:methods) {
			if (method.getName().equals("refreshPermissions")) {
				method.setAccessible(true);
				try {
					method.invoke(plugin);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}