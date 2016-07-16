package me.WoundedSlug.SlugplexHub;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.WoundedSlug.SlugplexHub.IconMenu.OptionClickEvent;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class main extends JavaPlugin implements Listener, PluginMessageListener{
	
	public IconMenu hubMenu;
	public IconMenu pointMenu;
	
	me.WoundedSlug.SlugAPI.main slugAPI;
	
	private OtherServer slugPvPServer;
	
	private OtherServer skywarsServer;
	

	private ArmorStand pvpCount;
	private ArmorStand skyCount;
	private ArmorStand skyStatus;
	private ArmorStand ppCount;
	
	static HashMap<String, ArmorStand> nameTags = new HashMap<String, ArmorStand>();
	
	FakePlayer fakePlayerAPI;
	
	main plugin;
	
	Gadgets gadgets;
	
	Cache cache;
	
	public void onEnable(){
		
		for(Entity e : Bukkit.getWorld("world").getEntities()){
			e.remove();
		}
		
		
		slugPvPServer = new OtherServer(this, "kitpvp-01");
		
		skywarsServer = new OtherServer(this, "skywars-01");
		
		fakePlayerAPI = new FakePlayer(this);
		
		
		plugin = this;
		gadgets = new Gadgets();
		gadgets.setMain(this);
		cache = new Cache(this, "ip", "port", "database", "username", "password", "skywars.batter", "skywars.spider", "pumpkinPossess.knockback", "pumpkinPossess.speed", "pumpkinPossess.burst", "kitpvp.jester", "kitpvp.scout", "kitpvp.tank", "kitpvp.archer");
		cache.addDatabase("pumpkinPossess", new String[]{ "knockback", "speed", "burst", "tags", "won"});
		cache.addDatabase("skywars", new String[]{"batter", "kills", "wins", "spider"});
		cache.addDatabase("kitpvp", new String[]{ "scout", "tank", "wizard", "kills", "jester", "archer"});
		Bukkit.getPluginManager().registerEvents(this, this);
		
		new BukkitRunnable(){
			public void run(){
				hubMenu.setOption(2, new ItemStack(Material.PUMPKIN, 1), ChatColor.GREEN + "PumpkinPossess",ChatColor.GRAY + "Avoid getting possessed in this tag style game", "", ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + Bukkit.getWorld("pumpkinPossess").getPlayers().size());
				hubMenu.setOption(4, new ItemStack(Material.GRASS),  ChatColor.GREEN + "SkyWars", ChatColor.GRAY + "Avoid falling to your death in this deathmatch style game", "", ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + skywarsServer.getPlayerCount(), ChatColor.GRAY + "Status: " + ChatColor.GREEN  + "" + skywarsServer.getMotd());
				hubMenu.setOption(6, removeAttributes(new ItemStack(Material.IRON_SWORD)),  ChatColor.GREEN + "SlugPvP", ChatColor.GRAY + "Fight to death again on a floating hell island", "", ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + slugPvPServer.getPlayerCount());
				pvpCount.setCustomName(ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + slugPvPServer.getPlayerCount());
				skyCount.setCustomName(ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + skywarsServer.getPlayerCount());
				skyStatus.setCustomName(ChatColor.GREEN + "" + skywarsServer.getMotd());
				ppCount.setCustomName(ChatColor.GRAY + "Players: " + ChatColor.GREEN + "" + Bukkit.getWorld("pumpkinPossess").getPlayers().size());
			}
		}.runTaskTimer(this, 20, 20);
		
		hubMenu = new IconMenu("SlugPlex Menu", 9, new IconMenu.OptionClickEventHandler() {
	            @Override
	            public void onOptionClick(IconMenu.OptionClickEvent event) {
		            if(event.getPosition() == 2)
		            	event.getPlayer().performCommand("pp");
		            else if(event.getPosition() == 4){
		            	ByteArrayDataOutput out = ByteStreams.newDataOutput();
		   				out.writeUTF("Connect");
		   				out.writeUTF("skywars-01");
		   				event.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
		            }else if(event.getPosition() == 6){
		            	ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("Connect");
						out.writeUTF("kitpvp-01");
						event.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
		            }
	                event.setWillClose(true);
	            }
	        }, this)
	        .setOption(2, new ItemStack(Material.PUMPKIN, 1), ChatColor.GREEN + "PumpkinPossess", ChatColor.GRAY + "Avoid getting possessed in this tag style game")
	        .setOption(4, new ItemStack(Material.GRASS),  ChatColor.GREEN + "SkyWars", ChatColor.GRAY + "Avoid falling to your death in this deathmatch style game")
	        .setOption(6, removeAttributes(new ItemStack(Material.IRON_SWORD)),  ChatColor.GREEN + "SlugPvP", ChatColor.GRAY + "Fight to death again on a floating hell island");
			
		Bukkit.getWorld("world").setTime(6000L);
		
		
		
		pointMenu = new IconMenu("Point Shop", 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                //event.getPlayer().performCommand("pp");
            	event.setWillClose(true);
            	if(event.getPosition() == 4){
            		Inventory inventory = Bukkit.createInventory(event.getPlayer(), 9, "SkyWars Shop");
            		
            		ItemStack bat = new ItemStack(Material.WOOD_SWORD);
            		bat = removeAttributes(bat);
            		if(cache.get("skywars.batter", event.getPlayer()) > 0){
            			bat = setItemNameAndLore(bat, ChatColor.GREEN + "Batter", new String[]{ChatColor.GRAY + "1 Wooden Sword w/ Knockback I", "", ChatColor.GOLD + "150 Points"});
            			bat = addGlow(bat);
            		}else{
            			bat = setItemNameAndLore(bat, ChatColor.RED + "Batter", new String[]{ChatColor.GRAY + "1 Wooden Sword w/ Knockback I", "", ChatColor.GOLD + "150 Points"});
            		}
            		inventory.setItem(2, bat);      
            		
            		ItemStack spider = new ItemStack(Material.SPIDER_EYE);
            		spider = removeAttributes(spider);
            		if(cache.get("skywars.spider", event.getPlayer()) > 0){
            			spider = setItemNameAndLore(spider, ChatColor.GREEN + "Spider", new String[]{ChatColor.GRAY + "3 Poison Splash Potions", ChatColor.GRAY + "8 Cobwebs", "", ChatColor.GOLD + "250 Points"});
            			spider = addGlow(spider);
            		}else{
            			spider = setItemNameAndLore(spider, ChatColor.RED + "Spider", new String[]{ChatColor.GRAY + "3 Poison Splash Potions", ChatColor.GRAY + "8 Cobwebs", "", ChatColor.GOLD + "250 Points"});
            		}
            		inventory.setItem(6, spider);
            		event.getPlayer().openInventory(inventory);
            		event.setWillClose(false);
            	}
            	if(event.getPosition() == 2){
            		Inventory inventory = Bukkit.createInventory(event.getPlayer(), 9, "Pumpkin Possess Shop");
            		
            		ItemStack knockback1 = new ItemStack(Material.BLAZE_ROD);
            		if(cache.get("pumpkinPossess.knockback", event.getPlayer()) > 0){
            			knockback1 = setItemNameAndLore(knockback1, ChatColor.GREEN + "Knockback I", new String[]{ChatColor.GRAY + "Knockback other players twice as far",ChatColor.GRAY + "when you're possessed", "", ChatColor.GOLD + "150 Points"});
            			knockback1 = addGlow(knockback1);
            		}else{
            			knockback1 = setItemNameAndLore(knockback1, ChatColor.DARK_PURPLE + "Knockback I", new String[]{ChatColor.GRAY + "Knockback other players twice as far",ChatColor.GRAY + "when you're possessed", "", ChatColor.GOLD + "150 Points"});
            		}
            		
            		ItemStack speed = new ItemStack(Material.SUGAR);
            		if(cache.get("pumpkinPossess.speed", event.getPlayer()) > 0){
            			speed = setItemNameAndLore(speed, ChatColor.GREEN + "Speed I", new String[]{ChatColor.GRAY + "Gain a helpful speed boost",ChatColor.GRAY + "when you're possessed", "", ChatColor.GOLD + "250 Points"});
            			speed = addGlow(speed);
            		}else{
            			speed = setItemNameAndLore(speed, ChatColor.DARK_PURPLE + "Speed I", new String[]{ChatColor.GRAY + "Gain a helpful speed boost",ChatColor.GRAY + "when you're possessed", "", ChatColor.GOLD + "250 Points"});
            		}
            		
            		inventory.setItem(2, knockback1);
            		inventory.setItem(6, speed);
            		event.getPlayer().openInventory(inventory);
            		event.setWillClose(false);
            	}
            	if(event.getPosition() == 6){
            		Inventory inventory = KitPvPHandler.getInventory(event.getPlayer(), cache);
            		
            		event.getPlayer().openInventory(inventory);
            		event.setWillClose(false);
            	}
            }
        }, this)
        .setOption(2, new ItemStack(Material.PUMPKIN, 1), ChatColor.GREEN + "PumpkinPossess", ChatColor.GRAY + "Buy upgrades for Pumpkin Possess here!")
        .setOption(4, new ItemStack(Material.GRASS),  ChatColor.GREEN + "SkyWars", ChatColor.GRAY + "Buy upgrades for SkyWars here!")
        .setOption(6, new ItemStack(Material.IRON_SWORD),  ChatColor.GREEN + "SlugPvP", ChatColor.GRAY + "Buy upgrades for SlugPvP here!");
		
		ArmorStand parkourStart = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 24.5f, 22.0f, 7.5f), EntityType.ARMOR_STAND);
		parkourStart.setCustomName(ChatColor.AQUA + "Parkour Start");
		parkourStart.setCustomNameVisible(true);
		parkourStart.setVisible(false);
		parkourStart.setSmall(true);
		parkourStart.setGravity(false);
		
		ArmorStand parkourEnd= (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 41.5f, 30.0f, 7.5f), EntityType.ARMOR_STAND);
		parkourEnd.setCustomName(ChatColor.AQUA + "Parkour End");
		parkourEnd.setCustomNameVisible(true);
		parkourEnd.setVisible(false);
		parkourEnd.setSmall(true);
		parkourEnd.setGravity(false);
		
		pvpCount = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 0.5, 22.1f, 19.5), EntityType.ARMOR_STAND);
		pvpCount.setCustomName(ChatColor.GRAY + "Players: ");
		pvpCount.setCustomNameVisible(true);
		pvpCount.setVisible(false);
		pvpCount.setSmall(true);
		pvpCount.setGravity(false);
		
		skyCount = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -18.5, 22.35, 0.5), EntityType.ARMOR_STAND);
		skyCount.setCustomName(ChatColor.GRAY + "Players: ");
		skyCount.setCustomNameVisible(true);
		skyCount.setVisible(false);
		skyCount.setSmall(true);
		skyCount.setGravity(false);
		
		skyStatus = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -18.5, 22.1, 0.5), EntityType.ARMOR_STAND);
		skyStatus.setCustomName(ChatColor.GREEN + "Resetting");
		skyStatus.setCustomNameVisible(true);
		skyStatus.setVisible(false);
		skyStatus.setSmall(true);
		skyStatus.setGravity(false);

		ppCount = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 0.5, 22.1, -18.5), EntityType.ARMOR_STAND);
		ppCount.setCustomName(ChatColor.GRAY + "Players: ");
		ppCount.setCustomNameVisible(true);
		ppCount.setVisible(false);
		ppCount.setSmall(true);
		ppCount.setGravity(false);
		fakePlayerAPI.addPlayer(ChatColor.GOLD + "" + ChatColor.BOLD + "SlugPvP", "Icarus", new Location(Bukkit.getWorld("world"), 0.5, 21, 19.5), "slugpvp");
		fakePlayerAPI.addPlayer(ChatColor.GOLD + "" + ChatColor.BOLD + "Skywars", "pixelglitch1", new Location(Bukkit.getWorld("world"), -18.5, 21, 0.5), "skywars");
		fakePlayerAPI.addPlayer(ChatColor.GOLD + "" + ChatColor.BOLD + "PumpkinPossess", 
				"eyJ0aW1lc3RhbXAiOjE0NDM3MzExNDE4NzQsInByb2ZpbGVJZCI6Ijc4NmI4NjljZjYxNDQxNTliYjY4YTlmNjI5ZmJjZTVhIiwicHJvZmlsZU5hbWUiOiJGb3d4eHkiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYxZDQ3OTU5NTkyNTIxN2RkZTExNTkzMTEyNzgyNGExNmUyMjllNWM5MjA3MmRiOTJiN2U4OTcxODQ3ZDQ1YiJ9fX0=",
				"sMNhOBlrApS/oAZdQezLSyqVBzXKdiUDLmDMX7Un4V/pHyFvtl0oxlWPVex5mUOcFgTuvs2QlbNUMO160rDNAyErpE0CwCpHQUEBsRC+3clcBGzzx8Q9Ms6xYr7s6s7Jttxul+cvL/IcUvgGhr6zeLpGcF1+Yxd6svX+C6hh4NaCWn5UkFapsb5kU6Elkouw0wvDhXnbwQkZUu633HjduINDHiu3Ac8XfmBUctqxaam70N8bxOoGoYy3r3q0IwCJiCQ5nPS93jvZ4UetJhcYjYyK4PNOJsP+sR8tjcyBEmcfTw2EBjBRlUSyY1HmTZW+10/mRfI7Dal5EsdEDJ74ZbTn5FRT3z6FK5BXwmSllYYV40wKqzMCFfEhyy/kXbd+pMbt7LlaJRYCLiw+lznE9dy6uY1TS8gAcOUqvXemLO2vZVK4HYduIchGhNCqClTieooa0b/K8OaAPkevV60owWL3Us3uw+E7m4xrNzM0N6aE+5jQW0mtp3P+eiSkHU9J65O/guHzYrqSwyCETygMbhcBH9JI/mInqG53Q/Up4IUuOy2WN3LkCXptZ1611Osx5cVdbXFJ3CBwPsUX4zHuiMGCHQzP+1cR0NTaX6mluCnHnDkFm7qMFWsegpmdmf1tt1QwLInfiK4vfAIuKVqRdN7+vOaSG1QItpEWk4b27rA=",
				new Location(Bukkit.getWorld("world"), 0.5, 21, -18.5), "pp");
        
       
       new BukkitRunnable(){
    	   public void run(){
    		   for(Player p : Bukkit.getOnlinePlayers()){
    			   if(p.getOpenInventory().getTitle().equalsIgnoreCase("SlugPlex Menu")){
    				   p.getOpenInventory().setItem(4, hubMenu.getOption(4));
    				   p.getOpenInventory().setItem(6, hubMenu.getOption(6));
    			   }
    		   }
    	   }
       }.runTaskTimer(this, 5, 5);
       
		
	}
	
	private Object getSessionService()
    {
        Server server = Bukkit.getServer();
        try
        {
            Object mcServer = server.getClass().getDeclaredMethod("getServer").invoke(server);
            for (Method m : mcServer.getClass().getMethods())
            {
                if (m.getReturnType().getSimpleName().equalsIgnoreCase("MinecraftSessionService"))
                {
                    return m.invoke(mcServer);
                }
            }
        }
        catch (Exception ex)
        {
            throw new IllegalStateException("An error occurred while trying to get the session service", ex);
        }
        throw new IllegalStateException("No session service found :o");
    }

    private Method getFillMethod(Object sessionService)
    {
        for(Method m : sessionService.getClass().getDeclaredMethods())
        {
            if(m.getName().equals("fillProfileProperties"))
            {
                return m;
            }
        }
        throw new IllegalStateException("No fillProfileProperties method found in the session service :o");
    }
	
	private static ItemStack removeAttributes(ItemStack item)
    {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag;
        if (!nmsStack.hasTag())
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        else
        {
            tag = nmsStack.getTag();
        }
        NBTTagList am = new NBTTagList();
        tag.set("AttributeModifiers", am);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
	
	public static ItemStack addGlow(ItemStack item){
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
	
	 private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
	        ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
	        item.setItemMeta(im);
	        return item;
	    }
	
	public void onDisable(){
		
	}
	
	 @SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
		 
		 //PPShop
        if (event.getInventory().getTitle().equals("Pumpkin Possess Shop") || event.getInventory().getTitle().equals("SkyWars Shop") || event.getInventory().getTitle().equals("SlugPvP Shop")) {
            event.setCancelled(true);
            final Player p = (Player)event.getWhoClicked();
            if(event.getInventory().getTitle().equals("Pumpkin Possess Shop")){
	            if(event.getSlot() == 2){
                	if(cache.get("pumpkinPossess.knockback", p) == 0){
                        if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 150){
                        	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Knockback I\"");
                        	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -150);
                        	cache.put("pumpkinPossess.knockback", p, 1);
                        }else{
                        	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                        }
                	}else{
                		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
	                }
	                  
	            }else if(event.getSlot() == 6){
                	if(cache.get("pumpkinPossess.speed", p) == 0){
                        if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
                        	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Speed I\"");
                        	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
                        	cache.put("pumpkinPossess.speed", p, 1);
                        }else{
                        	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                        }
                	}else{
                		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
                	}   
	            }
            }else if(event.getInventory().getTitle().equals("SkyWars Shop")){
            	if(event.getSlot() == 2){
                	if(cache.get("skywars.batter", p) == 0){
                        if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 150){
                        	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Batter I\"");
                        	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -150);
                        	cache.put("skywars.batter", p, 1);
                        }else{
                        	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                        }
                	}else{
                		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
                	}
            	}else if(event.getSlot() == 6){
            		if(cache.get("skywars.spider", p) == 0){
                        if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
                        	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Spider I\"");
                        	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
                        	cache.put("skywars.spider", p, 1);
                        }else{
                        	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                        }
                	}else{
                		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
                	}
            	}
            }else if(event.getInventory().getTitle().equals("SlugPvP Shop")){
            	KitPvPHandler.handleClick(p, event.getSlot(), cache, plugin);
            }
           
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            	public void run(){
            		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
	    			Objective o = s.registerNewObjective("sideThing", "dummy");
	    			
	    			o.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"SlugPlex");
	    			Score rank = o.getScore(ChatColor.YELLOW+"Rank:");
	    			rank.setScore(12);
	    			Score rankInfo = o.getScore(me.WoundedSlug.SlugAPI.main.getPrefix(p));
	    			rankInfo.setScore(11);
	    			Score blank1 = o.getScore(ChatColor.AQUA + "");
	    			blank1.setScore(10);
	    			Score points = o.getScore(ChatColor.YELLOW + "Points:");
	    			points.setScore(9);
	    			Score pointsInfo = o.getScore(ChatColor.RED + "" + me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()));
	    			pointsInfo.setScore(8);
	    			
	    			
	    			
	    			o.setDisplaySlot(DisplaySlot.SIDEBAR);
	    			
	    			p.setScoreboard(s);
            	}
            }, 20);
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    p.closeInventory();
                }
            }, 1);
        }
        
        
        //GadgetsMenu
        if(event.getInventory().getName().equalsIgnoreCase("Gadgets Menu")){
	        if(event.getWhoClicked() instanceof Player){
	        	gadgets.gadgetsMenuHandler((Player) event.getWhoClicked(), event.getSlot());
	        	event.getWhoClicked().closeInventory();
	        }
        }
        
        if(event.getWhoClicked().getLocation().getWorld().getName().equalsIgnoreCase("world"))
        	event.setCancelled(true);
    }
	
	@EventHandler
	public void onItemHold(PlayerItemHeldEvent e){
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("world")){
			if(e.getNewSlot() == 3){
				if(DisguiseAPI.isDisguised(e.getPlayer())){
					DisguiseAPI.getDisguise(e.getPlayer()).setViewSelfDisguise(true);
				}
			}else{
				if(DisguiseAPI.isDisguised(e.getPlayer())){
					DisguiseAPI.getDisguise(e.getPlayer()).setViewSelfDisguise(false);
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onWorldChange(PlayerChangedWorldEvent event){
		if(event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world")){
			Player p = event.getPlayer();
			p.getInventory().clear();
			ItemStack compass = new ItemStack(Material.COMPASS, 1);
			ItemMeta compassMeta = compass.getItemMeta();
			compassMeta.setDisplayName(ChatColor.YELLOW  + "" + ChatColor.BOLD + "Game Browser " + ChatColor.GRAY + "(Right Click)");
			compass.setItemMeta(compassMeta);
			p.getInventory().setItem(0, compass);
			
			ItemStack point = new ItemStack(Material.EMERALD, 1);
			ItemMeta pointMeta = point.getItemMeta();
			pointMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Point Shop " + ChatColor.GRAY + "(Right Click)");
			point.setItemMeta(pointMeta);
			p.getInventory().setItem(2, point);
			
			ItemStack gadgets = new ItemStack(Material.WATCH, 1);
			ItemMeta gadgetsMeta = gadgets.getItemMeta();
			gadgetsMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gadgets " + ChatColor.GRAY + "(Right Click)");
			gadgets.setItemMeta(gadgetsMeta);
			p.getInventory().setItem(1, gadgets);
			
			for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
		        event.getPlayer().removePotionEffect(effect.getType());
			p.setExp(0.0f);
			if(p.hasPermission("owner.owner") || p.hasPermission("admin.admin") || p.hasPermission("builder.builder") || p.hasPermission("slug.slug"))
				p.setAllowFlight(true);
			else
				p.setAllowFlight(false);
			Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective o = s.registerNewObjective("sideThing", "dummy");
			
			o.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"SlugPlex");
			Score rank = o.getScore(ChatColor.YELLOW+"Rank:");
			rank.setScore(12);
			Score rankInfo = o.getScore(me.WoundedSlug.SlugAPI.main.getPrefix(event.getPlayer()));
			rankInfo.setScore(11);
			Score blank1 = o.getScore(ChatColor.AQUA + "");
			blank1.setScore(10);
			Score points = o.getScore(ChatColor.YELLOW + "Points:");
			points.setScore(9);
			Score pointsInfo = o.getScore(ChatColor.RED + "" + me.WoundedSlug.SlugAPI.main.getPoints(event.getPlayer().getUniqueId().toString()));
			pointsInfo.setScore(8);
			
			
			
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			p.setScoreboard(s);
		}else{
			if(nameTags.containsKey(event.getPlayer().getUniqueId().toString())){
				nameTags.get(event.getPlayer().getUniqueId().toString()).remove();
				nameTags.remove(event.getPlayer().getUniqueId().toString());
			}
			DisguiseAPI.undisguiseToAll(event.getPlayer());
		}
	}
	
	@EventHandler
    public void onItemDrop (PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld().getName().equalsIgnoreCase("world"))
        	e.setCancelled(true);
    }
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		event.setQuitMessage(null);
		if(nameTags.containsKey(event.getPlayer().getUniqueId().toString())){
			nameTags.get(event.getPlayer().getUniqueId().toString()).remove();
			nameTags.remove(event.getPlayer().getUniqueId().toString());
		}
		if(Gadgets.firework.contains(event.getPlayer().getUniqueId().toString()))
			Gadgets.firework.remove(event.getPlayer().getUniqueId().toString());
	}
	


	@EventHandler (priority = EventPriority.HIGHEST)
	public void onEntitySpawn(EntitySpawnEvent e){
		if(e.getEntityType() == EntityType.ARMOR_STAND)
			e.setCancelled(false);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent e){
		if(e.getEntityType() == EntityType.ARMOR_STAND)
			e.setCancelled(false);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerDamage2(EntityDamageByEntityEvent event){
		if(event.getEntityType() == EntityType.PLAYER){
			//Players
			Player hit = (Player) event.getEntity();
			if(event.getDamager() instanceof Player){
				Player hitter = (Player) event.getDamager();
				if(hit.getLocation().getWorld().getName().equalsIgnoreCase("world")){
					if(hit.getLocation().getX() < 22.0f && hit.getLocation().getX() > 17.0f){
						if(hit.getLocation().getZ() < -16.0f && hit.getLocation().getZ() > -21.0f){
							if(hitter.getLocation().getWorld().getName().equalsIgnoreCase("world")){
								if(hitter.getLocation().getX() < 22.0f && hitter.getLocation().getX() > 17.0f){
									if(hitter.getLocation().getZ() < -16.0f && hitter.getLocation().getZ() > -21.0f){
										event.setCancelled(false);
										event.setDamage(0.0D);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		p.getInventory().clear();

		
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta compassMeta = compass.getItemMeta();
		compassMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Game Browser " + ChatColor.GRAY + "(Right Click)");
		compass.setItemMeta(compassMeta);
		p.getInventory().setItem(0, compass);
		
		ItemStack point = new ItemStack(Material.EMERALD, 1);
		ItemMeta pointMeta = point.getItemMeta();
		pointMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Point Shop " + ChatColor.GRAY + "(Right Click)");
		point.setItemMeta(pointMeta);
		p.getInventory().setItem(2, point);
		
		ItemStack gadgets = new ItemStack(Material.WATCH, 1);
		ItemMeta gadgetsMeta = gadgets.getItemMeta();
		gadgetsMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gadgets " + ChatColor.GRAY + "(Right Click)");
		gadgets.setItemMeta(gadgetsMeta);
		p.getInventory().setItem(1, gadgets);
		
		sendFullTitle(p, 3, 40, 3, ChatColor.GOLD + "Welcome to SlugPlex",ChatColor.GOLD + "Enjoy your stay!");
		sendTabTitle(p, ChatColor.AQUA + "slugplex.net", ChatColor.GOLD + "Enjoy your stay!");
		p.setExp(0.0f);
		if(p.hasPermission("owner.owner") || p.hasPermission("admin.admin") || p.hasPermission("builder.builder") || p.hasPermission("slug.slug"))
			p.setAllowFlight(true);
		else
			p.setAllowFlight(false);
		event.setJoinMessage(null);
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
	        event.getPlayer().removePotionEffect(effect.getType());
		final String name = event.getPlayer().getName();
		
		
		/*if(p.hasPermission("owner.owner")){
			MobDisguise wolf = new MobDisguise(DisguiseType.WOLF);
			wolf.setViewSelfDisguise(true);
			DisguiseAPI.disguiseToAll(event.getPlayer(), wolf);
		}*/
		
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
			event.getPlayer().removePotionEffect(effect.getType());
		
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable(){
			public void run(){
				if(event.getPlayer() != null){
					if(event.getPlayer().getWorld().getName().equalsIgnoreCase("world")){
						Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
						Objective o = s.registerNewObjective("sideThing", "dummy");
						
						o.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"SlugPlex");
						Score rank = o.getScore(ChatColor.YELLOW+"Rank:");
						rank.setScore(12);
						
						Score rankInfo = o.getScore(me.WoundedSlug.SlugAPI.main.getPrefix(event.getPlayer()));
						
						rankInfo.setScore(11);
						Score blank1 = o.getScore(ChatColor.AQUA + "");
						blank1.setScore(10);
						Score points = o.getScore(ChatColor.YELLOW + "Points:");
						points.setScore(9);
						Score pointsInfo = o.getScore(ChatColor.RED + "" + me.WoundedSlug.SlugAPI.main.getPoints(event.getPlayer().getUniqueId().toString()));
						pointsInfo.setScore(8);
						
						
						
						o.setDisplaySlot(DisplaySlot.SIDEBAR);
						
						p.setScoreboard(s);
					}
				}
				
			}
		}, 20);
		

		
	}

	@Deprecated
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, message, null);
    }

    @Deprecated
    public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, null, message);
    }

    @Deprecated
    public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        try {
        	
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, stay, fadeOut);
                sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                Object subtitlePacket = subtitleConstructor.newInstance(enumSubtitle, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e){
    	if(e.getTo().getWorld().getName().equalsIgnoreCase("world")){
	    	if(e.getTo().getX() < -56.0f || e.getTo().getX() > 56.0f){
	    		e.setCancelled(true);
	    		e.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
	    	}
	    	if(e.getTo().getZ() < -56.0f || e.getTo().getZ() > 56.0f){
	    		e.setCancelled(true);
	    		e.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
	    	}
	    	if(e.getTo().getY() < 20.0f || e.getTo().getY() > 45.0f){
	    		e.setCancelled(true);
	    		e.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
	    	}
	    	
    	}
    	
    	Player hit = (Player) e.getPlayer();
		if(hit.getLocation().getWorld().getName().equalsIgnoreCase("world")){
			if(hit.getLocation().getX() < 22.0f && hit.getLocation().getX() > 17.0f){
				if(hit.getLocation().getZ() < -16.0f && hit.getLocation().getZ() > -21.0f){
					hit.setFlying(false);
					hit.setAllowFlight(false);
					if(nameTags.containsKey(e.getPlayer().getUniqueId().toString())){
						nameTags.get(e.getPlayer().getUniqueId().toString()).remove();
						nameTags.remove(e.getPlayer().getUniqueId().toString());
					}
					DisguiseAPI.undisguiseToAll(e.getPlayer());
					e.getPlayer().getInventory().setItem(3, new ItemStack(Material.AIR));
				}else{
					if(hit.hasPermission("owner.owner") || hit.hasPermission("admin.admin") || hit.hasPermission("builder.builder") | hit.hasPermission("slug.slug"))
						hit.setAllowFlight(true);
					else
						hit.setAllowFlight(false);
				}
			}else{
				if(hit.hasPermission("owner.owner") || hit.hasPermission("admin.admin") || hit.hasPermission("builder.builder") || hit.hasPermission("slug.slug"))
					hit.setAllowFlight(true);
				else
					hit.setAllowFlight(false);
			}
		}
		if(!e.isCancelled()){
			if(nameTags.containsKey(e.getPlayer().getUniqueId().toString())){
				ArmorStand nameTag = nameTags.get(e.getPlayer().getUniqueId().toString());
				nameTag.teleport(e.getTo());
			}
		}
		
		if(hit.getLocation().getWorld().getName().equalsIgnoreCase("world")){
			if(hit.getLocation().getWorld().getBlockAt(hit.getLocation().getBlockX(), hit.getLocation().getBlockY() - 1, hit.getLocation().getBlockZ()).getType() == Material.WOOL){
				hit.setVelocity(new Vector(hit.getVelocity().getX(), 1.3f, hit.getVelocity().getZ()));
			}
		}
		
    }
    
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
    	if(nameTags.containsKey(e.getPlayer().getUniqueId().toString())){
			ArmorStand nameTag = nameTags.get(e.getPlayer().getUniqueId().toString());
			nameTag.teleport(e.getTo());
		}
    }

    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        header = ChatColor.translateAlternateColorCodes('&', header);

        if (footer == null) footer = "";
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());

        try {
            Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
            Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(getNMSClass("IChatBaseComponent"));
            Object packet = titleConstructor.newInstance(tabHeader);
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		if(event.getPlayer().getWorld().getName().equalsIgnoreCase("world")){
			if(event.getItem() != null){
				if(event.getItem().getType() == Material.COMPASS){
					hubMenu.open(event.getPlayer());
				}else if(event.getItem().getType() == Material.EMERALD){
					pointMenu.open(event.getPlayer());
				}else if(event.getItem().getType() == Material.WATCH){
					//Open Gadgets Menu
					gadgets.openMenu(event.getPlayer());
				}else if(event.getItem().getType() == Material.MONSTER_EGG){
					DisguiseAPI.undisguiseToAll(event.getPlayer());
					if(nameTags.containsKey(event.getPlayer().getUniqueId().toString())){
						nameTags.get(event.getPlayer().getUniqueId().toString()).remove();
						nameTags.remove(event.getPlayer().getUniqueId().toString());
					}
					event.getPlayer().getInventory().setItem(3, new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
		
		if(commandLabel.equalsIgnoreCase("skywars")){
			if(sender instanceof Player){
				if(sender instanceof Player){
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF("skywars-01");
					((Player) sender).sendPluginMessage(this, "BungeeCord", out.toByteArray());
					
				}
				
			}
		}else if(sender instanceof Player){
			if(sender instanceof Player){
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("Connect");
				out.writeUTF("kitpvp-01");
				((Player) sender).sendPluginMessage(this, "BungeeCord", out.toByteArray());
				
			}
			
		}
		
		
		
		return true;
		 
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}

}
