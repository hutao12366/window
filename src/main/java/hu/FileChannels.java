package hu;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 用于描述操作文件的信息
 *
 * @author Administrator
 */
public class FileChannels
{

    /**
     * @param path   路径
     * @param data   数据
     * @param offset 要写入的第一个字节的索引
     * @param length 要写入的字节数
     * @param append 如果 true ，则字节将被写入文件的末尾而不是开头
     */
    public static void write(String path, byte[] data, int offset, int length, boolean append)
    {
        ByteBuffer buffer;
        buffer = ByteBuffer.allocate(length);
        buffer.put(data, offset, length);
        buffer.flip();
        write(path, new ByteBuffer[]{buffer}, append);
        buffer.clear();
    }

    /**
     * @param path   路径
     * @param data   数据
     * @param offset 要写入的第一个字节的索引
     * @param length 要写入的字节数
     */
    public static void write(String path, byte[] data, int offset, int length)
    {
        write(path, data, offset, length, false);
    }

    /**
     * @param path 路径
     * @param data 数据
     */
    public static void write(String path, byte[] data)
    {
        write(path, data, 0, data.length, false);
    }

    /**
     * @param path   路径
     * @param data   数据
     * @param append 如果 true ，则字节将被写入文件的末尾而不是开头
     */
    public static void write(String path, byte[] data, boolean append)
    {
        write(path, data, 0, data.length, append);
    }

    public static void write(String path, ByteBuffer[] buffer, OpenOption... options)
    {
        FileChannel open = null;
        try
        {
            open = FileChannel.open(Paths.get(path), options);
            open.write(buffer);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (open != null)
                {
                    open.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param path   路径
     * @param buffer 缓冲区
     */
    public static void write(String path, ByteBuffer buffer)
    {
        write(path, new ByteBuffer[]{buffer}, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

    }

    /**
     * @param path   路径
     * @param buffer 缓冲区
     * @param append 如果 true ，则字节将被写入文件的末尾而不是开头
     */
    public static void write(String path, ByteBuffer[] buffer, boolean append)
    {
        if (append)
        {
            write(path, buffer, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        else
        {
            write(path, buffer, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        }
    }

    /**
     * 使用此函数文件字节数不得超出 int 范围，如果文件字节数超过 int 范围建议使用reads(String path, long capacity)函数
     *
     * @param path
     * @return
     */
    public static byte[] read(String path)
    {
        if (!new File(path).exists())
        {
            return null;
        }
        FileChannel open = null;
        byte[] bytes = null;
        try
        {
            open = FileChannel.open(Paths.get(path), StandardOpenOption.READ, StandardOpenOption.WRITE);
            ByteBuffer allocate = ByteBuffer.allocate((int) open.size());
            open.read(allocate);
            allocate.flip();
            bytes = allocate.array();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (open != null)
                {
                    open.close();
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    /**
     * @param path 路径
     * @return
     */
    public static ByteBuffer[] reads(String path)
    {
        FileChannel open = null;
        ByteBuffer[] buffers = null;
        try
        {
            open = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
            double l = (open.size() + 0.0) / (2147483647 + 0.0);
            int ceil = (int) Math.ceil(l);
            buffers = new ByteBuffer[ceil];
            long cc = open.size();
            for (int i = 0; i < ceil; i++)
            {
                if (cc > 0 && cc < 2147483647)
                {
                    buffers[i] = ByteBuffer.allocate((int) cc);
                    break;
                }
                else
                {
                    buffers[i] = ByteBuffer.allocate(2147483647);
                    cc -= 2147483647;
                }
            }
            open.read(buffers);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (open == null)
                {
                    open.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return buffers;
    }

    public static byte[] read(File file)
    {
        return read(file.getPath());
    }

    /**
     * 使用特定的编码方案解码application/x-www-form-urlencoded字符串。 所提供的编码用于确定由“ %xy
     * ”形式的任何连续序列表示哪些字符。 注意： World Wide Web Consortium Recommendation规定应使用UTF-8。
     * 不这样做可能会引起不兼容。
     *
     * @param html 要解码的 String
     * @param enc  支持的名称 character encoding
     * @return 新解码的 String
     */
    public static byte[] uRLDecoding(byte[] html, String enc)
    {
        byte[] bytes = null;

        try
        {
            bytes = URLDecoder.decode(new String(html), enc).getBytes();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }

}