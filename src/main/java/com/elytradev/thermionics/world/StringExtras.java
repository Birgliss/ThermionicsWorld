package com.elytradev.thermionics.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class StringExtras {
	public static List<String> wrapForTooltips(String str) {
		return wordWrap(str, 35);
	}
	
	public static void addInformation(String key, List<String> tooltip) {
		addInformation(key, "", tooltip);
	}
	
	public static void addInformation(String key, String formatCodes, List<String> tooltip) {
		String localized = I18n.translateToLocal(key);
		List<String> wrapped = wrapForTooltips(localized);
		for(String s : wrapped) tooltip.add(formatCodes+s);
	}
	
	/**
	 * Adds text pre-split with '/', such as poems with metered lines
	 */
	public static void addSplitInformation(String key, String formatCodes, List<String> tooltip) {
		String localized = I18n.translateToLocal(key);
		for(String s : localized.split("/")) {
			tooltip.add(formatCodes + s.trim());
		}
	}
	
	private static List<String> wordWrap(String str, int width) {
		List<String> result = new ArrayList<>();
		String[] words = str.split(" ");

		String line = "";
		for (String word : words) {
			
			if (line.length() + word.length() > width) {
				//If the current line won't fit with the new word appended, emit it and try the word on its own
				if (!line.isEmpty()) {
					result.add(line.trim());
					line = "";
				}
				
				// If the current word is too long to fit even on its own, split it up
				while (word.length() > width) {
					String subword = word.substring(0, width - 1) + "-";
					result.add(subword.trim());
					word = word.substring(width - 1);
					line = ""; //unnecessary but you never know
				}
			}
			line += " "+word;
		}
		if (!line.isEmpty()) result.add(line.trim());
		
		return result;
	}
	
	public static List<String> wordWrap(String str, int width, Function<String, Integer> widthFunction) {
		List<String> result = new ArrayList<>();
		String[] words = str.split(" ");

		String line = "";
		for (String word : words) {
			if (widthFunction.apply((line+" "+word).trim()) > width) {
				//If the current line won't fit with the new word appended, emit the line so the new word sits at the start of the next line
				if (!line.isEmpty()) {
					result.add(line.trim());
					line = "";
				}
			}
			line += (" "+word).trim(); //Add it even if it won't fit by itself
		}
		if (!line.isEmpty()) result.add(line.trim());
		
		return result;
	}
}
