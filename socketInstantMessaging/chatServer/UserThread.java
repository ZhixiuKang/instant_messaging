package chatServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

//import org.apache.commons.lang3.ObjectUtils.Null;

import java.io.*;

public class UserThread extends Thread {
	private Socket s = null;
	private String name;

	public UserThread(Socket socket) {
		this.s = socket;
	}

	public void run() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			String message;
			MessageDAO messageList = new MessageDAO();
			// ServerSocket ss=new ServerSocket(5001);
			// make a commucation protocol between sever and client
			// 101|name,pass -- login to sever
			// 102| -- ask sever for user list
			// 103|from,to.message -- talk to other user
			while (true) {
				message = dis.readUTF();
				if (message.startsWith("101")) {
					String content = message.substring(message.indexOf('|') + 1);
					name = content.split(",")[0];
					String pass = content.split(",")[1];
					boolean flag = UserinfoDAO.validateUser(name, pass);
					boolean online = UserinfoDAO.getonline(name);
					if (flag) {
						dos.writeUTF("success");
						dos.flush();
						new SendThread(s, name).start();
					} else if (!flag && online) {
						dos.writeUTF("online");
						dos.flush();
					} else {
						dos.writeUTF("failure");
						dos.flush();
					}
				} else if (message.startsWith("102")) {
					ArrayList<Userinfo> userList = UserinfoDAO.getUserlist();
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < userList.size(); i++) {
						Userinfo u = userList.get(i);
						buffer.append(u.getName() + ",");
					}
					String userstr = buffer.substring(0, buffer.length() - 1);
					Message m = new Message();
					m.setFrom("Server");
					m.setTo(name);
					m.setContent(userstr);
					m.setMessageType("102");
					m.setSendTime(new Date());
					// save massage
					messageList.getMessageList().add(m);
					// System.out.println("请求用户列表消息已经放入消息队列");
				} else if (message.startsWith("103")) {
					String content = message.substring(message.indexOf('|') + 1);
					Message m = new Message();
					m.setFrom(content.split(",")[0]);
					m.setTo(content.split(",")[1]);
					m.setContent(content.split(",")[2]);
					m.setSendTime(new Date());
					;
					m.setMessageType("103");
					messageList.getMessageList().add(m);
					// System.out.println("聊天消息已经放入消息队列");
				} else if (message.startsWith("104")) {
					String content = message.substring(message.indexOf('|') + 1);
					Message m = new Message();
					m.setFrom(content.split(",")[0]);
					m.setTo(content.split(",")[1]);
					m.setContent(content.split(",")[2]);
					m.setSendTime(new Date());
					;
					m.setMessageType("104");
					messageList.getMessageList().add(m);
					new FileRecieveThread(StartServer.filess.accept(), content.split(",")[2]).start();

					/*
					 * if (!UserinfoDAO.getonline(content.split(",")[1])) {
					 * dos.writeUTF("offline"); dos.flush(); }
					 */

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dis.close();
				dos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
