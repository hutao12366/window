package test;

import hu.MyFile;
import hu.Text;

public class Test12
{
	public static void main(String[] args)
	{

		char[] char1 = Text.readChar("D:\\JAVA\\Window2\\src\\隐藏的红玫瑰1.txt");
		int[] is = MyFile.seekChar(char1, "为自己的眼睛迷".toCharArray(), 0);


		assert is != null;
		String txt = Text.interceptTxt(char1, is);
		System.out.println(txt);

	}

}
