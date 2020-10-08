package hu;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MyFile
{

    public static Element[] textToElement(File file)
    {
        if (!file.exists())
        {
            return null;
        }
        List<Element> list = new ArrayList<>();
        BufferedReader reader = null;
        CharBuffer all;
        try
        {
            all = CharBuffer.allocate(15000000);
            reader = new BufferedReader(new FileReader(file));
            int read = reader.read(all);
            int o1 = 0;
            int is = 0;
            int on = 0;
            String a = "";
            String a1 = "";
            for (int i = 0; i < read; i++)
            {
                char c = all.get(i);
                if (c == '第')
                {
                    o1 = i;
                }
                if (c == '章')
                {
                    if ((i - o1) < 10)
                    {
                        for (int f = o1; f <= i; f++)
                        {
                            a += all.get(f);
                        }
                        if (is == 0)
                        {
                            a1 = a;
                        }
                        if (is == 1)
                        {
                            //                            PC:
                            //                            {
                            // System.out.println(a1 + ": offset= " + on + " length= " + (o1 - on));
                            list.add(toElement(a1, on, o1 - on));
                            // }
                            a1 = a;
                            on = o1;
                        }
                        is = 1;
                        a = "";
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            Close.close(reader);
        }

        return list.toArray(new Element[]{});
    }

    private static Element toElement(String name, int offset, int length)
    {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(name);
        root.addElement("offset").setText(offset + "");
        root.addElement("length").setText(length + "");
        return root;
    }

    /**
     * 查找 text中连续的value的位置
     *
     * @param text
     * @param value
     * @param i     从 i处开始查找
     * @return
     */
    public static int[] seekChar(String text, String value, int i)
    {
        char[] array = text.toCharArray();
        char[] values = value.toCharArray();
        return seekChar(array, values, i);
    }

    /**
     * 查找array中连续的value的值
     *
     * @param array
     * @param value
     * @param i     从 i处开始查找
     * @return
     */
    public static int[] seekChar(char[] array, char[] value, int i)
    {
        if ((array.length - i) < value.length)
        {
            return null;
        }
        int[] record = new int[value.length];
        int r = 0;
        for (; i < array.length && r < value.length; i++)
        {
            if (array[i] == value[r])
            {
                record[r] = i;
                r++;
            }
            else
            {
                if (array[i] != '\n')
                {
                    r = 0;
                }
            }


        }
        if (r == value.length)
        {
            return record;
        }
        else
        {
            return null;
        }
    }

    public static int[][] seekChars(char[] array, char[] value, int i)
    {
        List<int[]> list = new ArrayList<>();
        int[] a;
        while ((a = seekChar(array, value, i)) != null)
        {
            list.add(a);
            i = a[a.length - 1] + 1;
        }
        return list.toArray(new int[][]{});
    }

    /**
     * @param array  用于装替换后的文本 它将替换在它之前定义的数组
     * @param text   提供需要的文本
     * @param texts  需要插入的内容集合
     * @param muster 插入文本的位置集合
     */
    public static void replace(char[] array, char[] text, String[] texts, int[][] muster)
    {
        int a = 0;
        int o = -1;
        int[] incs;
        char[] value;
        if (texts != null || text.length > 0 || muster.length > 0)
        {

            for (int s = 0, y = 0; y < muster.length; y++, s++)
            {
                incs = muster[y];
                if (incs[incs.length - 1] >= text.length)
                {
                    break;
                }
                if (incs[0] <= o)
                {
                    continue;
                }
                assert texts != null;
                if (s == texts.length)
                {
                    s = 0;
                }
                value = texts[s].toCharArray();
                System.arraycopy(text, o + 1, array, a, incs[0] - o - 1);
                a += incs[0] - o - 1;
                o = incs[incs.length - 1];
                System.arraycopy(value, 0, array, a, value.length);
                a += value.length;
            }
        }
        System.arraycopy(text, o + 1, array, a, text.length - (o + 1));
    }

    //	/**
    //	 * 将text中的chars替换成chars1 注：在使用此函数前应该定义一个char[] 来存储替换完的数据
    //	 *
    //	 * @param array  替换完的数据会存到 array数组
    //	 * @param text
    //	 * @param chars
    //	 * @param chars1
    //	 * @return 替换的个数
    //	 */
    //	private static int replace(char[] array, char[] text, char[] chars, char[] chars1) {
    //		int o2 = 0;
    //		int sc = 0;
    //		for (int i = 0, s = 0;;) {
    //			int[] ints = MyFile.seekChar(text, chars, i);
    //			if (ints == null) {
    //				System.arraycopy(text, o2 + 1, array, s, text.length - (o2 + 1));
    //				break;
    //			}
    //			System.arraycopy(text, i, array, s, (ints[0]) - i);
    //			o2 = ints[ints.length - 1];
    //			s += (ints[0]) - i;
    //			System.arraycopy(chars1, 0, array, s, chars1.length);
    //			s += chars1.length;
    //			i = ints[ints.length - 1] + 1;
    //			sc++;
    //		}
    //		array = new String(array).trim().toCharArray();
    //		return sc;
    //	}
    //
    //	/**
    //	 * 将text中的chars替换成chars1
    //	 *
    //	 * @param text
    //	 * @param chars
    //	 * @param chars1
    //	 * @return
    //	 */
    //	public static char[] replace(char[] text, char[] chars, char[] chars1) {
    //		char[] array = new char[15000000];
    //		replace(array, text, chars, chars1);
    //		return array;
    //	}
    //
    //	/**
    //	 * 将text中的chars替换成chars1
    //	 *
    //	 * @param text
    //	 * @param chars
    //	 * @param chars1
    //	 * @return
    //	 */
    //	public static char[] replace(String text, String chars, String chars1) {
    //		return replace(text.toCharArray(), chars.toCharArray(), chars1.toCharArray());
    //	}

    /**
     * 在array中查找content中的char[] ，char[0]的第一个元素与char[length-1]的最后一个元素的间距不会超过gap
     *
     * @param array   需要提供的文本文件
     * @param content 要查找的char[]集合
     * @param i       从offset开始查找
     * @param gap     查找的最大间距
     * @return 返回所以符合条件的位置集合
     */
    public static int[][] seeks(char[] array, String[] content, int i, int gap)
    {
        List<int[]> list = new ArrayList<>(200);
        int[] seek;
        while ((seek = seekString(array, content, i, gap)) != null)
        {
            i = seek[1] + 1;
            list.add(seek);
        }
        return list.toArray(new int[][]{});
    }

    /**
     * 在array中查找content中的char[] ，char[0]的第一个元素与char[length-1]的最后一个元素的间距不会超过gap
     *
     * @param array   需要提供的文本文件
     * @param content 要查找的char[]集合
     * @param i       从offset开始查找
     * @param gap     查找的最大间距
     * @return char[0]的第一个元素与char[length-1]的最后一个元素的索引位置
     */
    public static int[] finds(char[] array, char[][] content, int i, int gap)
    {
        int[] position = new int[2];
        for (int s = 0; s < content.length; s++)
        {
            int[] ints = MyFile.seekChar(array, content[s], i);
            if (ints == null)
            {
                return null;
            }
            if (s == 0)
            {
                position[0] = ints[0];
            }

            if (ints[ints.length - 1] > position[0] + gap - 1)
            {
                s = -1;
                i = ints[ints.length - 1] - (gap - 1);
                continue;
            }
            else
            {
                i = ints[ints.length - 1] + 1;
            }

            if (s == content.length - 1)
            {
                position[1] = ints[ints.length - 1];
                return position;
            }
        }
        return null;
    }

    /**
     * 在array中查找content中的char[] ，char[0]的第一个元素与char[length-1]的最后一个元素的间距不会超过gap
     *
     * @param array   需要提供的文本文件
     * @param content 要查找的char[]集合
     * @param offset  从offset开始查找
     * @param gap     查找的最大间距
     * @return char[0]的第一个元素与char[length-1]的最后一个元素的索引位置
     */
    public static int[] seekString(String array, String[] content, int offset, int gap)
    {
        char[][] ss = new char[content.length][];
        for (int i = 0; i < content.length; i++)
        {
            ss[i] = content[i].toCharArray();
        }
        return finds(array.toCharArray(), ss, offset, gap);
    }

    /**
     * 在array中查找content中的char[] ，char[0]的第一个元素与char[length-1]的最后一个元素的间距不会超过gap
     *
     * @param array   需要提供的文本文件
     * @param content 要查找的char[]集合
     * @param offset  从offset开始查找
     * @param gap     查找的最大间距
     * @return char[0]的第一个元素与char[length-1]的最后一个元素的索引位置
     */
    public static int[] seekString(char[] array, String[] content, int offset, int gap)
    {
        char[][] ss = new char[content.length][];
        for (int i = 0; i < content.length; i++)
        {
            ss[i] = content[i].toCharArray();
        }
        return finds(array, ss, offset, gap);
    }
}
