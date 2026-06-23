package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.InputRequest;
import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;
import java.util.function.BiConsumer;
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
	protected Consumer<Player> retryFailure;
	
	protected ConvertableInputRequest(UUID playerUuid, RequestType type, InputParser<T> parser) {
		super(playerUuid, type);
		this.parser = parser;
	}
	
	public R maxAttempts(int attempts) {
		maxAttempts = attempts;
		return (R) this;
	}
	
	/**
	 * @param onRetryFailure whenever the player enteres a wrong value and needs to retry their attempt, in case it was the last attempt bringing them over the attempt limit calls {@link InputRequest#onFailure(BiConsumer)} instead with the reason {@link FailureReason#ATTEMPT_EXCEEDED}
	 */
	public R onRetryFailure(Consumer<Player> onRetryFailure) {
		this.retryFailure = onRetryFailure;
		return (R) this;
	}
	
	protected void incrementFailedAttemptsAndCheck(Player player) {
		usedAttempts++;
		if(usedAttempts >= maxAttempts){
			submitFailure(FailureReason.ATTEMPT_EXCEEDED, player);
			return;
		}
		if(retryFailure != null){
			retryFailure.accept(player);
		}
	}
}
