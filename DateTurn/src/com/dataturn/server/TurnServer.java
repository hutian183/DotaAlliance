package com.dataturn.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.dataturn.bean.Message;
import com.dataturn.constucts.UDPconstucts;
import com.dataturn.util.UDPUtil;

public class TurnServer implements Runnable {
	private Integer port;
	private Integer size;
	public TurnServer(Integer port ,Integer size) {
		this.port=port;
		this.size=size;
	}
	
	public static void main(String[] args) {
		new Thread(new TurnServer(2015, 1024)).start();
	}

	@Override
	public void run() {
		DatagramSocket server=null;
		try {
			server = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[size];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		for (;;) {
			Message message=UDPUtil.receive(server, packet);
			exe(message,server);
		}
	}

	private void exe(Message message, DatagramSocket server) {
		//p2p方法
		if(message.getMethod()!=null&&message.getMethod().equals(UDPconstucts.METHOD_P2P)){
			if(message.getToType()!=null&message.getToType().equals(UDPconstucts.OBJECT_TYPE_DEVICE)){
				
			}
		}else if(UDPconstucts.METHOD_MYADDRESS.equals(message.getMethod())){
			Message msg=new Message();
			
		}
	}
	
}

class P2PThread implements Runnable{
	@Override
	public void run() {
		
	}
}
