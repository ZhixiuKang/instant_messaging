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
			System.out.println("接收到" + str);
			if (str.equals("offline")) {
				fis = new FileInputStream(file);
				/* 开始正式发送数据: */
				byte[] buffer = new byte[20480];
				int num = 0; // 发送一次读取的字节数
				int count = 0;
				do {
					num = fis.read(buffer);
					if (num != (-1)) {
						// 发送：

						count += num;
						datedos.write(buffer, 0, num);
						datedos.flush();
						System.out.println("已发送：" + count);
					}
				} while (num != (-1));
				fis.close();
				datedos.close();
				datedis.close();
				datesocket.close();
				System.out.println(file.getName() + "文件发送完毕");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
