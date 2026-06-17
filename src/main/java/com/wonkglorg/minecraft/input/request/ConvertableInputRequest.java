package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import lombok.Getter;

import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class ConvertableInputRequest<T, R extends InputRequest<T, ?>> extends InputRequest<T, R>{
	
	/**
	 * How many times to reprompt till valid
	 */
	@Getter
	private int maxAttempts = 1;
	
	private int usedAttempts = 0;
	
	private InputParser<T> parser;
	
	protected ConvertableInputRequest(UUID playerUuid, RequestType type, InputParser<T> parser) {
		super(playerUuid, type);
		this.parser = parser;
	}
	
	public R maxAttempts(int attempts) {
		maxAttempts = attempts;
		return (R) this;
	}
	
	public void incrementFailedAttempts() {
		usedAttempts++;
	}
	
}
