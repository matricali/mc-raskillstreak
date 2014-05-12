package ras.minecraft.plugin.KillStreak;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * KillStreak
 * Bonus for Double, Triple, Quadra y Penta Kill!
 * @author Jorge Matricali <jorge.matricali@gmail.com>
 */
public class KillStreak extends JavaPlugin implements Listener {
    public long streakTime = 10000000;
    
    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void PlayerKillPlayer(PlayerDeathEvent e){
        getLogger().info("** PLAYER DEATH **");
        if(e.getEntity().getKiller()!=null){
            Player killer = e.getEntity().getKiller();
            int streakCount = 1;
            getLogger().info("===== "+killer.getName() + " =====");
            if(killer.hasMetadata("ras.killstreak.timestamp") && killer.hasMetadata("ras.killstreak.count")){
                long lastTimestamp = killer.getMetadata("ras.killstreak.timestamp").size()>0 ?
                        killer.getMetadata("ras.killstreak.timestamp").get(0).asLong() :
                        getCurrentTimestamp();
                
                getLogger().info("lastTimestamp="+lastTimestamp);
                getLogger().info("getTimestamp()="+getCurrentTimestamp());
                if( getCurrentTimestamp() - lastTimestamp < this.streakTime){
                    if(killer.getMetadata("ras.killstreak.count").size()>0)
                        streakCount = killer.getMetadata("ras.killstreak.count").get(0).asInt();
                    
                    streakCount++;
                    
                    getLogger().info("streakCount="+streakCount);   
                    //We have a kill streak =)
                    if(streakCount>1){
                        String killMessage = "";
                        switch(streakCount){
                            case 2:
                                killMessage = "&cDoble Kill";
                                break;
                            case 3:
                                killMessage = "&4Triple Kill";
                                break;
                            case 4:
                                killMessage = "&4&lQuadra Kill";
                                break;
                            case 5:
                                killMessage = "&4&k+&4&lPenta Kill&4&k+";
                                break;
                        }
                        killMessage = killMessage+" &7"+killer.getDisplayName()+" &7racha de asesinatos, con "+streakCount+" asesinatos consecutivos.";
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', killMessage));
                        killer.getWorld().playSound(killer.getLocation(), Sound.GHAST_SCREAM, 1, 1);
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, streakCount*200, streakCount));
                    }
                    if(streakCount>=5) streakCount=0;
                }
            }
            
            killer.setMetadata("ras.killstreak.count", new FixedMetadataValue(this, streakCount));
            killer.setMetadata("ras.killstreak.timestamp", new FixedMetadataValue(this, getCurrentTimestamp()));
        }
    }
    
    public long getCurrentTimestamp(){
        java.util.Date date= new java.util.Date();
        return date.getTime();
    }
}
