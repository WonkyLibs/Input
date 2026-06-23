package com.wonkglorg.minecraft.input;

import com.wonkglorg.minecraft.input.request.FailureReason;
import com.wonkglorg.minecraft.input.request.RequestType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.time.Duration;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "unused"})
public abstract class InputRequest<T, E extends Event, R extends InputRequest<T, E, ?>>{
	@Getter
	private final RequestType requestType;
	@Getter
	private boolean persistOnDisconnect = false;
	protected boolean cancelEventOnWrongInput = true;
	@Getter
	protected final UUID playerUuid;
	private BukkitTask timeoutTask;
	
	@Getter
	protected Duration timeoutDuration = Duration.ofSeconds(30);
	protected BiPredicate<Player, T> filter;
	protected BiConsumer<FailureReason, Player> failure;
	protected BiConsumer<Player, T> success;
	protected BiConsumer<Player, T> filterDeny;
	protected Consumer<Player> parseFailure;
	
	protected InputRequest(UUID playerUuid, RequestType type) {
		this.playerUuid = playerUuid;
		this.requestType = type;
	}
	
	public R timeout(Duration duration) {
		timeoutDuration = duration;
		return (R) this;
	}
	
	/**
	 * If the request should persist over the player disconnecting
	 */
	public R persistent(boolean value) {
		persistOnDisconnect = value;
		return (R) this;
	}
	
	/**
	 * If the event used to read this input should be cancelled if an invalid choice has been made either by the parser failing or by the filter denying it
	 */
	public R cancelEventOnWrongInput(boolean value) {
		cancelEventOnWrongInput = value;
		return (R) this;
	}
	
	public R filter(BiPredicate<Player, T> filter) {
		this.filter = filter;
		return (R) this;
	}
	
	/**
	 * @param parseFailure when the input request fails due to exceeding the expiry time
	 */
	public R onParseFailure(Consumer<Player> parseFailure) {
		this.parseFailure = parseFailure;
		return (R) this;
	}
	
	public R onSuccess(BiConsumer<Player, T> success) {
		this.success = success;
		return (R) this;
	}
	
	/**
	 * Executed when the request fails and is unable to return a result, this gets called no matter how the request has been stopped
	 *
	 * @param failure
	 * @return
	 */
	public R onFailure(BiConsumer<FailureReason, Player> failure) {
		this.failure = failure;
		return (R) this;
	}
	
	/**
	 * @param onFilterDeny if the filter denies the parsed value
	 */
	public R onFilterDeny(BiConsumer<Player, T> onFilterDeny) {
		this.filterDeny = onFilterDeny;
		return (R) this;
	}
	
	public void request() {
		if(timeoutDuration != null){
			Plugin plugin = InputManager.getInstance().plugin();
			timeoutTask = Bukkit.getServer().getScheduler().runTaskLater(plugin,
					() -> submitFailure(FailureReason.TIMEOUT, Bukkit.getPlayer(playerUuid)),
					timeoutDuration.toSeconds() * 20);
		}
		InputManager.getInstance().registerInputRequest(this);
	}
	
	/**
	 * Cancels the current timeout task
	 */
	void cancelTimeoutTask() {
		if(timeoutTask != null && !timeoutTask.isCancelled()){
			timeoutTask.cancel();
		}
	}
	
	void submitDisconnect(Player player) {
		submitFailure(FailureReason.PLAYER_DISCONNECT, player);
	}
	
	void submitForceCancel() {
		submitFailure(FailureReason.FORCE_CANCELLED, getPlayer());
	}
	
	/**
	 * Do not call this manually! this gets called by the library itself when handling inputs
	 */
	@Internal
	protected abstract void handleEvent(E event);
	
	protected boolean filter(Player player, T value) {
		if(filter == null){
			return true;
		}
		return filter.test(player, value);
	}
	
	protected void submitParseFailure(Player player) {
		if(parseFailure != null){
			parseFailure.accept(player);
		}
	}
	
	protected void submitFilterDeny(Player player, T value) {
		if(filterDeny != null){
			filterDeny.accept(player, value);
		}
	}
	
	/**
	 *
	 * @param reason the reason for the failure
	 * @param player
	 */
	protected void submitFailure(FailureReason reason, Player player) {
		if(failure != null){
			failure.accept(reason, player);
		}
		InputManager.getInstance().unRegisterInputRequest(this);
	}
	
	protected void submitSuccess(Player player, T value) {
		if(success != null){
			success.accept(player, value);
		}
		InputManager.getInstance().unRegisterInputRequest(this);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(playerUuid);
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(playerUuid);
	}
}
