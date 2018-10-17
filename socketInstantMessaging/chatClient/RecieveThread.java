package chatClient;

import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.io.*;

public class RecieveThread extends Thread {
	private MainFrame main;
	private Socket s;

	public RecieveThread(MainFrame main, Socket s) {
		this.main = main;
		this.s = s;
	}

	public void run() {
		try {
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			Document doc = main.getChatbox().getDocument();
			while (true) {
				// 103@from@content@sendtime;103@from@content@sendtime
				String message = dis.readUTF();
				System.out.println("读到消息：" + message);
				String[] m1 = message.split(";");
				for (int i = 0; i < m1.length; i++) {
					String[] m2 = m1[i].split("@");
					if (m2[0].equals("102")) {
						String[] m3 = m2[2].split(",");
						main.getReceiver().removeAll();
						for (int j = 0; j < m3.length; j++) {
							main.getReceiver().addItem(m3[j]);
						}

					} else if (m2[0].equals("103")) {
						String str = m2[3] + "\r\n";
						doc.insertString(doc.getLength(), str, MainFrame.timeAttributeSet);
						str = m2[1] + " send you:" + m2[2] + "\r\n";
						new ConvertExpression().recievemessage(main, str);
					} else if (m2[0].equals("104")) {
						int op = JOptionPane.showConfirmDialog(null, "对方正在发送文件，确定接收吗?", "文件传输",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (op == JOptionPane.YES_OPTION) {
							dos.writeUTF("OK");
							dos.flush();
							new FileRecieveThread(s, m2[2], main).start();
						} else {
							dos.writeUTF("NO");
							dos.flush();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

}
