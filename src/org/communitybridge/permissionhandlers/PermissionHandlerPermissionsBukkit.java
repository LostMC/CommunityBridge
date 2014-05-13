package org.communitybridge.permissionhandlers;

import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PermissionHandlerPermissionsBukkit extends PermissionHandler
{
	private static PermissionsPlugin permissions;

	public PermissionHandlerPermissionsBukkit() throws IllegalStateException
	{
		if (permissions == null)
		{
			Plugin plugin;
			plugin = Bukkit.getServer().getPluginManager().getPlugin("PermissionsBukkit");
			validate(plugin, "PermissionsBukkit");
			permissions = (PermissionsPlugin) plugin;
		}
	}

	@Override
	public boolean addToGroup(Player player, String groupName)
	{
		return Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						                                  "permissions player addgroup "
						                                  + player.getName() + " " + groupName);
	}

	@Override
	public List<String> getGroups(Player player)
	{
		List<String> groupNames = new ArrayList<String>();

		for (Group group : permissions.getAllGroups())
		{
			if (group.getPlayers().contains(player.getName().toLowerCase()))
			{
				groupNames.add(group.getName());
			}

		}

		return groupNames;
	}

	@Override
	public List<String> getGroupsPure(Player player)
	{
		return getGroups(player);
	}

	@Override
	public String getPrimaryGroup(Player player)
	{
		String playerName = player.getName();
		if (permissions.getPlayerInfo(playerName) == null
		 || permissions.getPlayerInfo(playerName).getGroups() == null
		 || permissions.getPlayerInfo(playerName).getGroups().isEmpty())
		{
			return "";
		}
		String group = permissions.getPlayerInfo(playerName).getGroups().get(0).getName();
		if (group == null)
		{
			return "";
		}
		else
		{
			return group;
		}
	}

 	@Override
	public boolean isMemberOfGroup(Player player, String groupName)
	{
		Group group = permissions.getGroup(groupName);

		if (group == null)
		{
			return false;
		}

		return group.getPlayers().contains(player.getName().toLowerCase());
	}

	@Override
	public boolean isPrimaryGroup(Player player, String groupName)
	{
		String primaryGroup = this.getPrimaryGroup(player);
		return primaryGroup != null && groupName.equalsIgnoreCase(primaryGroup);
	}

	@Override
	public boolean removeFromGroup(Player player, String groupName)
	{
		return Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						                                  "permissions player removegroup "
						                                + player.getName() + " " + groupName);
	}

	/**
	 * PermissionsBukkit doesn't have a primary group, so this calls AddToGroup.
	 */
	@Override
	public boolean setPrimaryGroup(Player player, String groupName, String formerGroupName)
	{
		boolean result;
		if (formerGroupName == null)
		{
			result = true;
		}
		else
		{
			result = removeFromGroup(player, formerGroupName);
		}
		return result && addToGroup(player, groupName);
	}

	@Override
	public boolean supportsPrimaryGroups()
	{
		return false;
	}
}
