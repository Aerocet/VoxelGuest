package com.thevoxelbox.voxelguest;

import com.thevoxelbox.permissions.PermissionsManager;
import com.thevoxelbox.voxelguest.modules.BukkitEventWrapper;
import com.thevoxelbox.voxelguest.modules.MetaData;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.modules.ModuleConfiguration;
import com.thevoxelbox.voxelguest.modules.ModuleEvent;
import com.thevoxelbox.voxelguest.modules.Setting;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The World Protection Module was created to help maintain various server
 * aspects, such as grief prevention. The World Protection Module offers
 * extreme amounts of customization based on config variables.
 * 
 * Handles:
 *      Block Drops
 *      Leaf Decay
 *      Ice Melting/Forming
 *      Snow Melting/Forming
 *      All Fire/Explosion Events
 *      Enchanting(allow or disallow)
 *      Food Level Management
 *      Vehicle Damage
 *      Vehicle Creation
 *      Weather Controls
 *      Portal Creation
 */
@MetaData(name="World Protection", description="Various world protection methods.")
public class WorldProtectionModule extends Module{
    public Server s = Bukkit.getServer();
    public HashSet<Integer> bannedblocks = new HashSet<Integer>();
    public HashSet<Integer> banneditems = new HashSet<Integer>();
    

    public WorldProtectionModule(){
        super(WorldProtectionModule.class.getAnnotation(MetaData.class));
    }
    
    class WorldProtectionConfiguration extends ModuleConfiguration {
        @Setting("disable-block-drops") public boolean blockdrops = false;
        @Setting("disable-leaf-decay") public boolean leafdecay = false;
        @Setting("disable-ice-melting") public boolean icemelt = false;
        @Setting("disable-snow-melting") public boolean snowmelt = false;
        @Setting("disable-ice-formation") public boolean iceform = false;
        @Setting("disable-snow-formatin") public boolean snowform = false;
        @Setting("disable-block-burning") public boolean blockburn = false;
        @Setting("disable-fire-spread") public boolean firespred = false;
        @Setting("disable-enchanting") public boolean enchanting = false;
        @Setting("disable-food-changes") public boolean foodchange = false;
        @Setting("unplacable-blocks") public String unplacable = "8,9,10,11,46";
        @Setting("unusable-items") public String unusableitems = "325,326,327";
        
        public WorldProtectionConfiguration(WorldProtectionModule parent) {
            super(parent);
        }
    }
    
    @Override
    public void enable() {
        bannedblocks.clear();
        banneditems.clear();
        
        String[] st1 = getConfiguration().getString("unplacable-blocks").split(",");
        String[] st2 = getConfiguration().getString("unusable-items").split(",");
        try{
            for(String str : st1){
                bannedblocks.add(Integer.parseInt(str));
            }
            
            for(String str : st2){
                banneditems.add(Integer.parseInt(str));
            }
        }
        catch(Exception e){
            VoxelGuest.log("Corrupted or invalidate configuration file. Cannot parse unsuable blocks/items", 1);
        }
    }

    @Override
    public String getLoadMessage() {
        return "World Protection has been loaded.";
    }
    
    @Override
    public void disable() {
        return;
    }
    
