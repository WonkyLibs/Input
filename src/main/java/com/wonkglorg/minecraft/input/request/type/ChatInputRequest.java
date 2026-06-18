package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import com.wonkglorg.minecraft.input.request.ConvertableInputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatInputRequest<T> extends ConvertableInputRequest<T, AsyncChatEvent, ChatInputRequest<T>>{
	
	public ChatInputRequest(UUID playerUuid, InputParser<T> parser) {
		super(playerUuid, RequestType.CHAT, parser);
	}
	
	@Override
	public void handleEvent(AsyncChatEvent e) {
		String message = PlainTextComponentSerializer.plainText().serialize(e.message());
		Player player = e.getPlayer();
		try{
			T value = parser.parse(message);
			
			if(!filter(player, value)){
				incrementFailedAttemptsAndCheck(player);
				submitFilterDeny(player, value);
				if(cancelEventOnWrongInput){
					e.setCancelled(true);
				}
				return;
			}
			
			submitSuccess(player, value);
			e.setCancelled(true);
		} catch(Exception ex){
			if(cancelEventOnWrongInput){
				e.setCancelled(true);
			}
			incrementFailedAttemptsAndCheck(player);
			submitParseFailure(player);
		}
	}
}
