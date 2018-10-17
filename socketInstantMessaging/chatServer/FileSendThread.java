package chatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class FileSendThread extends Thread {
	private String filepath = "F:\\";
	private Socket datesocket = null;
	private String filename = null;

	public FileSendThread(Socket socket, String filename) {
		this.filename = filename;
		this.datesocket = socket;
	}

	public void run() {
		try {
			System.out.println("已启动发送线程");
			FileInputStream fis = null;
			DataInputStream datedis = new DataInputStream(datesocket.getInputStream());
			DataOutputStream datedos = new DataOutputStream(datesocket.getOutputStream());

			fis = new FileInputStream(filepath + (filepath.endsWith("\\") ? "" : "\\") + filename);
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
			System.out.println(filename + "从服务器文件发送完毕");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
