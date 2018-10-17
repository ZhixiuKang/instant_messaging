package chatClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class FileSendThread extends Thread {
	private File file;
	private Socket socket;

	public FileSendThread(File file, Socket socket) {
		this.file = file;
		this.socket = socket;
	}

	public void run() {
		try {
			FileInputStream fis = null;
			Socket datesocket = new Socket(socket.getInetAddress(), 5001);
			DataInputStream datedis = new DataInputStream(datesocket.getInputStream());
			DataOutputStream datedos = new DataOutputStream(datesocket.getOutputStream());
			String str = datedis.readUTF();
			System.out.println("���յ�" + str);
			if (str.equals("offline")) {
				fis = new FileInputStream(file);
				/* ��ʼ��ʽ��������: */
				byte[] buffer = new byte[20480];
				int num = 0; // ����һ�ζ�ȡ���ֽ���
				int count = 0;
				do {
					num = fis.read(buffer);
					if (num != (-1)) {
						// ���ͣ�

						count += num;
						datedos.write(buffer, 0, num);
						datedos.flush();
						System.out.println("�ѷ��ͣ�" + count);
					}
				} while (num != (-1));
				fis.close();
				datedos.close();
				datedis.close();
				datesocket.close();
				System.out.println(file.getName() + "�ļ��������");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
