package chatServer;

import java.io.*;

import java.net.Socket;

public class FileRecieveThread extends Thread {
	private String filepath = "F:\\";
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private String filename;
	private FileOutputStream fos = null;

	public FileRecieveThread(Socket socket, String filename) {
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.filename = filename;
	}

	public void run() {
		try {
			dos.writeUTF("offline");
			dos.flush();
			fos = new FileOutputStream(filepath + (filepath.endsWith("\\") ? "" : "\\") + filename);
			byte[] buffer = new byte[20480]; // ���ջ��� 20k
			int num = 0; // ����һ�ζ�ȡ���ֽ���
			int count = 0;
			do {
				num = dis.read(buffer);
				if (num != (-1)) {
					// ���գ�
					count += num;
					fos.write(buffer, 0, num);
					fos.flush();
				}
			} while (num != (-1));
			fos.close();
			dos.close();
			dis.close();
			System.out.println("�������ѳɹ������ļ���" + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
