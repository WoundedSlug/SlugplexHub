package me.WoundedSlug.SlugplexHub;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Cache implements Listener{
	
	private HashMap<String, HashMap<String, Integer>> variables = new HashMap<String, HashMap<String, Integer>>();
	private main Plugin;
	public String ip, port, database, login, password;
	private HashMap<String, String[]> tables = new HashMap<String, String[]>();
	public Cache(main m, String ip, String port, String database, String login, String password, String... data){
		for(int c = 0; c < data.length; c++){
			variables.put(data[c], new HashMap<String, Integer>());
		}
		Plugin = m;
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.login = login;
		this.password = password;
        Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
	}
	
	public int get(String variable, Player p){
		if(variables.containsKey(variable)){
			HashMap<String, Integer> map = variables.get(variable);
			if(map.containsKey(p.getUniqueId().toString()))
				return map.get(p.getUniqueId().toString());
		}			
		return 0;
	}
	
	public void addDatabase(String name, String[] args){
		tables.put(name, args);
	}
	
	@SuppressWarnings("deprecation")
	public void put(final String variable, Player p, final int value){
		final String uuid = p.getUniqueId().toString();
		if(variables.containsKey(variable)){
			HashMap<String, Integer> map = variables.get(variable);
			map.put(uuid, value);
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Plugin, new Runnable(){
				public void run(){
					MySQL my = null;
					Connection c = null;
					try {
						my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(Plugin, ip, port, database, login, password);
						c = my.openConnection();
						String variableInfo[] = variable.split("\\.");
						Statement statement = c.createStatement();		    			
		    			statement.executeUpdate("UPDATE " + variableInfo[0] + " SET " + variableInfo[1] + "='" + value + "' WHERE uuid='" + uuid +"';");
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
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		final String uuid = e.getPlayer().getUniqueId().toString();
		for(HashMap<String, Integer> h : variables.values()){
			if(h.containsKey(uuid))
				h.remove(uuid);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(final AsyncPlayerPreLoginEvent e){
		final String uuid = e.getUniqueId().toString();
		final String name = e.getName();
		for(HashMap<String, Integer> h : variables.values()){
			h.put(uuid, 0);
		}
		
		
		for(String table : tables.keySet()){

			MySQL my = null;
			Connection c = null;
			try {
				my = (MySQL) MySQL.class.getDeclaredConstructor(new Class[]{Plugin.class, String.class, String.class, String.class, String.class, String.class}).newInstance(Plugin, ip, port, database, login, password);
				c = my.openConnection();
				Statement statement = c.createStatement();
	    		ResultSet res = statement.executeQuery("SELECT * FROM " + table + " WHERE uuid = '" + uuid + "';");
	    		if(!res.next()){
	    			String query = "INSERT INTO " + table + " (uuid, name";
	    			for(String arg : tables.get(table)){
	    				query += ", " + arg;
	    			}
	    			query += ") VALUE ('" + uuid +"', '" + name + "'";
	    			for( int x = 0; x < tables.get(table).length; x++){
	    				query += ", '0'";
	    			}
	    			query += ");";
	    			statement.executeUpdate(query);
	    		}else{
	    			for(Entry<String, HashMap<String, Integer>> set : variables.entrySet()){
	    				if(set.getKey().split("\\.")[0].equalsIgnoreCase(table)){
	    					variables.get(set.getKey()).put(uuid, res.getInt(set.getKey().split("\\.")[1]));
	    				}
	    			}
	    			if(res.getString("name") == null || !res.getString("name").equalsIgnoreCase(name)){
	    				statement.executeUpdate("UPDATE " + table + " SET name='" + name + "' WHERE uuid='" + uuid + "';");
	    			}
	    		}
    			my.closeConnection();
    			c.close();
			} catch (Exception e1){
				e1.printStackTrace();
				try {
					my.closeConnection();
					c.close();
				} catch (SQLException e11) {
					e11.printStackTrace();
				}
			}
		}
	
	}
	
}
