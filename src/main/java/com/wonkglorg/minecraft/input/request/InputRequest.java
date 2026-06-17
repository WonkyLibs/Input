package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.chat.exception.AttemptsExceeded;
import com.wonkglorg.minecraft.input.chat.exception.TimeExceeded;
import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import lombok.Getter;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public abstract class InputRequest<T, R extends InputRequest<T, ?>>{
	@Getter
	private final RequestType requestType;
	private boolean persistOnDisconnect = false;
	private Predicate<T> filter;
	private Consumer<T> onFailure;
	private Consumer<T> onRetry;
	private Consumer<T> onSuccess;
	@Getter
	protected final UUID playerUuid;
	private long startTime;
	
	@Getter
	private Duration timeout = Duration.ofSeconds(30);
	
	protected InputRequest(UUID playerUuid, RequestType type) {
		this.playerUuid = playerUuid;
		this.requestType = type;
	}
	
	public R timeout(Duration duration) {
		timeout = duration;
		return (R) this;
	}
	
	/**
	 * If the request should persist over the player disconnecting
	 */
	public R persistent(boolean value) {
		persistOnDisconnect = value;
		return (R) this;
	}
	
	public R filter(Predicate<T> filter) {
		this.filter = filter;
		return (R) this;
	}
	
	public R onFailure(Consumer<T> onFailure) {
		this.onFailure = onFailure;
		return (R) this;
	}
	
	public R onSuccess(Consumer<T> onSuccess) {
		this.onSuccess = onSuccess;
		return (R) this;
	}
	
	public R onRetry(Consumer<T> onRetry) {
		this.onRetry = onRetry;
		return (R) this;
	}
	
	public void request() {
	
	}
	
	public void cancel() {
		InputManager.getInstance().registerInputRequest();
	}
}
