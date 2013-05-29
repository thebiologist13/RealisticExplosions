package com.github.thebiologist13.realisticexplosives;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class ExplosionListener implements Listener {

	private final RealisticExplosives PLUGIN;
	private final int DEBRIS_CHANCE;
	
	public ExplosionListener(RealisticExplosives plugin) {
		this.PLUGIN = plugin;
		this.DEBRIS_CHANCE = plugin.getBetterConfig().getConfig().getInt("chance", 50);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onExplode(EntityExplodeEvent ev) {
		List<Block> blocks = ev.blockList();
		Location center = ev.getLocation();
		float yield = ev.getYield();
		
		Iterator<Block> itr = blocks.iterator();
		while(itr.hasNext()) { //Iterates through blocks
			Block b = itr.next();
			if((Math.random() * 100.0F) <= DEBRIS_CHANCE) { //Randomly chooses the blocks based on chance
				b.getDrops().clear(); //Remove the drops
				
				Location loc = b.getLocation();
				//Spawn the "debris"
				FallingBlock debris = loc.getWorld().spawnFallingBlock(loc, b.getType(), b.getData()); 
				Location relative = debris.getLocation(center);
				
				//Set the vector
				Vector vec = new Vector(yield / relative.getX(), yield / relative.getY(), 
						yield / relative.getZ());
				
				//Debug
				PLUGIN.getBetterLogger().printDebugMessage("Vector X: " + vec.getX());
				PLUGIN.getBetterLogger().printDebugMessage("Vector Y: " + vec.getY());
				PLUGIN.getBetterLogger().printDebugMessage("Vector Z: " + vec.getZ());
				
				debris.setVelocity(vec);
			}
		}
	}

}
