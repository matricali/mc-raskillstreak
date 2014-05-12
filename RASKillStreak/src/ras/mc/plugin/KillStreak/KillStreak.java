package ras.mc.plugin.KillStreak;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
    public long streakTime = 3000;
    
    @EventHandler
    public void PlayerKillPlayer(PlayerDeathEvent e){
        if(e.getEntity().getKiller()!=null){
            Player killer = e.getEntity().getKiller();
            int streakCount = 0;
            
            if(killer.hasMetadata("ras.killstreak.timestamp") && killer.hasMetadata("ras.killstreak.count")){
                long lastTimestamp = killer.getMetadata("ras.killstreak.timestamp").get(0).asLong();
                if( getCurrentTimestap() - lastTimestamp < this.streakTime){
                    streakCount = killer.getMetadata("ras.killstreak.count").get(0).asInt();
                    
                    //We have a kill streak =)
                    if(streakCount>1){
                        Bukkit.broadcastMessage(killer.getName()+" racha de asesinatos, con "+streakCount+" asesinatos consecutivos");
                        killer.getWorld().playSound(killer.getLocation(), Sound.GHAST_SCREAM, 1, 1);
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, streakCount*2, streakCount));
                    }
                    if(streakCount==5) streakCount=0;
                }
            }
            
            streakCount++;      
            killer.setMetadata("ras.killstreak.count", new FixedMetadataValue(this, streakCount));
            killer.setMetadata("ras.killstreak.timestamp", new FixedMetadataValue(this, getCurrentTimestap()));
        }
    }
    
    public long getCurrentTimestap(){
        java.util.Date date= new java.util.Date();
        return date.getTime();
    }
}
