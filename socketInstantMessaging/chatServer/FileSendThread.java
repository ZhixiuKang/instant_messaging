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
			System.out.println("�����������߳�");
			FileInputStream fis = null;
			DataInputStream datedis = new DataInputStream(datesocket.getInputStream());
			DataOutputStream datedos = new DataOutputStream(datesocket.getOutputStream());

			fis = new FileInputStream(filepath + (filepath.endsWith("\\") ? "" : "\\") + filename);
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
			System.out.println(filename + "�ӷ������ļ��������");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
