package xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Administrator
 */
public class XML
{


    /**
     *
     */
    public static Document generatexml(String roots, Map<String, String> s)
    {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(roots);
        String[] toArray = s.keySet().toArray(new String[]{});
        String value;
        for (String s1 : toArray)
        {
            value = s.get(s1);
            root.addElement(s1).setText(value);
        }
        return document;
    }

    /**
     * @param document 1
     * @param path     路径
     * @param append   如果为 true，则将数据写入文件末尾处，而不是写入文件开始处。
     * @param compact  是否压缩
     * @return 1
     */
    public static boolean write(Document document, String path, boolean append, boolean compact)
    {
        return write(document, new File(path), append, compact);
    }

    /**
     * @param document 1
     * @param file     文件
     * @param append   如果为 true，则将数据写入文件末尾处，而不是写入文件开始处。
     * @param compact  是否压缩
     * @return 1
     */
    public static boolean write(Document document, File file, boolean append, boolean compact)
    {
        XMLWriter writer = null;
        BufferedWriter fileWriter = null;
        try
        {
            fileWriter = new BufferedWriter(new FileWriter(file, append));
            OutputFormat format;
            if (compact)
            {
                format = OutputFormat.createCompactFormat();
            }
            else
            {
                format = OutputFormat.createPrettyPrint();
            }
            format.setNewlines(true);
            format.setIndent(true);
            format.setIndent("");
            writer = new XMLWriter(fileWriter, format);
            writer.write(document);
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                if (fileWriter != null)
                {
                    fileWriter.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static XMLData resolveXML(String name)
    {
        XMLData data = null;
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(name.getBytes());
            Document read = reader.read(in);
            Element rootElement = read.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext())
            {
                Element next = iterator.next();
                map.put(next.getName(), next.getText());
            }
            data = new XMLData(rootElement.getName(), map);

        } catch (DocumentException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param path 物理地址
     * @return 1
     */
    public static Document readDocument(String path)
    {
        return readDocument(new File(path));
    }


    public static Document readDocument(File file)
    {
        SAXReader reader = new SAXReader();
        Document document = null;
        try
        {
            document = reader.read(file);
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }
        return document;

    }

    public static String bar(String document, String xMLpath)
    {
        Document document1 = readDocument(document);
        return bar(document1, xMLpath);
    }

    public static String bar(Document document, String path)
    {
        Element root = document.getRootElement();
        bar(root, path);
        return bar(root, path);
    }

    public static String bar(Element root, String path)
    {
        String bar;
        bar = getElement(root, path).getText();
        return bar;
    }

    /**
     * HTML导航它只能找到第一次出现的元素
     *
     * @param root
     * @param path 元素关系
     *             如果 c元素是b元素的子元素 b元素是a元素的子元素
     *             那么就可以表示为 a/b/c
     * @return 需要的 Element
     */
    public static Element getElement(Element root, String path)
    {
        Element element = null;
        String[] split = path.split("/");
        for (String s : split)
        {
            if (s.trim().isEmpty())
            {
                continue;
            }
            for (Iterator<Element> it = root.elementIterator(s); it.hasNext(); )
            {
                root = it.next();
                element = root;
                break;
            }
        }
        return element;
    }

    /**
     * HTML 导航 它可以找到html树下任意一个 Element
     *
     * @param root      根元素
     * @param path      元素关系
     *                  如果 c元素是b元素的子元素 b元素是a元素的子元素
     *                  那么就可以表示为 a/b/c
     * @param frequency 元素的索引位置
     *                  比如要找 root元素下第 3 个 a元素下的第 4 个 b元素下的第 2 个 c元素
     *                  可以调 getElement(root,"a/b/c",new int[]{3,4,2})
     * @return 需要的 Element
     */
    public static Element getElement(Element root, String path, int[] frequency)
    {
        Element next = null;
        int s = 0;
        int i = 0;
        String[] split = path.split("/");
        for (; i < frequency.length; i++)
        {
            if (split[i].trim().isEmpty())
            {
                continue;
            }
            if (root == null)
            {
                return null;
            }
            for (Iterator<Element> it = root.elementIterator(split[i]); it.hasNext() && frequency[i] > s; s++)
            {
                next = it.next();
            }
            root = next;
            s = 0;
        }
        return root;

    }

    /**
     * 创建一个新的 Document
     *
     * @return
     */
    public static Document getDocument()
    {
        return DocumentHelper.createDocument();
    }

    public static class XMLData
    {
        private String root;
        private Map<String, String> map;

        public XMLData(String root, Map<String, String> map)
        {
            this.root = root;
            this.map = map;
        }

        public String getRoot()
        {
            return root;
        }

        public Map<String, String> getMap()
        {
            return map;
        }
    }

}
