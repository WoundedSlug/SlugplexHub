package me.WoundedSlug.SlugplexHub;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class FakePlayer implements Listener {

    private Map<String, EntityPlayer> playerMap = new HashMap<>();
    private Map<String, Location> locationMap = new HashMap<>();
    private Map<String, String> commands = new HashMap<>();
    private Plugin pl;
    

    @SuppressWarnings("deprecation")
	public FakePlayer(Plugin plu){
    	Bukkit.getServer().getPluginManager().registerEvents(this, plu);
    	pl = plu;
		new BukkitRunnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(p.getScoreboard() != null){
						Scoreboard s = p.getScoreboard();
						if(s.getTeam("NPC") != null){
							for(EntityPlayer ep : playerMap.values()){
								s.getTeam("NPC").setNameTagVisibility(NameTagVisibility.NEVER);
								s.getTeam("NPC").addEntry(ep.getName());
							}
						}else{
							s.registerNewTeam("NPC");
							for(EntityPlayer ep : playerMap.values()){
								s.getTeam("NPC").setNameTagVisibility(NameTagVisibility.NEVER);
								s.getTeam("NPC").addEntry(ep.getName());
							}
						}
					}
				}
			}
		}.runTaskTimer(plu, 1, 1);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(
		  new PacketAdapter(plu, ConnectionSide.CLIENT_SIDE, Packets.Client.USE_ENTITY) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				for(EntityPlayer ep : playerMap.values()){
					if(packet.getIntegers().getValues().get(0) == ep.getId()){
						Bukkit.getScheduler().scheduleSyncDelayedTask(plu, new Runnable(){
							public void run(){
								event.getPlayer().performCommand(commands.get(ep.getName()));
							}
						},1);
					}
				}
			}	
		 });
    }
    
    
    @EventHandler
    public void onMove(PlayerMoveEvent e){
    	for(EntityPlayer ep : playerMap.values()){
    		if(e.getTo().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName())){
		    	Player p = e.getPlayer();
		    	Location loc = locationMap.get(ep.getName());
				float b;
			    float c;
			    boolean d;
			    double e1;
			    double f;
			    double g;
			    e1 = p.getLocation().getX();
			    f = p.getLocation().getY() + p.getEyeHeight();
			    g = p.getLocation().getZ();
			    b = 1000f;
			    c = 400f;
			    d = true;
			    ep.pitch = 0.0F;
		        if (d) {
		            d = false;
		            double d0 = e1 - loc.getX();
		            double d1 = f - (loc.getY() + ep.getHeadHeight());
		            double d2 = g - loc.getZ();
		            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		
		            float f11 = (float) (MathHelper.b(d2, d0) * 180.0D / 3.141592741012573D) - 90.0F;
		            float f1 = (float) (-(MathHelper.b(d1, d3) * 180.0D / 3.141592741012573D));
		            
		            
		            
		            loc.setPitch(a(loc.getPitch(), f1, c));
		            loc.setYaw(a(loc.getYaw(), f11, b));
		        } else {
		            loc.setYaw(a(ep.aK, ep.aI, 1000.0F));
		        }
		        float f2 = MathHelper.g(ep.aK - ep.aI);
		        
		        if (f2 < -75.0F) {
		            loc.setYaw(ep.aI - 75.0F);
		        }
		
		        if (f2 > 75.0F)
		            loc.setYaw(ep.aI + 75.0F);
				PacketPlayOutEntityLook packet3 = new PacketPlayOutEntityLook(ep.getId(), (byte)(loc.getYaw() * 256/ 360), (byte)(loc.getPitch() * 256 / 360), false);
				for(Player p1 : Bukkit.getOnlinePlayers()){
					if(p1.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString()))
						sendPacket(p, packet3);
				}
				PacketPlayOutEntityHeadRotation packet4 = new PacketPlayOutEntityHeadRotation(ep, (byte)(loc.getYaw() * 256/ 360));
				for(Player p1 : Bukkit.getOnlinePlayers()){
					if(p1.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString()))
						sendPacket(p, packet4);
				}
    		}
    	}
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
    	Player p = e.getPlayer();
    	final String uuid = p.getUniqueId().toString();
		Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
			public void run(){
				for(Entry<String, EntityPlayer> entry : playerMap.entrySet()){
					EntityPlayer ep = entry.getValue();
					Location loc = locationMap.get(entry.getKey());
					PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid) && p1.getLocation().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName()))
							sendPacket(p, packet);
					}
					PacketPlayOutNamedEntitySpawn packet1 = new PacketPlayOutNamedEntitySpawn(ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
							sendPacket(p, packet1);
					}
					PacketPlayOutEntityTeleport packet2 = new PacketPlayOutEntityTeleport(ep.getId(), (int)( loc.getX() * 32), (int)( loc.getY() * 32), (int)( loc.getZ() * 32), (byte) ((int)( loc.getYaw() * 256 / 360)), (byte) ((int) loc.getPitch() * 256 / 360), false);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
							sendPacket(p, packet2);
					}
					
				}
			}
		}, 5);
		Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
			public void run(){
				for(EntityPlayer ep : playerMap.values()){
					PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
							sendPacket(p, packet);
					}
				}
			}
		}, 60);
    }
    
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		final String uuid = p.getUniqueId().toString();
		Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
			public void run(){
				for(EntityPlayer ep : playerMap.values()){
					PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid) && p1.getLocation().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName()))
							sendPacket(p, packet);
					}
					
					PacketPlayOutEntityDestroy packet2 = new PacketPlayOutEntityDestroy(ep.getId());
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid) && p1.getLocation().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName()))
							sendPacket(p, packet2);
					}
			    
					
					PacketPlayOutNamedEntitySpawn packet3 = new PacketPlayOutNamedEntitySpawn(ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid) && p1.getLocation().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName()))
							sendPacket(p, packet3);
					}
					Location loc = locationMap.get(ep.getName());
					PacketPlayOutEntityTeleport packet4 = new PacketPlayOutEntityTeleport(ep.getId(), (int)( loc.getX() * 32), (int)( loc.getY() * 32), (int)( loc.getZ() * 32), (byte) ((int)( loc.getYaw() * 256 / 360)), (byte) ((int) loc.getPitch() * 256 / 360), false);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid) && p1.getLocation().getWorld().getName().equalsIgnoreCase(locationMap.get(ep.getName()).getWorld().getName()))
							sendPacket(p, packet4);
					}
				}
			}
		}, 5);
		Bukkit.getScheduler().runTaskLater(pl, new Runnable(){
			public void run(){
				for(EntityPlayer ep : playerMap.values()){
					PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
					for(Player p1 : Bukkit.getOnlinePlayers()){
						if(p1.getUniqueId().toString().equalsIgnoreCase(uuid))
							sendPacket(p, packet);
					}
				}
			}
		}, 60);
    }
    
    public void addPlayer(String name, String skinData, String skinSignature, Location l, String command){
    	 MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
         WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
         UUID uuid = UUID.randomUUID();
         String npcName = ChatColor.DARK_GRAY+ "[NPC]" + uuid.toString().substring(0, 8);
         GameProfile gameProfile = new GameProfile(uuid, npcName);
         Property p = new Property("textures", skinData, skinSignature);
         gameProfile.getProperties().put("textures", p);
         PlayerInteractManager playerInteractManager = new PlayerInteractManager(nmsWorld);
         EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, playerInteractManager);
         playerMap.put(npcName, npc);
         locationMap.put(npcName, l);
         commands.put(npcName, command);
         ArmorStand as= (ArmorStand) l.getWorld().spawnEntity(new Location(l.getWorld(), l.getX(), l.getY() + 0.85f, l.getZ()), EntityType.ARMOR_STAND);
 		as.setCustomName(name);
 		as.setCustomNameVisible(true);
 		as.setVisible(false);
 		as.setSmall(true);
 		as.setGravity(false);
 		npc.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }
    
    public void addPlayer(String name, String skinName, Location l, String command) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        UUID uuid = Bukkit.getOfflinePlayer(skinName).getUniqueId();
        String npcName = ChatColor.DARK_GRAY+ "[NPC]" + uuid.toString().substring(0, 8);
        GameProfile gameProfile = new GameProfile(uuid, npcName);
        try {
			gameProfile = fillProfileProperties(gameProfile, true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        Property prop = gameProfile.getProperties().get("textures").iterator().next();
        System.out.println(skinName + "");
        System.out.println(prop.getName() + " valued " + prop.getValue() + " signed " + prop.getSignature());
        PlayerInteractManager playerInteractManager = new PlayerInteractManager(nmsWorld);
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, playerInteractManager);
        
        playerMap.put(npcName, npc);
        locationMap.put(npcName, l);
        commands.put(npcName, command);
        ArmorStand as= (ArmorStand) l.getWorld().spawnEntity(new Location(l.getWorld(), l.getX(), l.getY() + 0.85f, l.getZ()), EntityType.ARMOR_STAND);
		as.setCustomName(name);
		as.setCustomNameVisible(true);
		as.setVisible(false);
		as.setSmall(true);
		as.setGravity(false);
		npc.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }


    
	private float a(float f, float f1, float f2) {
	    float f3 = MathHelper.g(f1 - f);
	
	    if (f3 > f2) {
	        f3 = f2;
	    }
	
	    if (f3 < -f2) {
	        f3 = -f2;
	    }
	
	    return f + f3;
	}
	  
	public GameProfile fillProfileProperties(GameProfile profile,
			boolean requireSecure) throws Exception {
			
			//if (!Bukkit.isPrimaryThread())
			//throw new IllegalStateException("NMS.fillProfileProperties cannot be invoked from the main thread.");
			
			MinecraftSessionService sessionService = ((CraftServer) Bukkit.getServer()).getServer().aD();
			
			YggdrasilAuthenticationService auth = ((YggdrasilMinecraftSessionService) sessionService)
			.getAuthenticationService();
			
			URL url = HttpAuthenticationService.constantURL(
			"https://sessionserver.mojang.com/session/minecraft/profile/" +
			UUIDTypeAdapter.fromUUID(profile.getId()));
			
			url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + !requireSecure);
			Method MAKE_REQUEST = null;
			try {
			    MAKE_REQUEST = YggdrasilAuthenticationService.class.getDeclaredMethod("makeRequest", URL.class,
			            Object.class, Class.class);
			    MAKE_REQUEST.setAccessible(true);
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
			MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse)
			MAKE_REQUEST.invoke(auth, url, null, MinecraftProfilePropertiesResponse.class);
			if (response == null)
			return profile;
			
			GameProfile result = new GameProfile(profile.getId(), profile.getName());
			result.getProperties().putAll(response.getProperties());
			profile.getProperties().putAll(response.getProperties());
			
			return result;
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


}
 