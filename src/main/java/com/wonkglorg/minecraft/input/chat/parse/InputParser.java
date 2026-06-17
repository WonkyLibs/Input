package com.wonkglorg.minecraft.input.chat.parse;

@FunctionalInterface
public interface InputParser<T>{
	T parse(String input) throws Exception; //NOSONAR
}
