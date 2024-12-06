package me.mchiappinam.pdghwarp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Main extends JavaPlugin implements Listener {
	public static Inventory menu = null;
	protected HashMap<String, Integer> players = new HashMap<String, Integer>();

	public void onEnable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2iniciando...");
		File file = new File(getDataFolder(),"config.yml");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2verificando se a config existe...");
		if(!file.exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2config inexistente, criando config...");
				saveResource("config_template.yml",false);
				File file2 = new File(getDataFolder(),"config_template.yml");
				file2.renameTo(new File(getDataFolder(),"config.yml"));
				getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2config criada");
			}catch(Exception e) {getServer().getConsoleSender().sendMessage("§c[PDGHWarp] §cERRO AO CRIAR CONFIG");}
		}
		menu=getServer().createInventory(null, getConfig().getInt("menu.tamanho"), getConfig().getString("menu.nome").replaceAll("&", "§").replaceAll(">>", "»").replaceAll("<<", "«"));
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2registrando eventos...");
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2eventos registrados");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2definindo comandos...");
	    getServer().getPluginCommand("warp").setExecutor(new Comando(this));
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2comandos definidos");
		timer();
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2ativado - Developed by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2Acesse: http://pdgh.com.br/");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2Acesse: https://hostload.com.br/");
	}
	    
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2desativado - Developed by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2Acesse: http://pdgh.com.br/");
		getServer().getConsoleSender().sendMessage("§3[PDGHWarp] §2Acesse: https://hostload.com.br/");
	}
	
	private void timer() {
	  	getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
	  		public void run() {
	  			if(!players.isEmpty()) {
	  				final List<String> l = new ArrayList<String>();
		  			for(String p : players.keySet()) {
		  				if(getServer().getPlayerExact(p)==null) {
		  					l.add(p);
		  				}else{
			  				int i=players.get(p)-1;
			  				players.put(p, i);
					    	sendActionText(getServer().getPlayerExact(p), "§e§lTeleportado! §7Você está invencível por "+((double)i/10)+"s");
			  				if(players.get(p)<=0) {
			  					l.add(p);
				  				sendActionText(getServer().getPlayerExact(p), "§4§lSua invencibilidade de teleporte acabou! >:)");
			  				}
		  				}
		  			}
		  		  	for(String s : l)
		  		  		players.remove(s);
	  			}
	  		}
	  	}, 2, 2);
	}

	public void sendActionText(Player p, String message){
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }


	protected void warps(Player p) {
		for(String warps : getConfig().getConfigurationSection("warps").getKeys(false)) {
			if (StringUtils.isNumeric(warps)) {
				int slotDoItem=Integer.parseInt(warps);
				ItemStack a = new ItemStack(Material.getMaterial(getConfig().getInt("warps."+warps+".item")), 1);
				ItemMeta b = a.getItemMeta();
				List<String> l = new ArrayList<String>();
				if(getConfig().getBoolean("warps."+warps+".sistemaPermissao")) {
					if(!p.hasPermission(getConfig().getString("warps."+warps+".permissao"))) {
						b.setDisplayName("§c"+getConfig().getString("warps."+warps+".nome")
								.replaceAll("&0", "§c")
								.replaceAll("&1", "§c")
								.replaceAll("&2", "§c")
								.replaceAll("&3", "§c")
								.replaceAll("&4", "§c")
								.replaceAll("&5", "§c")
								.replaceAll("&6", "§c")
								.replaceAll("&7", "§c")
								.replaceAll("&8", "§c")
								.replaceAll("&9", "§c")
								.replaceAll("&a", "§c")
								.replaceAll("&b", "§c")
								.replaceAll("&c", "§c")
								.replaceAll("&d", "§c")
								.replaceAll("&e", "§c")
								.replaceAll("&f", "§c")
								.replaceAll("&l", "§c"));
					}else{
						b.setDisplayName(getConfig().getString("warps."+warps+".nome").replaceAll("&", "§"));
					}
				}else{
					b.setDisplayName(getConfig().getString("warps."+warps+".nome").replaceAll("&", "§"));
				}
				
				for(String msg : getConfig().getStringList("warps."+warps+".itemDesc"))
				    l.add(msg.replaceAll("&", "§"));
			    b.setLore(l);
			    a.setItemMeta(b);
			    menu.setItem(slotDoItem, a);
			}
		}
	}
	
}