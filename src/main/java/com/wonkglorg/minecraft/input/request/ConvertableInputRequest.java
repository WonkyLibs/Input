package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class ConvertableInputRequest<T, E extends Event, R extends InputRequest<T, E, R>> extends InputRequest<T, E, R>{
	
	/**
	 * How many times to reprompt till valid
	 */
	@Getter
	private int maxAttempts = 1;
	
	private int usedAttempts = 0;
	
	protected InputParser<T> parser;
	protected Consumer<Player> onRetryFailure;
	
	protected ConvertableInputRequest(UUID playerUuid, RequestType type, InputParser<T> parser) {
		super(playerUuid, type);
		this.parser = parser;
	}
	
	public R maxAttempts(int attempts) {
		maxAttempts = attempts;
		return (R) this;
	}
	
	/**
	 * @param onRetryFailure when the input request fails due to exceeding the allowed retry attempts
	 */
	public R onRetryFailure(Consumer<Player> onRetryFailure) {
		this.onRetryFailure = onRetryFailure;
		return (R) this;
	}
	
	protected boolean incrementFailedAttemptsAndCheck(Player player) {
		usedAttempts++;
		if(usedAttempts >= maxAttempts){
			submitFailure(player);
		}
		return true;
	}
}
