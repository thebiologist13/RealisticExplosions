package com.github.thebiologist13.realisticexplosives;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Explosive;
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
		this.DEBRIS_CHANCE = plugin.config.getInt("chance", 50);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onExplode(EntityExplodeEvent ev) {
		List<Block> blocks = ev.blockList();
		Location center = ev.getLocation();
		float yield = 3.0f;
		
		if(ev.getEntity() instanceof Explosive) {
			Explosive e = (Explosive) ev.getEntity();
			yield = e.getYield();
		} else if (ev.getEntity() instanceof Creeper) {
			Creeper c = (Creeper) ev.getEntity();
			if(c.isPowered()) {
				yield = 6.0f;
			} else {
				yield = 3.0f;
			}
		}
		
		Iterator<Block> itr = blocks.iterator();
		while(itr.hasNext()) { //Iterates through blocks
			Block b = itr.next();
			if((Math.random() * 100.0F) <= DEBRIS_CHANCE) { //Randomly chooses the blocks based on chance
				
				if(PLUGIN.config.getIntegerList("exclude").contains(b.getTypeId()))
					continue;
				
				b.getDrops().clear(); //Remove the drops
				
				Location loc = b.getLocation();
				//Spawn the "debris"
				FallingBlock debris = loc.getWorld().spawnFallingBlock(loc, b.getType(), b.getData()); 
				Location d = debris.getLocation();
				Location relative = new Location(loc.getWorld(), d.getX() - center.getX(),
						d.getY() - center.getY(), d.getZ() - center.getZ());
				
				//Set the vector
				Vector vec = new Vector(yield / relative.getX(), 
						yield / relative.getY(), 
						yield / relative.getZ());
				
				vec.normalize();
				vec.multiply(d.distance(center) / 2.5F);
				
				debris.setVelocity(vec);
			}
		}
	}

}
