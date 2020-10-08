package hu;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class Text
{

    private static char[] c;

    public static void main(String[] args)
    {

    }

    /**
     * 从文本文件中截取内容
     *
     * @param path   路径
     * @param offset
     * @param length
     * @return
     */
    public static char[] readText(String path, int offset, int length)
    {
        BufferedReader reader = null;
        char[] a = new char[length];
        try
        {
            reader = new BufferedReader(new FileReader(path));
            reader.skip(offset);
            reader.read(a);

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return a;
    }

    /**
     * @param path 给定文本
     * @return 文内容
     */
    public static char[] readChar(String path)
    {
        return readChar(new File(path));
    }

    /**
     * @param file 给定文本
     */
    public static char[] readChar(File file)
    {
        char[] array = null;

        if (file.isFile())
        {
            BufferedReader reader = null;
            CharArrayWriter chars;
            try
            {
                chars = new CharArrayWriter();
                reader = new BufferedReader(new FileReader(file));
                char[] a = new char[1024 * 10];
                int i;
                while ((i = reader.read(a)) != -1)
                {
                    chars.write(a, 0, i);
                }
                array = chars.toCharArray();
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return array;
    }

    /**
     * 截取指定字符范围内的字符
     *
     * @param array 给定字符
     * @param a     从a字符后开始截取
     * @param b     到b字符前停住截取
     * @return 1
     */
    public static String[] seek(char[] array, char a, char b)
    {
        List<String> list = new ArrayList<>();
        boolean is = false;
        int c = 0;
        StringBuilder builder = new StringBuilder();
        for (char s : array)
        {
            if (s == a)
            {
                c++;
                if (c > 1)
                {
                    builder.append(s);
                }
                is = true;
            }
            else if (s == b && is)
            {
                if (c > 0)
                {
                    c--;
                }
                if (c != 0)
                {
                    builder.append(s);
                }
                else
                {
                    is = false;
                    list.add(builder.toString());
                    builder.delete(0, builder.length());
                }
            }
            else
            {
                if (is)
                {
                    builder.append(s);
                }
            }
        }
        return list.toArray(new String[]{});
    }

    /**
     * 使特定的字符串用指定的字符包围
     *
     * @param text 给定字符串
     * @param a    分隔符
     * @param b    指定用于包裹字符串的字符串数组
     * @return
     */
    public static String surround(String text, String a, String[] b)
    {
        StringBuilder builder = new StringBuilder();
        if (text == null || text.isEmpty() || a == null || b == null)
        {
            return null;
        }
        String[] split = text.split(a);
        if (split[0].equals(text))
        {
            return null;
        }
        for (String s : split)
        {
            if (!s.trim().isEmpty())
            {
                builder.append(b[0]).append(s).append(b[1]);
            }
        }
        return builder.toString();

    }

    public static String[] readToArray(String path, int[][] a)
    {
        String[] s = new String[a.length];
        for (int i = 0; i < s.length; i++)
        {
            s[i] = new String(readText(path, a[i][0], (a[i][a[i].length - 1] - a[i][0]) + 1));
        }
        return s;
    }

    /**
     * 截取文本内容
     *
     * @param array 提供需要的文本
     * @param a     开始到结束的位置集合
     * @return 1
     */
    public static String[] readToArray(char[] array, int[][] a)
    {
        String[] s = new String[a.length];
        for (int i = 0; i < s.length; i++)
        {
            s[i] = interceptTxt(array, a[i]);
        }
        return s;
    }

    public static String interceptTxt(char[] array, int[] a)
    {
        int i = (a[a.length - 1] - a[0]) + 1;
        if (c == null || c.length < i)
        {
            c = new char[i];
        }
        System.arraycopy(array, a[0], c, 0, i);
        return new String(c, 0, i);

    }

}
