package me.mchiappinam.pdghwarp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
	
	private Main plugin;
	public Listeners(Main main) {
		plugin=main;
	}



	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
	    ItemStack clicked = e.getCurrentItem();
	    Inventory inventory = e.getInventory();
	    if (inventory.getName().equals(Main.menu.getName())) {
	    	e.setCancelled(true);
	    	if ((e.getCurrentItem() != null)&&(!e.getCurrentItem().getType().equals(Material.AIR))) {
	    		for(String warps : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
				    if (clicked.getItemMeta().getDisplayName().contains(plugin.getConfig().getString("warps."+warps+".nome").replaceAll("&", "§"))) {
						if(plugin.getConfig().getBoolean("warps."+warps+".sistemaPermissao"))
							if(!p.hasPermission(plugin.getConfig().getString("warps."+warps+".permissao"))) {
								for(String msg : plugin.getConfig().getStringList("warps."+warps+".msgPermissao"))
									p.sendMessage(msg.replaceAll("&", "§"));
						    	p.closeInventory();
								return;
							}
				    	List<String> locais=new ArrayList<String>();
				    	locais=plugin.getConfig().getStringList("warps."+warps+".locais");
				    	Random r = new Random();
				    	int selecionado = r.nextInt(locais.size());
				    	int etapa=0;
				    	Location spawn;
				    	String zero=null;
				    	String um=null;
				    	String dois=null;
				    	String tres=null;
				    	String quatro=null;
				    	String cinco=null;
						for(String lo : locais.get(selecionado).split(";")) {
							if(etapa==0)
								zero=lo;
							else if(etapa==1)
								um=lo;
							else if(etapa==2)
								dois=lo;
							else if(etapa==3)
								tres=lo;
							else if(etapa==4)
								quatro=lo;
							else if(etapa==5)
								cinco=lo;
							etapa++;
						}
						spawn = new Location(plugin.getServer().getWorld(zero),Double.parseDouble(um)+.5,Double.parseDouble(dois)+0.1,Double.parseDouble(tres)+.5,Float.parseFloat(quatro),Float.parseFloat(cinco));
						p.closeInventory();
						plugin.players.put(p.getName().toLowerCase(), 40);
						p.teleport(spawn);
				    	p.closeInventory();
				    	//p.sendMessage("§cTeleportado!");
				    	plugin.sendActionText(p, "§e§lTeleportado! §7Você está invencível por...");
				    	p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0, 5);
		            	p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
					    return;
				    }
	    		}
		    }
	    }
	}
	
	
	


	@EventHandler(ignoreCancelled=false, priority=EventPriority.MONITOR)
	private void onHit(EntityDamageByEntityEvent e) {
	    if(!e.isCancelled())
			if(e.getEntity() instanceof Player)
				if(e.getDamager() instanceof Player||e.getDamager() instanceof Projectile) {
					Player ent = (Player)e.getEntity();
					Player dam = null;
					Projectile a = null;
					if(e.getDamager() instanceof Player)
						dam=(Player)e.getDamager();
					else {
						a = (Projectile) e.getDamager();
						if(a.getShooter() instanceof Player)
							dam=(Player)a.getShooter();
					}
				if(a!=null)
					a.remove();
		        if (plugin.players.containsKey(ent.getName().toLowerCase())) {
		        	e.setCancelled(true);
		        	ent.sendMessage("§e"+dam.getName()+" §cte atacou com você invencível!");
		        	plugin.sendActionText(dam, "§e"+ent.getName()+" §c§lestá invencível por "+((double)plugin.players.get(ent.getName().toLowerCase())/10)+"s");
		        }else if (plugin.players.containsKey(dam.getName().toLowerCase())) {
	        		e.setCancelled(true);
		        	//plugin.sendActionText((Player)e.getEntity(), "§e"+p.getName()+" §c§lestá invencível por "+TimeUnit.MILLISECONDS.toMillis(plugin.players.get(p))+"s");
		        }
	        }
	}
    

	
	@EventHandler(priority=EventPriority.HIGHEST)
	private void onQuit(PlayerQuitEvent e) {
		if(plugin.players.containsKey(e.getPlayer().getName().toLowerCase())) {
			plugin.players.remove(e.getPlayer().getName().toLowerCase());
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	private void onKick(PlayerKickEvent e) {
		if(plugin.players.containsKey(e.getPlayer().getName().toLowerCase())) {
			plugin.players.remove(e.getPlayer().getName().toLowerCase());
		}
	}
	
}