    /*
     * World Protection - BlockBreak Event
     * Written by: Razorcane
     * 
     * Handles Block Drops.
     */
    @ModuleEvent(event=BlockBreakEvent.class)
    public void onBlockBreak(BukkitEventWrapper wrapper){
        BlockBreakEvent event = (BlockBreakEvent) wrapper.getEvent();
        Player p = event.getPlayer();
        Block b = event.getBlock();
        
        if(getConfiguration().getBoolean("diable-block-drops")){
            b.setType(Material.AIR);
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - BlockPlace Event
     * Written by: Razorcane
     * 
     * Handles prevention of certain blocks from being placed.
     */
    @ModuleEvent(event=BlockPlaceEvent.class)
    public void onBlockPlace(BukkitEventWrapper wrapper){
        BlockPlaceEvent event = (BlockPlaceEvent) wrapper.getEvent();
        Player[] p = Bukkit.getOnlinePlayers();
        int onlinecount = Bukkit.getOnlinePlayers().length;
        Block b = event.getBlock();
        
        if(bannedblocks.contains(b.getTypeId())){
            event.setCancelled(true);
            
            for(int i = 0; i < onlinecount; i++){
                if(PermissionsManager.getHandler().hasPermission(p[i].getName(), "voxelguest.protection.bannedblocks.warning")){
                    p[i].sendMessage("§9[VG] §8Player §c" + event.getPlayer().getName() + " §8tried to §bplace §9" + b.getType().toString() + "§8(§9" + b.getTypeId() + "§8)");
                }
            }
        }
    }
    
    /*
     * World Protection - PlayerInteract Event
     * Written by: Razorcane
     * 
     * Handles the prevention of using restricted items.
     */
    @ModuleEvent(event=PlayerInteractEvent.class)
    public void onPlayerInteract(BukkitEventWrapper wrapper){
        PlayerInteractEvent event = (PlayerInteractEvent) wrapper.getEvent();
        Player[] p = Bukkit.getOnlinePlayers();
        int onlinecount = Bukkit.getOnlinePlayers().length;
        ItemStack is = event.getItem();
        
        if(banneditems.contains(is.getTypeId())){
            event.setCancelled(true);
            
            for(int i = 0; i < onlinecount; i++){
                if(PermissionsManager.getHandler().hasPermission(p[i].getName(), "voxelguest.protection.bannedblocks.warning")){
                    p[i].sendMessage("§9[VG] §8Player &c" + event.getPlayer().getName() + " §8tried to §buse §9" + is.getType().toString() + "§8(§9" + is.getTypeId() + "§8)");
                }
            }
        }
        
        
    }
    
    /*
     * World Protection - LeavesDecay Event
     * Written by: Razorcane
     * 
     * Handles leaf decay, obviously.
     */
    @ModuleEvent(event=LeavesDecayEvent.class)
    public void onLeavesDecay(BukkitEventWrapper wrapper){
        LeavesDecayEvent event = (LeavesDecayEvent) wrapper.getEvent();
        
        if(getConfiguration().getBoolean("disable-leaf-decay")){
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - BlockFade Event
     * Written by: Razorcane
     * 
     * Handles Snow/Ice Melting
     */
    @ModuleEvent(event=BlockFadeEvent.class)
    public void onBlockFade(BukkitEventWrapper wrapper){
        BlockFadeEvent event = (BlockFadeEvent) wrapper.getEvent();
        Block b = event.getBlock();
        
        if(b.getType().equals(Material.ICE) && getConfiguration().getBoolean("disable-ice-melting")){
            event.setCancelled(true);
        }
        
        if(b.getType().equals(Material.SNOW) && getConfiguration().getBoolean("disable-snow-melting")){
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - BlockForm Event
     * Written by: Razorcane
     * 
     * Handles Ice/Snow Forming
     */
    @ModuleEvent(event=BlockFormEvent.class)
    public void onBlockForm(BukkitEventWrapper wrapper){
        BlockFormEvent event = (BlockFormEvent) wrapper.getEvent();
        Block b = event.getBlock();
        
        if(b.getType().equals(Material.ICE) && getConfiguration().getBoolean("disable-ice-formation")){
            event.setCancelled(true);
        }
        
        if(b.getType().equals(Material.SNOW) && getConfiguration().getBoolean("disable-snow-formation")){
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - BlockBurn Event
     * Written by: Razorcane
     * 
     * Handles Fire burning blocks
     */
    @ModuleEvent(event=BlockBurnEvent.class)
    public void onBlockBurn(BukkitEventWrapper wrapper){
        BlockBurnEvent event = (BlockBurnEvent) wrapper.getEvent();
        
        if(getConfiguration().getBoolean("disable-block-burning")){
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - BlockSpread Event
     * Written by: Razorcane
     * 
     * Handles Fire Spread.
     */
    @ModuleEvent(event=BlockSpreadEvent.class)
    public void onBlockSpread(BukkitEventWrapper wrapper){
        BlockSpreadEvent event = (BlockSpreadEvent) wrapper.getEvent();
        Block b = event.getBlock();
        
        if(b.getType().equals(Material.FIRE) && getConfiguration().getBoolean("disable-fire-spread")){
            event.setCancelled(true);
        }
    }
    
    /*
     * World Protection - EnchantItem Event
     * Written by: Razorcane
     * 
     * Handles Item Enchanting, obviously.
     */
    @ModuleEvent(event=EnchantItemEvent.class)
    public void onEnchantItem(BukkitEventWrapper wrapper){
        EnchantItemEvent event = (EnchantItemEvent) wrapper.getEvent();
        
        if(getConfiguration().getBoolean("disable-enchanting")){
            event.setCancelled(true);
        }
    }
}
