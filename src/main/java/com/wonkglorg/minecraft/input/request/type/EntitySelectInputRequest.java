package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.InputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntitySelectInputRequest extends InputRequest<Entity, EntityDamageByEntityEvent, EntitySelectInputRequest>{
	public EntitySelectInputRequest(UUID playerUuid) {
		super(playerUuid, RequestType.DROP_ITEM);
	}
	
	@Override
	public void handleEvent(EntityDamageByEntityEvent e) {
		var item = e.getEntity();
		Player player = (Player) e.getDamager();
		handleInput(e, player, item);
	}
}
