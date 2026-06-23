package com.wonkglorg.minecraft.input.request;

import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import com.wonkglorg.minecraft.input.request.type.ChatInputRequest;
import com.wonkglorg.minecraft.input.request.type.DropItemInputRequest;
import com.wonkglorg.minecraft.input.request.type.EntitySelectInputRequest;

import java.util.UUID;

public class Input{
	/**
	 * Builds a request that requires the player to drop an item as the input
	 *
	 * @param playerUuid the player to register the request for
	 */
	public static DropItemInputRequest itemDrop(UUID playerUuid) {
		return new DropItemInputRequest(playerUuid);
	}
	
	/**
	 * Builds a request that requires the player to type in chat as the input
	 *
	 * @param playerUuid the player to register the request for
	 * @param parser what format to parse input as
	 */
	public static <T> ChatInputRequest<T> chat(UUID playerUuid, InputParser<T> parser) {
		return new ChatInputRequest<>(playerUuid, parser);
	}
	
	/**
	 * Builds a request that requires the player to click an entity to select
	 *
	 * @param playerUuid the player to register the request for
	 */
	public static EntitySelectInputRequest entityClick(UUID playerUuid) {
		return new EntitySelectInputRequest(playerUuid);
	}
	
}
