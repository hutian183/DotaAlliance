package com.dataturn.util;

public class StringUtil {
	/**
	 * 判断非空
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj){
		if(obj!=null&&!obj.equals(""))
			return true;
		else
			return false;
	}
	/**
	 * 合并连个byte数组
	 * @param b
	 * @param b1
	 * @param b2
	 */
	public static void bytesaddbytes(byte[] b,byte[] b1,byte[] b2){
		for (int i = 0; i < b1.length; i++) {
			b[i]=b1[i];
		}
		int num=b1.length;
		for (int i = 0; i < b2.length; i++) {
			b[i+num]=b2[i];
		}
	}
	public static void bytesaddbytes(byte[] b,byte[] b1,byte[] b2,int b2len){
		for (int i = 0; i < b1.length; i++) {
			b[i]=b1[i];
		}
		int num=b1.length;
		for (int i = 0; i < b2len; i++) {
			b[i+num]=b2[i];
		}
	}
	/**
	 * 截取byte数组
	 * @param data
	 * @param offset
	 * @param length
	 * @return
	 */
	public static byte[] getBytes(byte[] data, int offset, int length) {
		byte[] returnbyte=new byte[length];
		for (int i = 0; i < length; i++) {
			returnbyte[i]=data[offset+i];
		}
		return returnbyte;
	}
}
