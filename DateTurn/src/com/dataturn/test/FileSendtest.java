package com.dataturn.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.dataturn.bean.Message;
import com.dataturn.constucts.UDPconstucts;
import com.dataturn.util.MD5Util;
import com.dataturn.util.StringUtil;
import com.dataturn.util.UDPUtil;
import com.dataturn.util.XmlUtil;

public class FileSendtest {
	/**
	 * 每次传输大小
	 */
	public static int transize=20*1024;
	
	public static void main(String[] args) throws Exception {
		File file=new File("D:/jdk-8u25-windows-x64 - 副本 (2).rar");
		rec();
		send(file);
	}
	
	public static void send(final File file) throws Exception{
		final DatagramSocket server=new DatagramSocket(2016);
		Thread.sleep(1000);
		new Thread(){
			public void run() {
			byte[] buf=new byte[1024];
			DatagramPacket packet=new DatagramPacket(buf, buf.length);
			Message m=new Message();
			m.setMethod(UDPconstucts.METHOD_FILE);
			m.setToType(file.getName());
			//服务器地址
			SocketAddress target = new InetSocketAddress("172.22.141.148", 2015);
			UDPUtil.send(XmlUtil.BeanToXml(m),target, server);
			
				Message message= UDPUtil.receive(server, packet);
				System.out.println("send:"+message.toString());
				server.close();
				if(message.getMethod().equals(UDPconstucts.METHOD_FILE_PORT)){
					String port=message.getPort();
					System.out.println("send:开始发送文件。。。向"+packet.getAddress()+":"+port);
					//开始执行接收
					new Thread(new Send(port,file,packet.getAddress())).start();
				}
			}}.start();
		
	}
	
	public static void rec() throws SocketException{
		final DatagramSocket server=new DatagramSocket(2015);
		
		new Thread(){
		public void run() {
			byte[] buf=new byte[1024];
			DatagramPacket packet=new DatagramPacket(buf, buf.length);
			//while(true){
				Message message= UDPUtil.receive(server, packet);
				System.out.println("rec:"+message.toString());
				if(message.getMethod().equals(UDPconstucts.METHOD_FILE)){
					String port="10002";
					System.out.println("rec:开始接收文件。。。从"+message.getFrom()+":"+packet.getPort());
					Message m2=new Message();
					m2.setMethod(UDPconstucts.METHOD_FILE_PORT);
					m2.setPort(port);
					File file=new File("D:/迅雷下载/"+message.getToType());
					//开始执行接收
					new Thread(new Recevice(port,file)).start();
					UDPUtil.send(XmlUtil.BeanToXml(m2), packet.getPort(), packet.getAddress(), server);
				}
			//}
		}}.start();
	}

}
class Recevice implements Runnable{
	private int size=0;
	private String port;
	private File file; 
	public Recevice(String port,File file) {
		this.port=port;
		this.file=file;
	}
	@Override
	public void run() {
		try {
		DatagramSocket server=new DatagramSocket(Integer.parseInt(port));
		//接收的数据包
		byte[] buf=new byte[FileSendtest.transize+32];
		DatagramPacket packet=new DatagramPacket(buf, buf.length);
		FileOutputStream fos=new FileOutputStream(file);
		//返回码
		String returnmsg="";
		String remotMd5="";
		String nowMd5="";
		while(true){
			//Thread.sleep(1);
			server.receive(packet);
			String receiveMessage = new String(packet.getData(), 0, 4);
			if("over".equals(receiveMessage)){
				System.out.println("over");
				fos.close();
				break;
			}
			//进行编码校验用MD5校验
			remotMd5=new String(packet.getData(),0,32);
			nowMd5=MD5Util.getMD5String(StringUtil.getBytes(packet.getData(),32,packet.getLength()-32));
			//比对
			if(MD5Util.checkPassword(remotMd5, nowMd5)){
				returnmsg=UDPconstucts.BACK_OK;
			}else{
				returnmsg=UDPconstucts.BACK_ERROR;
			}
			//确认收到信息
			UDPUtil.send(returnmsg, packet.getPort(),packet.getAddress(), server);
			
			//size=size+packet.getLength();
			//System.out.println("recevice:"+size);
			fos.write(packet.getData(), 32, packet.getLength()-32);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

class Send implements Runnable{
	private int size=0;
	private String port;
	private File file;
	private InetAddress address;
	

	public Send(String port, File file,InetAddress address) {
		super();
		this.port = port;
		this.file = file;
		this.address=address;
	}


	@Override
	public void run() {
		 DatagramSocket server=null;
		try {
			server = new DatagramSocket(2016 );
		} catch (Exception e) {
			e.printStackTrace();
		} 
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] b=new byte[FileSendtest.transize];
		byte[] data=new byte[FileSendtest.transize+32];
		//back数据包
		byte[] b2=new byte[1024];
		DatagramPacket dp= new DatagramPacket(b2, b2.length);
		//发送时间用于延迟测试
		//long sendTime=0;
		try {
			int len=fis.read(b);
			String md5="";
			while(len>0){
				//进行编码产生消息头
				 md5=MD5Util.getMD5String(StringUtil.getBytes(b, 0, len));
				 //组装消息
				StringUtil.bytesaddbytes(data, md5.getBytes(), b);
				//发送
				//sendTime=System.currentTimeMillis();
				UDPUtil.send(data, len+32,Integer.parseInt(port) , address, server);
				server.receive(dp);
				//收到正确信息时才发送下个信息
				if(!UDPconstucts.BACK_OK.equals(UDPUtil.toString(dp))){
					continue;
				}
				//统计
				len=fis.read(b);
				size=size+len;
				//System.out.println(100.*size/file.length()+"%");
			}
			UDPUtil.send("over", Integer.parseInt(port) , address, server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
