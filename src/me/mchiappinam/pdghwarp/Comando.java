package me.mchiappinam.pdghwarp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Comando implements CommandExecutor, Listener {
	
	private Main plugin;
	public Comando(Main main) {
		plugin=main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("warp")) {
			if(sender==plugin.getServer().getConsoleSender()) {
				sender.sendMessage("§3[WARP] §aConsole bloqueado de executar o comando!");
				return true;
			}
			if((args.length>0)&&(!sender.hasPermission("pdgh.op"))) {
				sender.sendMessage("§cUse /warp");
				return true;
			}
            if(((Player)sender).isDead()) {
				sender.sendMessage("§3[WARP] §aVocê está morto!");
				return true;
			}
            if(args.length==0) {
				plugin.warps((Player)sender);
				((Player)sender).openInventory(Main.menu);
				return true;
            }else{
    			if(args[0].equalsIgnoreCase("add")) {
    				if(args.length<2) {
    					sender.sendMessage("§c/warp add <posicaoMenu(0-"+(plugin.getConfig().getInt("menu.tamanho")-1)+"> <nome> -§4- Adiciona uma warp. Mais configurações na config!");
    					return true;
    				}
    				StringBuilder sb = new StringBuilder();
		            sb.append(args[2]);
		            for (int i = 3; i < args.length; i++) {
		              sb.append(" ");
		              sb.append(args[i]);
		            }
    				List<String> lista=new ArrayList<String>();
    				List<String> desc=new ArrayList<String>();
    				desc.add("");
    				desc.add("&cAltere a desc. na config");
    				List<String> msgPerm=new ArrayList<String>();
    				msgPerm.add("&6Apenas &lVIPs&6 podem se teleportar para essa warp.");
    				msgPerm.add("&6Mais informações: &fhttp://pdgh.com.br/vip/");
    				lista=plugin.getConfig().getStringList("warps."+args[1]+".locais");
    				Player p=(Player)sender;
    				lista.add(p.getLocation().getWorld().getName()+";"+p.getLocation().getBlockX()+";"+p.getLocation().getBlockY()+";"+p.getLocation().getBlockZ()+";"+p.getLocation().getYaw()+";"+p.getLocation().getPitch());
    				if(p.getItemInHand().getTypeId()==0)
    					plugin.getConfig().set("warps."+args[1]+".item", 1);
    				else
    					plugin.getConfig().set("warps."+args[1]+".item", p.getItemInHand().getTypeId());
    				if(!plugin.getConfig().contains("warps."+args[1]+".nome"))
    					plugin.getConfig().set("warps."+args[1]+".nome", ""+sb);
    				if(!plugin.getConfig().contains("warps."+args[1]+".itemDesc"))
    					plugin.getConfig().set("warps."+args[1]+".itemDesc", desc);
    				plugin.getConfig().set("warps."+args[1]+".locais", lista);
    				if(!plugin.getConfig().contains("warps."+args[1]+".sistemaPermissao"))
    					plugin.getConfig().set("warps."+args[1]+".sistemaPermissao", false);
    				if(!plugin.getConfig().contains("warps."+args[1]+".permissao"))
    					plugin.getConfig().set("warps."+args[1]+".permissao", "pdgh.vip");
    				if(!plugin.getConfig().contains("warps."+args[1]+".msgPermissao"))
    					plugin.getConfig().set("warps."+args[1]+".msgPermissao", msgPerm);
    				plugin.saveConfig();
    				plugin.reloadConfig();
    				lista.clear();
    				desc.clear();
    				msgPerm.clear();
    				sender.sendMessage("§3[WARP] §aWarp adicionada! Você utilizou o comando:");
    				sender.sendMessage("§3[WARP] §a/warp "+args[0]+" "+args[1]+" "+sb);
    				sender.sendMessage("§3[WARP] §aID do item: "+p.getItemInHand().getTypeId());
    				return true;
    			}}
			return true;
		}
		return false;
	}

}