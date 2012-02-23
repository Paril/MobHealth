package me.sablednah.MobHealth;

import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import cam.Likeaboss;
import cam.boss.Boss;
import cam.boss.BossManager;

import com.garbagemule.MobArena.Arena;
import com.garbagemule.MobArena.MobArenaHandler;
import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.api.*;



public class HeroesWeaponDamageEventListener implements Listener {
	public MobHealth plugin;
	
	public HeroesWeaponDamageEventListener(MobHealth instance) {
		this.plugin=instance;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void WeaponDamageEvent(WeaponDamageEvent event){

		if (!event.isCancelled()) {
			
			int targetHealth=0;
			
			if (MobHealth.debugMode) {
//				event.setDamage(200);
				System.out.print("----");
				System.out.print("Entity Damaged " + event.getEntity());
				System.out.print("Entity getEventName  " + event.getEventName());
				System.out.print("Entity Damage class  " + event.getClass());
				System.out.print("Entity Damage  " + event.getDamage());
				if (event.getEntity() instanceof ComplexLivingEntity) System.out.print("Entity Damaged is ComplexLivingEntity ");
			}

			
			Player playa = null;
			
			if(event instanceof WeaponDamageEvent) {
				WeaponDamageEvent damageEvent = event;

				if(damageEvent.getDamager() instanceof Projectile) {
					Projectile bullit = (Projectile) damageEvent.getDamager();
					if (bullit.getShooter() instanceof Player) {
						playa = (Player) bullit.getShooter();
					}
				}
				
				if (damageEvent.getDamager() instanceof Player) {
					playa = (Player) damageEvent.getDamager();
				}
				
				if (playa != null) {	
					if(MobHealth.getPluginState(playa)){	
						if((playa.hasPermission("mobhealth.show") && MobHealth.usePermissions ) || (!MobHealth.usePermissions) ) {

							LivingEntity targetMob = (LivingEntity) event.getEntity();
							
							targetHealth=targetMob.getHealth();

							if (MobHealth.hasLikeABoss) {
								Likeaboss LaB=(Likeaboss) plugin.getServer().getPluginManager().getPlugin("Likeaboss");
								BossManager BM=LaB.getBossManager();
								if(BM != null)  {
									Boss thisBoss = BM.getBoss(targetMob);
									if(thisBoss != null)  {
										targetHealth=thisBoss.getHealth();
									}
								}
							} else if (MobHealth.hasMobArena) {
								MobArenaHandler maHandler = new MobArenaHandler();
								Arena arena = maHandler.getArenaWithPlayer(playa);
								if (arena !=null) {
									if (arena.isBossWave()) {
										if (targetMob instanceof LivingEntity && maHandler.isMonsterInArena(targetMob)) {
											targetHealth=MobHealth.maBossHealthMax;
										} 
									}
								}
							} else { //I need a Hero!
								Heroes heroes = (Heroes) plugin.getServer().getPluginManager().getPlugin("Heroes");
								if(heroes != null)  {
									targetHealth=heroes.getDamageManager().getEntityHealth(targetMob);
								}
							}
							
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new MessageScheduler(playa, damageEvent, targetMob, targetHealth, event.getDamage(),plugin), 2L);

						} else {
							if (MobHealth.debugMode) {
								System.out.print("Not allowed - mobhealth.show is "+playa.hasPermission("mobhealth.show")+" - usePermissions set to "+MobHealth.usePermissions);
							}
						}
					}
				} 
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void SkillDamageEvent(SkillDamageEvent event){

		if (!event.isCancelled()) {
			
			int targetHealth=0;
			
			if (MobHealth.debugMode) {
//				event.setDamage(200);
				System.out.print("----");
				System.out.print("Entity Damaged " + event.getEntity());
				System.out.print("Entity getEventName  " + event.getEventName());
				System.out.print("Entity Damage class  " + event.getClass());
				System.out.print("Entity Damage  " + event.getDamage());
				if (event.getEntity() instanceof ComplexLivingEntity) System.out.print("Entity Damaged is ComplexLivingEntity ");
			}

			
			Player playa = null;
			
			if(event instanceof SkillDamageEvent) {
				SkillDamageEvent damageEvent = event;

				playa = (Player) damageEvent.getDamager().getPlayer();
				
				System.out.print("playa - " + playa);
				
				if (playa != null) {	
					if(MobHealth.getPluginState(playa)){	
						if((playa.hasPermission("mobhealth.show") && MobHealth.usePermissions ) || (!MobHealth.usePermissions) ) {

							LivingEntity targetMob = (LivingEntity) event.getEntity();
							
							targetHealth=targetMob.getHealth();

							Heroes heroes = (Heroes) plugin.getServer().getPluginManager().getPlugin("Heroes");
							if(heroes != null)  {
								targetHealth=heroes.getDamageManager().getEntityHealth(targetMob);
							}
							
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new SkillMessageScheduler(playa, damageEvent, targetMob, targetHealth, event.getDamage(),plugin), 1L);

						} else {
							if (MobHealth.debugMode) {
								System.out.print("Not allowed - mobhealth.show is "+playa.hasPermission("mobhealth.show")+" - usePermissions set to "+MobHealth.usePermissions);
							}
						}
					}
				} 
			}
		}
	}
	
	
}