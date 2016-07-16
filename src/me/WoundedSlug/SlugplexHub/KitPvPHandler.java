package me.WoundedSlug.SlugplexHub;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class KitPvPHandler {

	public static Inventory getInventory(Player p, Cache cache){
		Inventory inventory = Bukkit.createInventory(p, 27, "SlugPvP Shop");
		//Row 1
		ItemStack jester = new ItemStack(Material.RAW_FISH);
		if(cache.get("kitpvp.jester", p) > 0){
			jester = setItemNameAndLore(jester, ChatColor.GREEN + "Jester I", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Jester Fish", ChatColor.GRAY + "Speed I", "", ChatColor.GOLD + "150 Points"});
			jester = addGlow(jester);
		}else{
			jester = setItemNameAndLore(jester, ChatColor.DARK_PURPLE + "Jester I", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Jester Fish", ChatColor.GRAY + "Speed I", "", ChatColor.GOLD + "150 Points"});
		}
		
		ItemStack scout = new ItemStack(Material.STONE_SWORD);
		scout = removeAttributes(scout);
		scout = setItemNameAndLore(scout, ChatColor.GREEN + "Scout I", new String[]{ChatColor.GRAY + "Leather Armor", ChatColor.GRAY + "Stone Sword W/ Sharpness I", ChatColor.GRAY + "Speed I", "", ChatColor.GOLD + "Free"});
		scout = addGlow(scout);
		
		ItemStack tank = new ItemStack(Material.IRON_CHESTPLATE);
		tank = removeAttributes(tank);
		tank = setItemNameAndLore(tank, ChatColor.GREEN + "Tank I", new String[]{ChatColor.GRAY + "Iron Armor", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", ChatColor.GRAY + "Slowness II", "", ChatColor.GOLD + "Free"});
		tank = addGlow(tank);
		
		ItemStack archer = new ItemStack(Material.BOW);
		if(cache.get("kitpvp.archer", p) > 0){
			archer = setItemNameAndLore(archer, ChatColor.GREEN + "Archer I", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Archer Bow", ChatColor.GRAY + "Wooden Sword", "", ChatColor.GOLD + "150 Points"});
			archer = addGlow(archer);
		}else{
			archer = setItemNameAndLore(archer, ChatColor.DARK_PURPLE + "Archer I", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Archer Bow", ChatColor.GRAY + "Wooden Sword", "", ChatColor.GOLD + "150 Points"});
		}
		
		inventory.setItem(1, scout);
		inventory.setItem(3, tank);
		inventory.setItem(5, jester);
		inventory.setItem(7, archer);
		
		
		
		
		ItemStack jester2 = new ItemStack(Material.RAW_FISH);
		if(cache.get("kitpvp.jester", p) > 1){
			jester2 = setItemNameAndLore(jester2, ChatColor.GREEN + "Jester II", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Jester Fish", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "250 Points"});
			jester2 = addGlow(jester2);
		}else{
			jester2 = setItemNameAndLore(jester2, ChatColor.DARK_PURPLE + "Jester II", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Jester Fish", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "250 Points"});
		}
		
		ItemStack scout2 = new ItemStack(Material.STONE_SWORD);
		scout2 = removeAttributes(scout2);
		if(cache.get("kitpvp.scout", p) > 0){
    		scout2 = setItemNameAndLore(scout2, ChatColor.GREEN + "Scout II", new String[]{ChatColor.GRAY + "Leather Armor", ChatColor.GRAY + "Stone Sword W/ Sharpness I", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "250 Points"});
			scout2 = addGlow(scout2);
		}else{
			scout2 = setItemNameAndLore(scout2, ChatColor.DARK_PURPLE + "Scout II", new String[]{ChatColor.GRAY + "Leather Armor", ChatColor.GRAY + "Stone Sword W/ Sharpness I", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "250 Points"});
		}
		
		ItemStack tank2 = new ItemStack(Material.IRON_CHESTPLATE);
		tank2 = removeAttributes(tank2);
		if(cache.get("kitpvp.tank", p) > 0){
			tank2 = setItemNameAndLore(tank2, ChatColor.GREEN + "Tank II", new String[]{ChatColor.GRAY + "Iron Armor", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "250 Points"});
			tank2 = addGlow(tank2);
		}else{
			tank2 = setItemNameAndLore(tank2, ChatColor.DARK_PURPLE + "Tank II", new String[]{ChatColor.GRAY + "Iron Armor", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "250 Points"});
		}
		
		ItemStack archer2 = new ItemStack(Material.BOW);
		if(cache.get("kitpvp.archer", p) > 1){
			archer2 = setItemNameAndLore(archer2, ChatColor.GREEN + "Archer II", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Improved Archer Bow", ChatColor.GRAY + "Wooden Sword", "", ChatColor.GOLD + "250 Points"});
			archer2 = addGlow(archer2);
		}else{
			archer2 = setItemNameAndLore(archer2, ChatColor.DARK_PURPLE + "Archer II", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Improved Archer Bow", ChatColor.GRAY + "Wooden Sword", "", ChatColor.GOLD + "250 Points"});
		}
		
		inventory.setItem(1 + 9, scout2);
		inventory.setItem(3 + 9, tank2);
		inventory.setItem(5 + 9, jester2);
		inventory.setItem(7 + 9, archer2);
		
		
		
		ItemStack jester3 = new ItemStack(Material.RAW_FISH);
		if(cache.get("kitpvp.jester", p) > 2){
			jester3 = setItemNameAndLore(jester3, ChatColor.GREEN + "Jester III", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Improved Jester Fish", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "400 Points"});
			jester3 = addGlow(jester3);
		}else{
			jester3 = setItemNameAndLore(jester3, ChatColor.DARK_PURPLE + "Jester III", new String[]{ChatColor.GRAY + "Rainbow Leather Armor", ChatColor.GRAY + "Improved Jester Fish", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "400 Points"});
		}
		
		ItemStack scout3 = new ItemStack(Material.STONE_SWORD);
		scout3 = removeAttributes(scout3);
		if(cache.get("kitpvp.scout", p) > 1){
    		scout3 = setItemNameAndLore(scout3, ChatColor.GREEN + "Scout III", new String[]{ChatColor.GRAY + "Leather Armor W/ Prot III Chest", ChatColor.GRAY + "Stone Sword W/ Sharpness I", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "400 Points"});
			scout3 = addGlow(scout3);
		}else{
			scout3 = setItemNameAndLore(scout3, ChatColor.DARK_PURPLE + "Scout III", new String[]{ChatColor.GRAY + "Leather Armor W/ Prot III Chest", ChatColor.GRAY + "Stone Sword W/ Sharpness I", ChatColor.GRAY + "Speed II", "", ChatColor.GOLD + "400 Points"});
		}
		
		ItemStack tank3 = new ItemStack(Material.IRON_CHESTPLATE);
		tank3 = removeAttributes(tank3);
		if(cache.get("kitpvp.tank", p) > 1){
			tank3 = setItemNameAndLore(tank3, ChatColor.GREEN + "Tank III", new String[]{ChatColor.GRAY + "Iron Armor W/ Prot II Chest", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "400 Points"});
			tank3 = addGlow(tank3);
		}else{
			tank3 = setItemNameAndLore(tank3, ChatColor.DARK_PURPLE + "Tank III", new String[]{ChatColor.GRAY + "Iron Armor W/ Prot II Chest", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "400 Points"});
		}
		
		ItemStack archer3 = new ItemStack(Material.BOW);
		if(cache.get("kitpvp.archer", p) > 2){
			archer3 = setItemNameAndLore(archer3, ChatColor.GREEN + "Archer III", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Improved Archer Bow", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "400 Points"});
			archer3 = addGlow(archer3);
		}else{
			archer3 = setItemNameAndLore(archer3, ChatColor.DARK_PURPLE + "Archer III", new String[]{ChatColor.GRAY + "Chain Armor", ChatColor.GRAY + "Improved Archer Bow", ChatColor.GRAY + "Wooden Sword W/ Sharpness I", "", ChatColor.GOLD + "400 Points"});
		}
		
		inventory.setItem(1 + 9*2, scout3);
		inventory.setItem(3 + 9*2, tank3);
		inventory.setItem(5 + 9*2, jester3);
		inventory.setItem(7 + 9*2, archer3);
		
		
		
		return inventory;
	}
	
	@SuppressWarnings("deprecation")
	public static void handleClick(Player p, int position, Cache cache, Plugin pl){
		if(position == 1){
			p.sendMessage(ChatColor.RED + "You already have that unlocked!");
		}else if(position == 3){
			p.sendMessage(ChatColor.RED + "You already have that unlocked!");
		}else if(position == 5){
    		if(cache.get("kitpvp.jester", p) == 0){
                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 150){
                	String uuid = p.getUniqueId().toString();
                	final String ip = cache.ip;
                	final String port = cache.port;
                	final String database = cache.database;
                	final String login = cache.login;
                	final String password = cache.password;
                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
        				public void run(){
        					MySQL my = null;
        					Connection c = null;
        					try {
        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
        						c = my.openConnection();
        						Statement statement = c.createStatement();		    			
        		    			if(statement.executeUpdate("UPDATE kitpvp SET jester='1' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>149 and jester='0';") > 0){
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
		        		    						p1.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Jester I\"");
		        		    						me.WoundedSlug.SlugAPI.main.addPoints(p1.getUniqueId().toString(), -150);
		        		    	                	cache.put("kitpvp.jester", p1, 1);
        		    							}
        		    						}
        		    					}
        		    				}, 1);
        		    			}else{
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
        		    						}
        		    						
        		    					}
        		    				}, 1);
        		    			}
        		    			statement.close();
        		    			my.closeConnection();
        		    			c.close();
        					} catch (Exception e){
        						e.printStackTrace();
        						try {
        							my.closeConnection();
        							c.close();
        						} catch (SQLException e1) {
        							e1.printStackTrace();
        						}
        					}
        				}
        			}, 1);
                }else{
                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                }
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 7){
    		if(cache.get("kitpvp.archer", p) == 0){
                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 150){
                	String uuid = p.getUniqueId().toString();
                	final String ip = cache.ip;
                	final String port = cache.port;
                	final String database = cache.database;
                	final String login = cache.login;
                	final String password = cache.password;
                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
        				public void run(){
        					MySQL my = null;
        					Connection c = null;
        					try {
        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
        						c = my.openConnection();
        						Statement statement = c.createStatement();		    			
        		    			if(statement.executeUpdate("UPDATE kitpvp SET archer='1' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>149 and archer='0';") > 0){
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
		        		    						p1.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Archer I\"");
		        		    						me.WoundedSlug.SlugAPI.main.addPoints(p1.getUniqueId().toString(), -150);
		        		    	                	cache.put("kitpvp.archer", p1, 1);
        		    							}
        		    						}
        		    					}
        		    				}, 1);
        		    			}else{
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
        		    						}
        		    						
        		    					}
        		    				}, 1);
        		    			}
        		    			statement.close();
        		    			my.closeConnection();
        		    			c.close();
        					} catch (Exception e){
        						e.printStackTrace();
        						try {
        							my.closeConnection();
        							c.close();
        						} catch (SQLException e1) {
        							e1.printStackTrace();
        						}
        					}
        				}
        			}, 1);
                }else{
                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                }
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 1 + 9){
    		if(cache.get("kitpvp.scout", p) == 0){
                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
                	String uuid = p.getUniqueId().toString();
                	final String ip = cache.ip;
                	final String port = cache.port;
                	final String database = cache.database;
                	final String login = cache.login;
                	final String password = cache.password;
                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
        				public void run(){
        					MySQL my = null;
        					Connection c = null;
        					try {
        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
        						c = my.openConnection();
        						Statement statement = c.createStatement();		    			
        		    			if(statement.executeUpdate("UPDATE kitpvp SET scout='1' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>249 and scout='0';") > 0){
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
        		    								p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Scout II\"");
		        		    						me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
		        		    	                	cache.put("kitpvp.scout", p1, 1);
        		    							}
        		    						}
        		    					}
        		    				}, 1);
        		    			}else{
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
        		    						}
        		    						
        		    					}
        		    				}, 1);
        		    			}
        		    			statement.close();
        		    			my.closeConnection();
        		    			c.close();
        					} catch (Exception e){
        						e.printStackTrace();
        						try {
        							my.closeConnection();
        							c.close();
        						} catch (SQLException e1) {
        							e1.printStackTrace();
        						}
        					}
        				}
        			}, 1);
                }else{
                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                }
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 3 + 9){
    		if(cache.get("kitpvp.tank", p) == 0){
                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
                	String uuid = p.getUniqueId().toString();
                	final String ip = cache.ip;
                	final String port = cache.port;
                	final String database = cache.database;
                	final String login = cache.login;
                	final String password = cache.password;
                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
        				public void run(){
        					MySQL my = null;
        					Connection c = null;
        					try {
        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
        						c = my.openConnection();
        						Statement statement = c.createStatement();		    			
        		    			if(statement.executeUpdate("UPDATE kitpvp SET tank='1' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>249 and tank='0';") > 0){
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Tank II\"");
        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
        		    			                	cache.put("kitpvp.tank", p, 1);
        		    							}
        		    						}
        		    					}
        		    				}, 1);
        		    			}else{
        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
        		    					public void run(){
        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
        		    						}
        		    						
        		    					}
        		    				}, 1);
        		    			}
        		    			statement.close();
        		    			my.closeConnection();
        		    			c.close();
        					} catch (Exception e){
        						e.printStackTrace();
        						try {
        							my.closeConnection();
        							c.close();
        						} catch (SQLException e1) {
        							e1.printStackTrace();
        						}
        					}
        				}
        			}, 1);
                }else{
                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
                }
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 5 + 9){
    		if(cache.get("kitpvp.jester", p) <= 1){
    			if(cache.get("kitpvp.jester", p) == 1){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET jester='2' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>249 and jester='1';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Jester II\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
	        		    			                	cache.put("kitpvp.jester", p, 2);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Jester I before you can purchase Jester II");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 7 + 9){
    		if(cache.get("kitpvp.archer", p) <= 1){
    			if(cache.get("kitpvp.archer", p) == 1){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 250){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET archer='2' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>249 and archer='1';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Archer II\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -250);
	        		    			                	cache.put("kitpvp.archer", p, 2);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Archer I before you can purchase Archer II");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 1 + 9*2){
    		//Scout III
    		if(cache.get("kitpvp.scout", p) <= 1){
    			if(cache.get("kitpvp.scout", p) == 1){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 400){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET scout='2' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>399 and scout='1';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Scout III\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -400);
	        		    			                	cache.put("kitpvp.scout", p, 2);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Scout II before you can purchase Scout III");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 3 + 9*2){
    		//Tank III
    		if(cache.get("kitpvp.tank", p) <= 1){
    			if(cache.get("kitpvp.tank", p) == 1){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 400){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET tank='2' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>399 and tank='1';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Tank III\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -400);
	        		    			                	cache.put("kitpvp.tank", p, 2);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Tank II before you can purchase Tank III");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 5 + 9*2){
    		//Jester III
    		if(cache.get("kitpvp.jester", p) <= 2){
    			if(cache.get("kitpvp.jester", p) == 2){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 400){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET jester='3' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>399 and jester='2';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Jester III\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -400);
	        		    			                	cache.put("kitpvp.jester", p, 3);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Jester II before you can purchase Jester III");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}else if(position == 7 + 9*2){
    		if(cache.get("kitpvp.archer", p) <= 2){
    			if(cache.get("kitpvp.archer", p) == 2){
	                if(me.WoundedSlug.SlugAPI.main.getPoints(p.getUniqueId().toString()) >= 400){
	                	String uuid = p.getUniqueId().toString();
	                	final String ip = cache.ip;
	                	final String port = cache.port;
	                	final String database = cache.database;
	                	final String login = cache.login;
	                	final String password = cache.password;
	                	Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, new Runnable(){
	        				public void run(){
	        					MySQL my = null;
	        					Connection c = null;
	        					try {
	        						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(pl, ip, port, database, login, password);
	        						c = my.openConnection();
	        						Statement statement = c.createStatement();		    			
	        		    			if(statement.executeUpdate("UPDATE kitpvp SET archer='3' WHERE uuid='" + uuid +"' and (SELECT points FROM points WHERE uuid='" + uuid + "')>399 and archer='2';") > 0){
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid)){
	        		    			                	p.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Purchased " +ChatColor.GOLD  + "" + ChatColor.BOLD + "\"Archer III\"");
	        		    			                	me.WoundedSlug.SlugAPI.main.addPoints(p.getUniqueId().toString(), -400);
	        		    			                	cache.put("kitpvp.archer", p, 3);
	        		    							}
	        		    						}
	        		    					}
	        		    				}, 1);
	        		    			}else{
	        		    				Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable(){
	        		    					public void run(){
	        		    						for(Player p1 : Bukkit.getOnlinePlayers()){
	        		    							if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
	        		    								p1.sendMessage(ChatColor.RED + "Insufficient Funds or You already have that unlocked!");
	        		    						}
	        		    						
	        		    					}
	        		    				}, 1);
	        		    			}
	        		    			statement.close();
	        		    			my.closeConnection();
	        		    			c.close();
	        					} catch (Exception e){
	        						e.printStackTrace();
	        						try {
	        							my.closeConnection();
	        							c.close();
	        						} catch (SQLException e1) {
	        							e1.printStackTrace();
	        						}
	        					}
	        				}
	        			}, 1);
	                }else{
	                	p.sendMessage(ChatColor.RED + "Insufficient Funds!");
	                }
    			}else{
    				p.sendMessage(ChatColor.RED + "You need Archer II before you can purchase Archer III");
    			}
        	}else{
        		p.sendMessage(ChatColor.RED + "You already have that unlocked!");
        	}
    	}
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
	
	 private static ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
	        ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
	        item.setItemMeta(im);
	        return item;
	    }
	
}
