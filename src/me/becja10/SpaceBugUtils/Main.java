package me.becja10.SpaceBugUtils;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		//silentTP
		if(cmd.getName().equalsIgnoreCase("silenttp"))
		{
			if(!(sender instanceof Player))
				sender.sendMessage("This command can only be run by a player.");
			else
			{
				Player p = (Player) sender;
				switch (args.length)
				{
				case 1: //proper amount of players sent
					//make sure the target is online/real
					Player tar = Bukkit.getPlayer(args[0]);
					if(tar == null)
						p.sendMessage(ChatColor.RED+"Player not found.");
					else
					{
						tar.teleport(p.getLocation());
						p.sendMessage("Very sneaky");
					}
					break;
				default: //they screwed up.
					return false;
				}
			}
		}
		return true;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClick(PlayerInteractEvent event)
	{
		Player p = event.getPlayer();
		if(p.hasPermission("spacebugutils.changespawner")) return;
		
		Material inHand = p.getItemInHand().getType();
		//prevent players changing mob spanwers
		if(inHand == Material.MONSTER_EGG || inHand == Material.MONSTER_EGGS)
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if(event.getClickedBlock().getType() == Material.MOB_SPAWNER)
					event.setCancelled(true);
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
				p.performCommand("mail send 10becja AUTO above the nether " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockZ());
				p.sendMessage(ChatColor.RED + "Going above the Nether ceiling is NOT allowed");
				this.logger.info("[SpaceBugUtils] "+ p.getName() + " was prevented from going above nether.");
			}
		}
	}
	
	//prevent players using too many caps, or spamming chat
	@EventHandler(priority=EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event)
	{
		//don't lower mods/admins
		if(event.getPlayer().hasPermission("spacebugutils.chat")) return;
		String msg = event.getMessage();
		//loop over msg and count caps
		int caps = 0;
		int low = 0;
		for(Character c : msg.toCharArray())
		{
			if(Character.isUpperCase(c)) 
				caps++;
			else
				low++;
		}
		//if more than half the characters are caps, make them lower case
		if ((double) caps/low > 1 && msg.length() > 5)
			event.setMessage(msg.toLowerCase());
	}
}