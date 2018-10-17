package chatClient;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.omg.PortableInterceptor.INACTIVE;

public class ConvertExpression {

	public static ArrayList<Index> check(String input) {
		ArrayList<Index> indexs = new ArrayList<Index>();
		String character[] = { "[11]", "[1112]", "[22]", "[33]", "[343]", "[3454]", "[4343]", "[44]", "[4546r]",
				"[45g]", "[46435]", "[545634]", "[55]", "[5567g1]", "[56g56]", "[5g5]", "[5g5d]", "[6565]", "[65f]",
				"[gg]", "[hfe]", "[hgfr]", "[r4gr4]", "[rev]", "[rt4]" };
		int x = 0, y = 0;
		while (x != -1) {
			x = input.indexOf("[");
			y = input.indexOf("]");
			if (y != -1 && x != -1) {
				String bq1 = input.substring(x, y + 1);
				for (int i = 0; i < character.length; i++) {
					if (bq1.equals(character[i])) {
						Index ex = new Index();
						ex.setFirstindex(x);
						ex.setExpressioncode(bq1);
						StringBuffer input1 = new StringBuffer(input);
						input1.delete(x, y + 1);
						input = input1.toString();
						ex.setText(input);
						indexs.add(ex);
					}
				}
			}

		}
		return indexs;
	}

	// 带有表情的文本框转化成可以发送的字符串
	public static String sendmessage(ArrayList<Index> in, String text) {
		StringBuffer sb = new StringBuffer(text);
		int j = 0;
		for (int i = 0; i < in.size(); i++) {
			sb.insert(in.get(i).getFirstindex() + j - i, in.get(i).getExpressioncode());
			sb.delete(in.get(i).getFirstindex() + j - i + in.get(i).getExpressioncode().length(),
					in.get(i).getFirstindex() + j - i + in.get(i).getExpressioncode().length() + 1); // 删除多余的空格
			j = j + in.get(i).getExpressioncode().length();
		}
		text = sb.toString();
		return text;
	}
	// 将输入消息处理后显示到已发消息框中
	public void recievemessage(MainFrame frame, String text) { 
		try {
			ArrayList<Index> indexs = check(text);
			Document doc = frame.chatbox.getDocument();
			if (indexs.size() == 0) {
				doc.insertString(doc.getLength(), text, MainFrame.textAttributeSet);
			} else {
				int existstring = doc.getLength();
				doc.insertString(doc.getLength(), indexs.get(indexs.size() - 1).getText(), MainFrame.textAttributeSet);
				for (int i = indexs.size() - 1; i >= 0; i--) {
					frame.chatbox.setCaretPosition(existstring + indexs.get(i).getFirstindex());
					frame.chatbox.insertIcon(new ImageIcon(
							getClass().getResource("/image/" + indexs.get(i).getExpressioncode() + ".gif")));
				}
			}

		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

}
