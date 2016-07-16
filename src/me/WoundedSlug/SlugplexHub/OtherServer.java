package me.WoundedSlug.SlugplexHub;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class OtherServer implements PluginMessageListener{

	
	private String server;
	
	private String address;
	private int port;
	
	private boolean online;
	private int playerCount;
	private int maxPlayers;
	private String motd;
	
	
	@SuppressWarnings("deprecation")
	public OtherServer(Plugin pl, String name){
		address = "waiting";
		server = name;
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(pl, new Runnable(){
			public void run(){
				if(address != "waiting")
					ping();
				else{
					if(Bukkit.getOnlinePlayers().size() > 0){
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
					    out.writeUTF("ServerIP");
					    out.writeUTF(server);
					     Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
					     player.sendPluginMessage(pl, "BungeeCord", out.toByteArray());
					}
				}
			}
		}, 10, 10);
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(pl, "BungeeCord");
	    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(pl, "BungeeCord", this);
	}
	
	public String getMotd(){
		return motd;
	}
	
	public int getPlayerCount(){
		return playerCount;
	}

	public int getMaxPlayers(){
		return maxPlayers;
	}
	
	public boolean getOnline(){
		return online;
	}
	
	private void ping(){
	      try{
	         Socket socket = new Socket();
	         OutputStream outputStream;
	         DataOutputStream dataOutputStream;
	         InputStream inputStream;
	         InputStreamReader inputStreamReader;
	         socket.setSoTimeout(500);
	         socket.connect(new InetSocketAddress(address, port));
	         outputStream = socket.getOutputStream();
	         dataOutputStream = new DataOutputStream(outputStream);
	         inputStream = socket.getInputStream();
	         inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));
	         dataOutputStream.write(new byte[]{(byte) 0xFE,(byte) 0x01});
	         int packetId = inputStream.read();
	         if(packetId == -1){
	            dataOutputStream.close();
	            outputStream.close();
	            inputStreamReader.close();
	            inputStream.close();
	            socket.close();
	            throw new IOException("Premature end of stream.");
	         }
	         if(packetId != 0xFF){
	            dataOutputStream.close();
	            outputStream.close();
	            inputStreamReader.close();
	            inputStream.close();
	            socket.close();
	            throw new IOException("Invalid packet ID (" + packetId + ").");
	         }
	         int length = inputStreamReader.read();
	         if(length == -1){
	            dataOutputStream.close();
	            outputStream.close();
	            inputStreamReader.close();
	            inputStream.close();
	            socket.close();
	            throw new IOException("Premature end of stream.");
	         }
	         if(length == 0){
	            dataOutputStream.close();
	            outputStream.close();
	            inputStreamReader.close();
	            inputStream.close();
	            socket.close();
	            throw new IOException("Invalid string length.");
	         }
	         char[] chars = new char[length];
	         if(inputStreamReader.read(chars,0,length) != length){
	            dataOutputStream.close();
	            outputStream.close();
	            inputStreamReader.close();
	            inputStream.close();
	            socket.close();
	            throw new IOException("Premature end of stream.");
	         }
	         String string = new String(chars);
	         if(string.startsWith("§")){
	            String[] data = string.split("\0");
	            motd = data[3];
	            playerCount = Integer.parseInt(data[4]);
	            maxPlayers = Integer.parseInt(data[5]);
	         }
	         else{
	            String[] data = string.split("§");
	            motd = data[0];
	            playerCount = Integer.parseInt(data[1]);
	            maxPlayers = Integer.parseInt(data[2]);
	         }
	         dataOutputStream.close();
	         outputStream.close();
	         inputStreamReader.close();
	         inputStream.close();
	         socket.close();
	      } catch (SocketException exception) {
	          online = false;
	      } catch (IOException exception) {
	    	  online = false;
	      }
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    
		 if (!channel.equals("BungeeCord")) {
		      return;
		    }
		    ByteArrayDataInput in = ByteStreams.newDataInput(message);
		    if(in.readUTF().equalsIgnoreCase("ServerIP")){
		    	if(in.readUTF().equalsIgnoreCase(server)){
			    	address = in.readUTF();
			    	port = in.readShort();
		    	}
		    }
		
	}
	
}
