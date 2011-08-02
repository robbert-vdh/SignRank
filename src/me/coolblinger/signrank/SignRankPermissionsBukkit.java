package me.coolblinger.signrank;

import com.platymuus.bukkit.permissions.PermissionsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivateKey;

public class SignRankPermissionsBukkit {
	protected PermissionsPlugin plugin;

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