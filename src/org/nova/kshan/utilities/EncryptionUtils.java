package org.nova.kshan.utilities;

import java.util.Scanner;

/**
 * Made by: K-Shan
 */
public class EncryptionUtils {
	
	static char[][] transform = { 
		
		// Start of letters
		{ 'a', 'k' }, { 'b', 'e' }, { 'c', 'l' }, { 'd', 'a' }, { 'e', 'x' }, { 'f', 'c' }, { 'g', 't' }, 
		{ 'h', 'n' }, { 'i', 'm' }, { 'j', 'v' }, { 'k', 's' }, { 'l', 'r' }, { 'm', 'p' }, { 'n', 'b' },
		{ 'o', '0' }, { 'p', 'h' }, { 'q', 'f' }, { 'r', 'z' }, { 's', 'w' }, { 't', 'y' }, { 'u', 'd' },
		{ 'v', 'g' }, { 'w', 'i' }, { 'x', 'j' }, { 'y', 'q' }, { 'z', 'u' },
		
		// Start of capital letters
		{ 'A', 'K' }, { 'B', 'E' }, { 'C', 'L' }, { 'D', 'A' }, { 'E', 'X' }, { 'F', 'C' }, { 'G', 'T' }, 
		{ 'H', 'N' }, { 'I', 'M' }, { 'J', 'V' }, { 'K', 'S' }, { 'L', 'R' }, { 'M', 'P' }, { 'N', 'B' },
		{ 'O', '0' }, { 'P', 'H' }, { 'Q', 'F' }, { 'R', 'Z' }, { 'S', 'W' }, { 'T', 'Y' }, { 'U', 'D' },
		{ 'V', 'G' }, { 'W', 'I' }, { 'X', 'J' }, { 'Y', 'Q' }, { 'Z', 'U' },
		
		// Start of numbers
		{ '0', 'o' }, { '1', '8' }, { '2', '9' }, { '3', '1' }, { '4', '6' },
		{ '5', '2' }, { '6', '5' }, { '7', '3' }, { '8', '7' }, { '9', '4' }
 	};
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("Text to be encrypted: ");
		String text = s.nextLine();
		System.out.println(encrypt(text, true));
		System.out.print("Text to be decrypted: ");
		text = s.nextLine();
		System.out.println(encrypt(text, false));
		s.close();
	}
	
	public static String encrypt(String s, boolean encrypt) {
		String encrypted = "";
		for(int i = 0; i < s.length(); i++)
			encrypted += encryptedChar(s.charAt(i), encrypt);
		return encrypted;
	}
	
	public static char encryptedChar(char c, boolean encrypt) {
		for (char[] color : transform) {
			if (c == color[encrypt ? 0 : 1])
				return color[encrypt ? 1 : 0];
		}
		return 0;
	}
	

}
