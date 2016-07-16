package me.WoundedSlug.SlugplexHub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class Gadgets implements Listener {

	static ArrayList<Location> trampolines = new ArrayList<Location>();
	public static List<String> firework = new ArrayList<String>();
	static main Plugin;
	
	public  void setMain(main pl){
		Plugin = pl;
		
		new BukkitRunnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(firework.contains(p.getUniqueId().toString())){
						p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					}
				}
			}
		}.runTaskTimer(Plugin, 20, 20);
		
		Bukkit.getServer().getPluginManager().registerEvents(this,  pl);
		
	}
	
	public void openMenu(Player p){
		Inventory inventory = Bukkit.createInventory(p, 9, "Gadgets Menu");
		
		@SuppressWarnings("deprecation")
		ItemStack wolfDisguise = new ItemStack(Material.MONSTER_EGG, 1, EntityType.WOLF.getTypeId());
		if(p.hasPermission("slug.slug")){
			wolfDisguise = setItemNameAndLore(wolfDisguise, ChatColor.GREEN + "Wolf Disguise", new String[]{ChatColor.GRAY + "Disguise as a wolf!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
			wolfDisguise = addGlow(wolfDisguise);
		}else{
			wolfDisguise = setItemNameAndLore(wolfDisguise, ChatColor.DARK_PURPLE + "Wolf Disguise", new String[]{ChatColor.GRAY + "Disguise as a wolf!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
		}
		inventory.setItem(1, wolfDisguise);
		
		ItemStack trampoline = new ItemStack(Material.WOOL, 1, (byte)9);
		if(p.hasPermission("slug.slug")){
			trampoline = setItemNameAndLore(trampoline, ChatColor.GREEN + "Trampoline", new String[]{ChatColor.GRAY + "Spawn a bouncy trampoline!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
			trampoline = addGlow(trampoline);
		}else{
			trampoline = setItemNameAndLore(trampoline, ChatColor.DARK_PURPLE + "Trampoline", new String[]{ChatColor.GRAY + "Spawn a bouncy trampoline!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
		}
		inventory.setItem(3, trampoline);
		
		ItemStack fireworks = new ItemStack(Material.FIREWORK);
		if(p.hasPermission("slug.slug")){
			fireworks = setItemNameAndLore(fireworks, ChatColor.GREEN + "Fireworks", new String[]{ChatColor.GRAY + "Shoot off fireworks!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
			fireworks = addGlow(fireworks);
		}else{
			fireworks = setItemNameAndLore(fireworks, ChatColor.DARK_PURPLE + "Fireworks", new String[]{ChatColor.GRAY + "Shoot off fireworks!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
		}
		inventory.setItem(5, fireworks);
		
		ItemStack cookieBomb = new ItemStack(Material.COOKIE);
		if(p.hasPermission("slug.slug")){
			cookieBomb = setItemNameAndLore(cookieBomb, ChatColor.GREEN + "Cookie Bomb", new String[]{ChatColor.GRAY + "Spawn a ton of cookies!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
			cookieBomb = addGlow(cookieBomb);
		}else{
			cookieBomb = setItemNameAndLore(cookieBomb, ChatColor.DARK_PURPLE + "Cookie Bomb", new String[]{ChatColor.GRAY + "Spawn a ton of cookies!", "", ChatColor.GRAY + "Requires: " + ChatColor.YELLOW + "Slug"});
		}
		inventory.setItem(7,  cookieBomb);
		
		p.openInventory(inventory);
	}
	
	@SuppressWarnings("deprecation")
	public void gadgetsMenuHandler(Player p, int slot){
		if(p.hasPermission("slug.slug")){
			if(slot == 1){
				MobDisguise wolf = new MobDisguise(DisguiseType.WOLF);
				wolf.setViewSelfDisguise(false);
				ArmorStand nameTag = null;
				boolean doRemove = false;
				if(!main.nameTags.containsKey(p.getUniqueId().toString())){
					nameTag = (ArmorStand) Bukkit.getWorld("world").spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
					nameTag.setCustomName(me.WoundedSlug.SlugAPI.main.getRealPrefix(p) + p.getName());
					nameTag.setCustomNameVisible(true);
					nameTag.setVisible(false);
					nameTag.setSmall(true);
					nameTag.setGravity(false);
					main.nameTags.put(p.getUniqueId().toString(), nameTag);
					doRemove = true;
				}
				DisguiseAPI.disguiseToAll(p, wolf);
				ItemStack wolfDisguise = new ItemStack(Material.MONSTER_EGG, 1, EntityType.WOLF.getTypeId());
				wolfDisguise = setItemNameAndLore(wolfDisguise, ChatColor.YELLOW + "" + ChatColor.BOLD + "Wolf Disguise " + ChatColor.GRAY + "(Right click to remove)", new String[]{""});
				p.getInventory().setItem(3, wolfDisguise);
				if(doRemove){
					PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(nameTag.getEntityId());
					main.sendPacket(p, packet);
				}
			}else if(slot == 3){
				if(p.getLocation().getX() < 5.0f){
					boolean fine = true;
					for(Location l : trampolines){
						if(p.getLocation().distance(l) <= 7){
							fine = false;
						}
					}
					if(fine){
						final int x = p.getLocation().getBlockX() - 2;
						final int z = p.getLocation().getBlockZ() - 2;
						final int y = p.getLocation().getBlockY();
						Material[][] blocks = new Material[5][5];
						byte[][] data = new byte[5][5];
						boolean foundSign = false;
						for(int c = 0; c < 5; c++){
							for(int cc = 0; cc < 5; cc++){
								if(p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).getType() == Material.WALL_SIGN || p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).getType() == Material.SIGN || p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).getType() == Material.SIGN_POST)
									foundSign = true;
								blocks[c][cc] = p.getLocation().getWorld().getBlockAt(x + c,y,z + cc).getType();
								data[c][cc] = p.getLocation().getWorld().getBlockAt(x + c,y,z + cc).getData();
							}
						}
						if(foundSign){
							p.sendMessage(ChatColor.RED + "You are too close to a sign!");
						}else{
							final Location l = p.getLocation().getBlock().getLocation();
							trampolines.add(l);
							for(int c = 0; c < 5; c++){
								for(int cc = 0; cc < 5; cc++){
									p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).setType(Material.WOOL);
									if(c == 0 || c == 4 || cc == 0 || cc == 4){
										p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).setData((byte)15);
									}else{
										p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).setData((byte)9);
									}
								}
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable(){
								public void run(){
									trampolines.remove(l);
									for(int c = 0; c < 5; c++){
										for(int cc = 0; cc < 5; cc++){
											p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).setType(blocks[c][cc]);
											p.getLocation().getWorld().getBlockAt(x + c, y, z + cc).setData(data[c][cc]);
										}
									}
								}
							}, 300);
						}
					}else{
						p.sendMessage(ChatColor.RED + "You are too close to another trampoline!");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You are too close the parkour");
				}
			}else if(slot == 5){
				if(!firework.contains(p.getUniqueId().toString())){
					firework.add(p.getUniqueId().toString());
					final String uuid = p.getUniqueId().toString();
					Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable(){
						public void run(){
							firework.remove(uuid);
						}
					}, 200);
				}else{
					p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You already have fireworks!");
				}
				
			}else if(slot == 7){
				World w = p.getWorld();
				Location l = new Location(w, p.getLocation().getX(), p.getLocation().getY()+1.5f, p.getLocation().getZ());
				l = l.toVector().add(p.getLocation().getDirection().multiply(2.5)).toLocation(w);
				for(int c = 0; c < 50; c++){
					l = l.toVector().add(p.getLocation().getDirection().multiply(0.02)).toLocation(w);
					ItemStack is = new ItemStack(Material.COOKIE);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(UUID.randomUUID().toString());
					is.setItemMeta(im);
					Item i = w.dropItemNaturally(l, is);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable(){
						public void run(){
							i.remove();
						}
					}, 50);
				}
			}
		}else{
			p.sendMessage(ChatColor.RED + "You must be atleast rank " + ChatColor.YELLOW + "Slug" + ChatColor.RED + " to use this item!");
		}
	}
	
	@EventHandler
	public void onPicket(PlayerPickupItemEvent e){
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("world"))
			e.setCancelled(true);
	}
	
	private static ItemStack addGlow(ItemStack item){
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
	
	 private static ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
	        ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
	        item.setItemMeta(im);
	        return item;
	    }
	
}
