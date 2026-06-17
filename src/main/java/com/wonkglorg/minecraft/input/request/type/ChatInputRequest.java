package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.chat.parse.InputParser;
import com.wonkglorg.minecraft.input.request.ConvertableInputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;

import java.util.UUID;

public class ChatInputRequest<T> extends ConvertableInputRequest<T, ChatInputRequest<T>>{
	
	public ChatInputRequest(UUID playerUuid, InputParser<T> parser) {
		super(playerUuid, RequestType.CHAT, parser);
	}
	
	@Override
	protected void registerRequest() {
		InputManager.getInstance().registerChatInputRequest(this);
	}
	
	@Override
	protected void unRegisterRequest() {
		InputManager.getInstance().consumeOutstandingChatRequest(playerUuid);
	}
	
	public void parse(String input) throws Exception {
		complete(parser.parse(input));
		unRegisterRequest();
	}
	
}
