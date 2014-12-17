package me.becja10.SpaceBugUtils;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public final Logger logger = Logger.getLogger("Minecraft");

	public void onEnable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
	}

  
	//prevent players from going above the nether
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		if (p.hasPermission("spacebugutils.nether")) return;
		if (p.getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER))
		{
			if (p.getLocation().getBlockY() > 127)
			{
				p.performCommand("spawn");
				p.performCommand("mail send 10becja AUTOMATED above the nether");
				p.sendMessage(ChatColor.RED + "Going above the Nether ceiling is NOT allowed");
			}
		}
	}
}