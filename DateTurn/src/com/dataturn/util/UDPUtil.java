package com.dataturn.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.dataturn.bean.Message;

public class UDPUtil {
	public static Message receive(Integer port){
		try {
			DatagramSocket server = new DatagramSocket(port);
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			return receive(server, packet);
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Message receive(DatagramSocket server,DatagramPacket packet){
		Message message= null;
		try {
			server.receive(packet);
			String receiveMessage = new String(packet.getData(), 0, packet.getLength());
			message= XmlUtil.XmlToBean(receiveMessage, Message.class);
			message.setFrom(packet.getAddress().getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	
	public static void send(String sendMessageA, int port, InetAddress address, DatagramSocket server) {
		try {
			byte[] sendBuf = sendMessageA.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, port);
			server.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void send(byte[] sendBuf,int len, int port, InetAddress address, DatagramSocket server) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, len, address, port);
			server.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void send(String sendMessageA, SocketAddress address, DatagramSocket server) {
		try {
			byte[] sendBuf = sendMessageA.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address);
			server.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String toString(DatagramPacket packet) {
		String receiveMessage = new String(packet.getData(), 0, packet.getLength());
		return receiveMessage;
	}
}
