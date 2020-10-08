package hu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class Test
{

    public static void main(String[] args)
    {

    }

    public static String a(int amount)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= amount; i++)
        {
            builder.append("第").append(i).append("章").append("\r\n");
        }
        return builder.toString();
    }


    public static String[] seek(char[] array, char a, char b)
    {
        List<String> list = new ArrayList<>();
        String[] valueArray;
        boolean is = false;
        boolean cc = false;
        int c = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
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
                    if (s == '<')
                    {
                        cc = true;
                    }
                    else if (s == '>' && cc)
                    {
                        cc = false;
                        builder.append(a(builder2.toString()));
                        builder2.delete(0, builder.length());

                    }
                    else
                    {
                        if (cc)
                        {
                            builder2.append(s);
                        }
                        if (!cc)
                        {
                            builder.append(s);
                        }
                    }

                }
            }
        }
        if (list.isEmpty())
        {
            valueArray = new String[]{new String(array)};
        }
        else
        {
            valueArray = list.toArray(new String[]{});
        }
        return valueArray;
    }

    private static String a(String a)
    {
        switch (a)
        {
            case "br":
                return "\r\n";
            case "b":
                return "\n";
            case "r":
                return "<";
            case "y":
                return ">";
            default:

                break;
        }

        return "";
    }

}
