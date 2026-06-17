package com.wonkglorg.minecraft.input.chat.parse;

public class InputParsers{
	public static final InputParser<String> STRING = s -> s;
	public static final InputParser<Integer> INT = Integer::parseInt;
	
}
