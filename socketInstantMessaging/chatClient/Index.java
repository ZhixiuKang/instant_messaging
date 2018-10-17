package chatClient;

import java.awt.Color;

public class Index { // 表情类
	private String Expressioncode; // 表情代码
	private int firstindex; // 插入位置
	private String text; // 当前剩下的输入字符

	public String getExpressioncode() {
		return Expressioncode;
	}

	public void setExpressioncode(String Expressioncode) {
		this.Expressioncode = Expressioncode;
	}

	public int getFirstindex() {
		return firstindex;
	}

	public void setFirstindex(int firstindex) {
		this.firstindex = firstindex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}