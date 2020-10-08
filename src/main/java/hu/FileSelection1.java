package hu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * @author Administrator
 */
public class FileSelection1 extends JFileChooser
{
    /**
     *
     */
    private static final long serialVersionUID = -632786135687995536L;

    public FileSelection1()
    {
        setFileSelectionMode(FILES_AND_DIRECTORIES);
        setMultiSelectionEnabled(false);
        setApproveButtonText("保存");
        setFileFilter(new FileNameExtensionFilter("文本", "txt", "xml", "html"));
        setFont(new Font("宋体", Font.PLAIN, 12));
    }
}
