package org.nova.cache.ce;

import javax.xml.bind.DatatypeConverter;

import org.nova.cache.Cache;
import org.nova.cache.utility.CacheUtils;

public class Tester {

//	public static void main(String[] args) {
//		Cache s = new Cache("data/cache/");
//		byte[] bytes = s.getIndices()[7].getFile(65967);
//		System.out.println(Arrays.toString(bytes));
//		String hex = bytesToHex(bytes);
//		System.out.println(hex);
//		byte[] bs = hexToBytes(hex);
//		System.out.println(Arrays.toString(bs));
//		String hx = bytesToHex(bs);
//		System.out.println(hx);
//	}
	
	public static void main(String[] args) {
		Cache s = new Cache("data/cache/");
		byte[] data = s.getIndices()[7].getFile(66009, 0);
		System.out.println(data == null);
		System.out.println(CacheUtils.getModelsSize(s));
		System.out.println(getTextureFromHex("0x63bc"));
		System.out.println(1 ^ 0x7d);
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static final String bytesToHex(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
	}
	
	/**
	 * 
	 * @param hex
	 * @return
	 */
	public static final byte[] hexToBytes(String hex) {
		return DatatypeConverter.parseHexBinary(hex);
	}
	
	/**
	 * 
	 * @param hexCode
	 * @return
	 */
	public static int getTextureFromHex(String hexCode) {
		return ((hexToDecimal(hexCode) >> 8) + (hexToDecimal(hexCode) < 100 ? 0 : - 1));
	}
	
	/**
	 * 
	 * @param hexCode
	 * @return
	 */
	public static int hexToDecimal(String hexCode) {
        String digits = "0123456789ABCDEF";
        hexCode = hexCode.toUpperCase();
        int val = 0;
        for (int i = 0; i < hexCode.length(); i++) {
            char c = hexCode.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }


    /**
     * decimal has to be non-negative
     * @param decimal
     * @return
     */
    public static String decimalToHex(int decimal) {
        String digits = "0123456789ABCDEF";
        if (decimal == 0) return "0";
        String hex = "";
        while (decimal > 0) {
            int digit = decimal % 16;                // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            decimal = decimal / 16;
        }
        return hex;
    }

}
