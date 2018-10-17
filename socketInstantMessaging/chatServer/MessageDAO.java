package chatServer;

import java.util.ArrayList;

public class MessageDAO {
	/*
	 * ע��һ����final��ֻ��ʼ��һ�Σ��������ÿ�ζ���ʼ���������е�MessageDAO����� messageList�ڳ�ʼ״̬���ǿ�
	 */

	private static final ArrayList<Message> messageList = new ArrayList<Message>();

	public MessageDAO() {
	}

	public ArrayList<Message> getMessageList() {
		return messageList;
	}

	public void removeMyMessage(ArrayList<Message> myMessage) {
		for (Message message : myMessage) {
			messageList.remove(message);
		}
	}

	public ArrayList<Message> getMymessage(String string) {
		ArrayList<Message> usermessage = new ArrayList<Message>();
		for (Message message : messageList) {
			if (string.equals(message.getTo())) {
				usermessage.add(message);
			}
		}
		return usermessage;
	}

	public String getall() {
		String temp = "";
		for (Message message : messageList) {
			temp += message.getMessageType() + "|" + "From:" + message.getFrom() + "To:" + message.getTo() + "Content:"
					+ message.getContent() + "\n";
		}
		return temp;
	}
}
