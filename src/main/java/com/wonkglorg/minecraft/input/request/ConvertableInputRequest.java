package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.InputRequest;
import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "unused"})
public abstract class ConvertableInputRequest<T, E extends Event, R extends InputRequest<T, E, R>> extends InputRequest<T, E, R>{
	
	protected InputParser<T> parser;
	
	protected Consumer<Player> parseFailure;
	
	protected ConvertableInputRequest(UUID playerUuid, RequestType type, InputParser<T> parser) {
		super(playerUuid, type);
		this.parser = parser;
	}
	
	protected void handleInput(Cancellable event, Player player, String input) {
		try{
			T value = parser.parse(input);
			if(!filter(player, value)){
				incrementFailedAttemptsAndCheck(player);
				submitFilterDeny(player, value);
				if(cancelEventOnWrongInput){
					event.setCancelled(true);
				}
				return;
			}
			
			submitSuccess(player, value);
			event.setCancelled(true);
		} catch(Exception ex){
			if(cancelEventOnWrongInput){
				event.setCancelled(true);
			}
			incrementFailedAttemptsAndCheck(player);
			submitParseFailure(player);
		}
	}
	
	/**
	 * @param parseFailure when the input request fails due to exceeding the expiry time
	 */
	public R onParseFailure(Consumer<Player> parseFailure) {
		this.parseFailure = parseFailure;
		return (R) this;
	}
	
	protected void submitParseFailure(Player player) {
		if(parseFailure != null){
			parseFailure.accept(player);
		}
	}
}
