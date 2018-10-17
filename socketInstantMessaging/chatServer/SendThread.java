package chatServer;

import java.net.Socket;
import java.util.ArrayList;
import java.io.*;;

public class SendThread extends Thread {
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	Socket socket;
	private String name;

	public SendThread(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}

	public void run() {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

			MessageDAO messageList = new MessageDAO();
			ArrayList<Message> myMessages;
			while (true) {
				System.out.println(name + "����Ϣ��������Ϣ��\n" + messageList.getall());
				// get message
				// 103@from@content@sendtime;103@from@content@sendtime
				myMessages = messageList.getMymessage(name);
				if (myMessages != null) {
					StringBuffer buffer = new StringBuffer();
					StringBuffer filebuffer = new StringBuffer();
					for (int i = 0; i < myMessages.size(); i++) {
						Message m = myMessages.get(i);
						if (m.getMessageType().equals("104")) {
							dos.writeUTF(m.getMessageType() + "@" + m.getFrom() + "@" + m.getContent() + "@"
									+ m.getSendTime() + ";");
							System.out.println("���������ļ��Ƿ������Ϣ");
							String string = dis.readUTF();
							System.out.println("�ѽ�����Ϣ");
							if (string.equals("OK")) {
								System.out.println("�������������߳�");
								new FileSendThread(StartServer.filess.accept(), m.getContent()).start();
							} else {
								// �û��ܾ������ļ������������ļ�ɾ��

								continue;
							}
							// filebuffer.append(m.getMessageType()+"@"+m.getFrom()+"@"+m.getContent()+"@"+m.getSendTime()+";");
						} else {
							buffer.append(m.getMessageType() + "@" + m.getFrom() + "@" + m.getContent() + "@"
									+ m.getSendTime() + ";");
						}

					}

					if (buffer.length() > 0) {
						// System.out.println("�������͵���Ϣ�ǣ�"+buffer);
						String strMessage = buffer.substring(0, buffer.length() - 1);
						dos.writeUTF(strMessage);
						dos.flush();
					}
					messageList.removeMyMessage(myMessages);

				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
