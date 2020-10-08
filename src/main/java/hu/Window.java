package hu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

enum Style
{
    REPLACING_A, ALL_REPLACEMENT

}

/**
 * @author Administrator
 */
public class Window extends JFrame
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JList<String> list;
    private final RJTextArea textArea;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private File file;
    private String encoder = "UTF-8";
    private int[][] ints;

    /**
     * Create the frame.
     */
    private Window()
    {

        Font font = new Font("Dialog", Font.PLAIN, 12);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
            {
                UIManager.put(key, font);
            }
        }
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setFont(new Font("宋体", Font.PLAIN, 12));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setForeground(Color.BLACK);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0);
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        splitPane.setRightComponent(scrollPane);

        textArea = new RJTextArea("");
        textArea.setDropMode(DropMode.INSERT);
        textArea.setFont(new Font("宋体", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        splitPane.setLeftComponent(scrollPane_1);

        list = new JList<>();
        list.setBorder(new TitledBorder(null, "\u76EE\u5F55", TitledBorder.LEFT, TitledBorder.TOP, null, null));
        list.setModel(model);
        scrollPane_1.setViewportView(list);

        list.addListSelectionListener(this::addComponentListener);
        Menu menu = new Menu();
        addWindowListener(menu.windowClose);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            try
            {
                Window frame = new Window();
                frame.setVisible(true);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    private void clear()
    {
        try
        {
            this.textArea.reset();
            if (ints == null)
            {
                return;
            }
            model.removeAllElements();
            ints = null;
        } catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void addComponentListener(ListSelectionEvent listSelectionEvent)
    {
        int select = list.getSelectedIndex();
        int i = -1;
        if (select == i)
        {
            return;
        }
        this.textArea.requestFocus();
        this.textArea.setCaretPosition(ints[select][0]);
        this.textArea.select(ints[select][0], ints[select][1] + 1);
    }

    private void setModel(String[] value, int[][] ints)
    {
        if (ints != null)
        {
            this.ints = null;
            this.ints = ints;
        }
        if (value != null)
        {
            model.removeAllElements();
            for (String v : value)
            {
                model.addElement(v);
            }
        }
    }

    private void popUps(Component component, String text)
    {
        JOptionPane.showMessageDialog(component, text, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 菜单栏实现
     */
    private class Menu
    {
        Window win = Window.this;
        JMenuBar bar;
        JMenu cd, cd1, cd2, cd3;
        JMenuItem item, item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13,
                item14, item15, item16, item17, item18;
        JCheckBoxMenuItem box, box1, box2;
        String text = "";// 与设置目录相关
        String[] textArrays;// 与设置目录相关
        int i1, lengths;// 与设置目录相关
        boolean isSelect;// 与自动刷新目录相关
        List<Integer> select;// 与文件选择相关
        RightClickMenu rightClickMenu;
        RJTextArea revocation;
        Toolkit toolkit;
        Clipboard systemClipboard;
        MenuListeners menuListeners;
        WindowAdapters windowClose;// 窗口关闭侦听器
        JDialog dialog, dialog1, dialog2, dialog3, dialog4, dialog5, dialog6, dialog7;

        private Menu()
        {
            bar = new JMenuBar();
            win.setJMenuBar(bar);
            revocation = win.textArea;
            toolkit = Toolkit.getDefaultToolkit();
            systemClipboard = toolkit.getSystemClipboard();
            menuListeners = new MenuListeners();
            windowClose = new WindowAdapters();
            dialog7 = new PopupWhether(win, "记事本");

            cd = this.newMenu("文件(F)");
            cd1 = this.newMenu("编辑(E)");
            cd2 = this.newMenu("格式(O)");
            cd3 = this.newMenu("查看(V)");

            item = this.newItem("打开(O)");
            item1 = this.newItem("保存(S)");
            item2 = this.newItem("另存为(A)");
            item3 = this.newItem("页面设置(U)");
            item4 = this.newItem("替换(R)");
            item5 = this.newItem("字体(F)");
            item6 = this.newItem("撤销(U)");
            item7 = this.newItem("目录设置(Q)");
            item8 = this.newItem("查找(F)");
            item9 = this.newItem("按目录替换(D)");
            item10 = this.newItem("退出(X)");
            item11 = this.newItem("转到(G)");
            item12 = this.newItem("复制(C)");
            item13 = this.newItem("粘贴(P)");
            item14 = this.newItem("删除(L)");
            item15 = this.newItem("剪切(T)");
            item16 = this.newItem("重做(X)");
            item17 = this.newItem("全选(A)");
            item18 = this.newItem("新建(N)");

            box = this.newBox("自动换行(W)", true);
            box1 = this.newBox("自动刷新目录(F)", false);
            box2 = this.newBox("文本可编辑(S)", false);

            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
            item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
            item12.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            item13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
            item15.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
            item17.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
            item14.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
            item8.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
            item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
            item11.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
            item18.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
            item16.setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK,
                            true));

            cd.setMnemonic('F');
            cd1.setMnemonic('E');
            cd2.setMnemonic('O');
            cd3.setMnemonic('V');

            item.setMnemonic('O');
            item1.setMnemonic('S');
            item2.setMnemonic('A');
            item3.setMnemonic('U');
            item4.setMnemonic('R');
            item5.setMnemonic('F');
            item6.setMnemonic('U');
            item7.setMnemonic('Q');
            item8.setMnemonic('F');
            item9.setMnemonic('D');
            item10.setMnemonic('X');
            item11.setMnemonic('G');
            item12.setMnemonic('C');
            item13.setMnemonic('P');
            item14.setMnemonic('L');
            item15.setMnemonic('T');
            item16.setMnemonic('X');
            item17.setMnemonic('A');
            item18.setMnemonic('N');

            box.setMnemonic('W');
            box1.setMnemonic('F');
            box2.setMnemonic('S');

            cd.add(item18);
            cd.add(item);
            cd.add(item1);
            cd.add(item2);
            cd.addSeparator();
            cd.add(item3);
            cd.add(item10);

            cd1.add(item6);
            cd1.add(item16);
            cd1.addSeparator();
            cd1.add(item15);
            cd1.add(item12);
            cd1.add(item13);
            cd1.add(item14);
            cd1.addSeparator();
            cd1.add(box2);
            cd1.addSeparator();
            cd1.add(item8);
            cd1.add(item4);
            cd1.add(item9);
            cd1.add(item11);
            cd1.addSeparator();
            cd1.add(item17);

            cd2.add(box);
            cd2.add(item5);

            cd3.add(box1);
            cd3.add(item7);

            bar.add(cd);
            bar.add(cd1);
            bar.add(cd2);
            bar.add(cd3);

            rightClickMenu = new RightClickMenu();
            addMonitor();
            new PopupMenu();
        }

        private JCheckBoxMenuItem newBox(String text, boolean is)
        {
            JCheckBoxMenuItem box = new JCheckBoxMenuItem(text);
            box.setPreferredSize(new Dimension(120, 30));
            box.setHorizontalAlignment(SwingConstants.LEFT);
            box.setState(is);
            return box;
        }

        private JMenu newMenu(String text)
        {
            JMenu jMenu = new JMenu(text);
            jMenu.setPreferredSize(new Dimension(55, 15));
            jMenu.setHorizontalAlignment(SwingConstants.LEFT);
            return jMenu;
        }

        private JMenuItem newItem(String text)
        {
            JMenuItem item = new JMenuItem(text);
            item.setPreferredSize(new Dimension(150, 25));
            item.setHorizontalAlignment(SwingConstants.LEFT);
            return item;
        }

        private void addMonitor()
        {
            this.item.addActionListener(this::actionPerformed);
            this.box.addActionListener(this::actionPerformed_1);
            this.item5.addActionListener(this::actionPerformed_2);
            this.item1.addActionListener(this::actionPerformed3);
            this.item6.addActionListener(this::actionPerformed4);
            this.item8.addActionListener(this::actionPerformed_5);
            this.item7.addActionListener(this::actionPerformed_6);
            this.box1.addActionListener(this::actionPerformed_7);
            this.item10.addActionListener(this::actionPerformed_8);
            this.item2.addActionListener(this::actionPerformed_9);
            this.item4.addActionListener(this::actionPerformed_10);
            this.item9.addActionListener(this::actionPerformed_11);
            this.item11.addActionListener(this::actionPerformed_12);
            this.box2.addActionListener(this::actionPerformed_13);
            this.item16.addActionListener(this::actionPerformed_14);
            this.item12.addActionListener(this::actionPerformed_15);
            this.item13.addActionListener(this::actionPerformed_16);
            this.item14.addActionListener(this::actionPerformed_17);
            this.item15.addActionListener(this::actionPerformed_18);
            this.item17.addActionListener(this::actionPerformed_19);
            this.item18.addActionListener(this::actionPerformed_20);
            this.item3.addActionListener(this::actionPerformed_21);
            this.cd1.addMenuListener(menuListeners);
        }

        private void actionPerformed_21(ActionEvent actionEvent)
        {
            if (dialog4 == null)
            {
                dialog4 = new PopupPageSetupWindow(win, "页面设置", true);
            }
            dialog4.setVisible(true);
        }

        private void actionPerformed_20(ActionEvent actionEvent)
        {
            win.clear();
            win.file = null;
            win.textArea.setEditable(true);
            this.box2.setState(true);
        }

        private void actionPerformed_19(ActionEvent actionEvent)
        {
            win.textArea.requestFocus();
            win.textArea.select(0, win.textArea.getText().length());

        }

        private void actionPerformed_18(ActionEvent actionEvent)
        {
            String text;
            StringSelection selection;
            text = win.textArea.getSelectedText();
            selection = new StringSelection(text);
            systemClipboard.setContents(selection, null);
            win.textArea.replaceRange("", win.textArea.getSelectionStart(), win.textArea.getSelectionEnd());
        }

        private void actionPerformed_17(ActionEvent actionEvent)
        {
            win.textArea.replaceRange("", win.textArea.getSelectionStart(), win.textArea.getSelectionEnd());
        }

        private void actionPerformed_16(ActionEvent actionEvent)
        {
            if (!systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor))
            {
                return;
            }
            Transferable contents = systemClipboard.getContents(this);
            if (contents != null)
            {
                win.textArea.requestFocus();
                String text = "";
                try
                {
                    text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException e)
                {
                    e.printStackTrace();
                }
                win.textArea.replaceRange(text, win.textArea.getSelectionStart(), win.textArea.getSelectionEnd());

            }

        }

        /**
         * 复制
         *
         * @param actionEvent 1
         */
        private void actionPerformed_15(ActionEvent actionEvent)
        {
            String selectedText = win.textArea.getSelectedText();
            if (selectedText != null)
            {
                StringSelection selection = new StringSelection(selectedText);
                systemClipboard.setContents(selection, null);

            }

        }

        private void actionPerformed_14(ActionEvent actionEvent)
        {
            if (this.revocation.isRedo())
            {
                this.item14.setEnabled(true);
                this.revocation.redo();
            }

        }

        //
        private void actionPerformed_13(ActionEvent actionEvent)
        {
            win.textArea.setEditable(box2.isSelected());
        }

        private void actionPerformed_12(ActionEvent actionEvent)
        {
            if (dialog3 != null)
            {
                dialog3.setVisible(true);
                return;
            }
            JLabel label;
            JButton button, button1_1;
            JPanel panel, panel_1;
            Container contentPane;

            dialog3 = new JDialog(win, "转到指定字符", false);
            contentPane = dialog3.getContentPane();
            label = new JLabel("字符位置(L):");
            button = new JButton("取消");
            button1_1 = new JButton("转到");
            NumberFormat format = NumberFormat.getIntegerInstance();
            format.setGroupingUsed(false);
            JFormattedTextField field = new JFormattedTextField(format);
            field.getText();

            panel = new JPanel();
            panel_1 = new JPanel();

            contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 6));
            dialog3.setResizable(false);
            dialog3.setLocationRelativeTo(win);

            dialog3.setSize(400, 200);
            panel.setPreferredSize(new Dimension(394, 120));
            panel_1.setPreferredSize(new Dimension(394, 45));
            label.setPreferredSize(new Dimension(394, 45));
            button.setPreferredSize(new Dimension(90, 30));
            button1_1.setPreferredSize(new Dimension(90, 30));
            field.setPreferredSize(new Dimension(375, 36));

            panel.add(label);
            panel.add(field);

            panel_1.add(button1_1);
            panel_1.add(button);

            contentPane.add(panel);
            contentPane.add(panel_1);

            button.addActionListener(e -> dialog3.dispose());
            button1_1.addActionListener(e ->
            {
                String texts;
                texts = field.getText();
                if (texts.isEmpty())
                {
                    win.popUps(dialog3, "请输入行数");
                    return;
                }
                else if (Integer.parseInt(texts.trim()) <= 0)
                {
                    win.popUps(dialog3, "索引必须大于0");
                    return;
                }
                int i = Integer.parseInt(texts.trim());
                if (i > win.textArea.getDocument().getLength())
                {
                    win.popUps(dialog3, "索引不能大于文本长度");
                    return;
                }
                win.textArea.requestFocus();
                win.textArea.setCaretPosition(i);
                win.textArea.select(i, i - 1);
            });

            dialog3.setVisible(true);
        }

        private void actionPerformed_11(ActionEvent actionEvent)
        {
            if (dialog2 == null)
            {
                dialog2 = new PopupWindow(win, "按目录替换", false);
            }
            dialog2.setVisible(true);
        }

        private void actionPerformed_10(ActionEvent actionEvent)
        {
            if (dialog1 == null)
            {
                dialog1 = new PopupReplaceWindow(win, "替换", false);
            }
            dialog1.setVisible(true);
        }

        private void actionPerformed_9(ActionEvent actionEvent)
        {
            FileSelection1 selection1;
            int i;
            String text;

            selection1 = new FileSelection1();
            text = win.textArea.getText();
            selection1.setDialogTitle("保存");
            if (text == null || text.length() == 0)
            {
                win.popUps(win, "没有文本内容");
                return;
            }
            i = selection1.showOpenDialog(win);

            if (FileSelection.APPROVE_OPTION == i)
            {
                File file = selection1.getSelectedFile();
                if (file == null || !file.getPath().contains("."))
                {
                    win.popUps(win, "无效文件路径");
                    return;
                }
                try
                {
                    FileChannels.write(file.getPath(), text.getBytes(win.encoder));
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        }

        private void actionPerformed_8(ActionEvent actionEvent)
        {
            windowClose.windowClosing(new WindowEvent(win, 201));

        }

        private void actionPerformed_7(ActionEvent actionEvent)
        {
            isSelect = box1.isSelected();
        }

        private void actionPerformed_6(ActionEvent actionEvent)
        {
            if (dialog5 == null)
            {
                dialog5 = new PopupSetCatalogue(win, "目录设置", true, this.text, this.i1, this.lengths);
            }
            dialog5.setVisible(true);
        }

        private void actionPerformed_5(ActionEvent actionEvent)
        {
            if (dialog == null)
            {
                dialog = new PopupSeekWindow(win, "查找", false);
            }
            dialog.setVisible(true);

        }

        private void actionPerformed4(ActionEvent actionEvent)
        {
            if (this.revocation.isUndo())
            {
                this.revocation.undo();
                this.item6.setEnabled(true);
            }
        }

        private void actionPerformed3(ActionEvent actionEvent)
        {
            File file1 = null;
            try
            {
                String text = win.textArea.getText();
                if (text == null || text.isEmpty())
                {
                    win.popUps(win, "没有文本内容");
                    return;
                }
                if (win.file == null)
                {
                    FileSelection selection1 = new FileSelection();
                    int i = selection1.showOpenDialog(win);
                    if (i == FileSelection.APPROVE_OPTION)
                    {
                        file1 = selection1.getSelectedFile();
                    }
                    else if (i == FileSelection.CANCEL_OPTION)
                    {
                        return;
                    }
                }
                else
                {
                    file1 = win.file;
                }

                assert file1 != null;
                FileChannels.write(file1.getPath(), text.getBytes(win.encoder));
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

        }

        private void actionPerformed_2(ActionEvent e)
        {
            if (dialog6 == null)
            {
                dialog6 = new PopupSetFontWindow(win, "字体设置", true);
            }
            dialog6.setVisible(true);
        }

        private void actionPerformed_1(ActionEvent e)
        {
            win.textArea.setLineWrap(box.isSelected());

        }

        private void actionPerformed(ActionEvent e)
        {
            FileSelection select = new FileSelection();
            if (select.showOpenDialog(win) == FileSelection.APPROVE_OPTION)
            {
                File file = select.getSelectedFile();
                if (file.isFile())
                {
                    win.file = file;
                }
                if (Window.this.file != null)
                {
                    byte[] read = FileChannels.read(win.file);
                    try
                    {
                        win.textArea.setEditable(false);
                        win.setTitle(file.getName());
                        this.revocation.setText(new String(read, win.encoder));
                        win.textArea.setCaretPosition(0);
                        System.gc();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void checkMenuItemEnabled()
        {
            String text = win.textArea.getSelectedText();
            boolean isThereData = win.textArea.getText().isEmpty();
            if (text == null)
            {
                this.item12.setEnabled(false);
                this.item14.setEnabled(false);
                this.item15.setEnabled(false);

            }
            else
            {
                this.item12.setEnabled(true);
                this.item14.setEnabled(true);
                this.item15.setEnabled(true);

            }
            if (isThereData)
            {
                this.item4.setEnabled(false);
                this.item8.setEnabled(false);
                this.item9.setEnabled(false);
                this.item11.setEnabled(false);
                this.item17.setEnabled(false);

            }
            else
            {
                this.item4.setEnabled(true);
                this.item8.setEnabled(true);
                this.item9.setEnabled(true);
                this.item11.setEnabled(true);
                this.item17.setEnabled(true);
            }

            this.item13.setEnabled(systemClipboard.getContents(this) != null);
            item6.setEnabled(revocation.isUndo());
            this.item16.setEnabled(revocation.isRedo());

        }

        /**
         * 页面设置
         */
        private class PopupPageSetupWindow extends JDialog
        {
            /**
             *
             */
            private static final long serialVersionUID = -6145094968455471165L;
            JPanel panel, panel1, panel2, panel3, panel4;
            JButton button, button_1;
            JLabel label, label_1;
            JComboBox<String> comboBox, comboBox_1;
            Color[] colors;
            JTextArea area_1;

            PopupPageSetupWindow(Frame owner, String title, boolean modal)
            {
                super(owner, title, modal);
                setSize(550, 320);
                setResizable(false);
                setLocationRelativeTo(win);
                Container contentPane = getContentPane();

                panel = new JPanel();
                panel1 = new JPanel();
                panel2 = new JPanel();
                panel3 = new JPanel();
                panel4 = new JPanel();
                new JPanel();
                button = new JButton("确认");
                button_1 = new JButton("取消");
                label = new JLabel("背景:");
                label_1 = new JLabel("字体颜色:");
                comboBox = new JComboBox<>();
                comboBox_1 = new JComboBox<>();
                area_1 = new JTextArea();

                contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
                panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
                panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
                panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 7));
                panel3.setLayout(null);

                addModel();

                area_1.setBounds(40, 70, 90, 120);
                area_1.setEditable(false);
                area_1.setLineWrap(true);
                area_1.setFont(new Font("宋体", Font.PLAIN, 5));
                for (int i = 0; i < 40; i++)
                {
                    area_1.append("Hello World");
                }

                panel3.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "示例", TitledBorder.LEFT,
                                TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 12), Color.CYAN));
                panel4.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "纸张", TitledBorder.LEFT,
                                TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 12), Color.CYAN));

                panel.setPreferredSize(new Dimension(544, 260));
                panel1.setPreferredSize(new Dimension(544, 32));
                panel2.setPreferredSize(new Dimension(340, 260));
                panel3.setPreferredSize(new Dimension(175, 260));
                panel4.setPreferredSize(new Dimension(340, 90));
                button.setPreferredSize(new Dimension(90, 20));
                button_1.setPreferredSize(new Dimension(90, 20));
                comboBox.setPreferredSize(new Dimension(250, 20));
                comboBox_1.setPreferredSize(new Dimension(250, 20));
                label.setPreferredSize(new Dimension(60, 20));
                label_1.setPreferredSize(new Dimension(60, 20));
                area_1.setPreferredSize(new Dimension(60, 20));

                panel4.add(label);
                panel4.add(comboBox);
                panel4.add(label_1);
                panel4.add(comboBox_1);

                panel2.add(panel4);

                panel3.add(area_1);

                panel.add(panel2);
                panel.add(panel3);

                panel1.add(button);
                panel1.add(button_1);

                contentPane.add(panel);
                contentPane.add(panel1);

                //
                //                panel.setBorder(new LineBorder(Color.MAGENTA));
                //                panel_1.setBorder(new LineBorder(Color.MAGENTA));
                //                panel_2.setBorder(new LineBorder(Color.BLUE));
                //                panel_5.setBorder(new LineBorder(Color.MAGENTA));

                monitor();
            }

            private void addModel()
            {
                colors = new Color[10];
                String[] color = new String[10];
                this.colors[0] = new Color(0, 0, 0);
                color[0] = "黑色";
                this.colors[1] = Color.RED;
                color[1] = "红色";
                this.colors[2] = new Color(255, 255, 255);
                color[2] = "白色";
                this.colors[3] = Color.BLUE;
                color[3] = "蓝色";
                this.colors[4] = Color.ORANGE;
                color[4] = "桔黄色";
                this.colors[5] = Color.YELLOW;
                color[5] = "黄色";
                this.colors[6] = Color.PINK;
                color[6] = "粉红色";
                this.colors[7] = Color.GREEN;
                color[7] = "绿色";
                this.colors[8] = Color.GRAY;
                color[8] = "灰色";
                this.colors[9] = Color.DARK_GRAY;
                color[9] = "深灰色";

                comboBox.setModel(new DefaultComboBoxModel<>(color));
                comboBox_1.setModel(new DefaultComboBoxModel<>(color));
                comboBox.setSelectedIndex(2);
                comboBox_1.setSelectedIndex(0);

            }

            private void monitor()
            {
                comboBox.addActionListener(this::actionPerformed);
                comboBox_1.addActionListener(this::actionPerformed);
                button.addActionListener(this::actionPerformed_2);
                button_1.addActionListener(this::actionPerformed1);
            }

            private void actionPerformed_2(ActionEvent actionEvent)
            {
                win.textArea.setForeground(area_1.getForeground());
                win.textArea.setBackground(area_1.getBackground());
                this.dispose();
            }

            private void actionPerformed1(ActionEvent actionEvent)
            {
                this.dispose();
            }

            private void actionPerformed(ActionEvent e)
            {
                int select, select_1;
                select = comboBox.getSelectedIndex();
                select_1 = comboBox_1.getSelectedIndex();
                area_1.setBackground(colors[select]);
                area_1.setForeground(colors[select_1]);
            }

        }

        private class MenuListeners implements MenuListener
        {

            @Override
            public void menuSelected(MenuEvent e)
            {
                checkMenuItemEnabled();
            }

            @Override
            public void menuDeselected(MenuEvent e)
            {
                item6.setEnabled(true);
                item16.setEnabled(true);
            }

            @Override
            public void menuCanceled(MenuEvent e)
            {

            }
        }

        private class PopupWindow extends JDialog
        {

            private static final long serialVersionUID = 1L;
            final String[] as = new String[]{"@", "/", "$", "\n", "\r\n"};
            JTextArea textArea;
            JButton button, button_1, button_2, button_3;
            JPanel panel, panel_1, panel_2;
            JScrollPane scrollPane;
            JComboBox<String> comboBox;

            private PopupWindow(Frame owner, String title, boolean modal)
            {
                super(owner, title, modal);
                setSize(600, 450);
                setLocationRelativeTo(win);
                Container pane = getContentPane();

                button = new JButton("确认");
                button_1 = new JButton("取消");
                button_2 = new JButton("添加文件");
                button_3 = new JButton("清空");
                textArea = new JTextArea("");
                panel = new JPanel();
                panel_1 = new JPanel();
                panel_2 = new JPanel();
                scrollPane = new JScrollPane(textArea);
                comboBox = new JComboBox<>();

                pane.setLayout(new BorderLayout(0, 0));
                panel.setLayout(new FlowLayout(FlowLayout.LEFT));
                panel_1.setLayout(new BorderLayout(0, 0));
                panel_2.setLayout(new FlowLayout(FlowLayout.RIGHT));

                panel.setPreferredSize(new Dimension(100, 250));
                panel_2.setPreferredSize(new Dimension(400, 35));
                button_2.setPreferredSize(new Dimension(90, 24));
                button_3.setPreferredSize(new Dimension(90, 24));
                comboBox.setPreferredSize(new Dimension(90, 24));
                button.setPreferredSize(new Dimension(80, 25));
                button_1.setPreferredSize(new Dimension(80, 25));

                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"@", "/", "$", "\\n", "\\r\\n"}));
                comboBox.setEditable(true);
                comboBox.setSelectedIndex(0);
                comboBox.setToolTipText("设置分割符");
                textArea.setLineWrap(true);

                panel.add(comboBox);
                panel.add(button_2);
                panel.add(button_3);

                panel_1.add(scrollPane, BorderLayout.CENTER);

                panel_2.add(button);
                panel_2.add(button_1);

                pane.add(panel, BorderLayout.WEST);
                pane.add(panel_1, BorderLayout.CENTER);
                pane.add(panel_2, BorderLayout.SOUTH);

                Monitor();
            }

            private void Monitor()
            {
                button_2.addActionListener(this::actionPerformed);
                comboBox.addActionListener(this::actionPerformed_1);
                button_1.addActionListener(this::actionPerformed_2);
                button.addActionListener(this::actionPerformed3);
                button_3.addActionListener(this::actionPerformed4);

            }

            private void actionPerformed4(ActionEvent actionEvent)
            {
                this.textArea.setText("");
            }

            private void actionPerformed3(ActionEvent actionEvent)
            {
                String texts;
                int[][] a;
                int c = 0;
                int len = 0;
                String[] seek;
                char[] arrayData;
                if (Menu.this.select.isEmpty())
                {
                    win.popUps(this, "请选择目录");
                    return;
                }
                a = new int[select.size()][];
                rightClickMenu.itemMonitor(null);
                for (int s : select)
                {
                    a[c] = win.ints[s];
                    c++;
                }
                texts = PopupWindow.this.textArea.getText();
                seek = Test.seek(texts.toCharArray(), '[', ']');
                for (int[] value : a)
                {
                    len += (value[1] - value[0]);
                }
                double v = (texts.length() - seek.length * 2) * (a.length / (seek.length + 0.0));
                int n = (int) Math.ceil(v);
                len = n - len;
                arrayData = new char[win.textArea.getText().length() + len];
                MyFile.replace(arrayData, win.textArea.getText().toCharArray(), seek, a);
                win.clear();
                win.textArea.setText(new String(arrayData).trim(), false);
                select.clear();
                this.dispose();
            }

            private void actionPerformed_2(ActionEvent actionEvent)
            {
                dispose();
            }

            private void actionPerformed_1(ActionEvent actionEvent)
            {
                String ay;
                ay = textArea.getText();
                if (ay.isEmpty())
                {
                    return;
                }
                refresh(ay);
            }

            void actionPerformed(ActionEvent e)
            {
                FileSelection selection = new FileSelection();
                int i = selection.showOpenDialog(this);
                if (i == FileSelection.APPROVE_OPTION)
                {
                    File selectedFile = selection.getSelectedFile();
                    if (selectedFile.isFile())
                    {
                        BufferedReader reader = null;
                        CharBuffer buffer;
                        String text;
                        buffer = CharBuffer.allocate((int) selectedFile.length());
                        try
                        {
                            reader = new BufferedReader(new FileReader(selectedFile));
                            reader.read(buffer);
                            buffer.flip();
                            text = new String(buffer.array()).trim();
                            refresh(text);
                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                        } finally
                        {
                            Close.close(reader);
                        }

                    }

                }

            }

            private void refresh(final String text)
            {
                String character, surround;
                int i;

                i = comboBox.getSelectedIndex();
                character = as[i];
                if (character == null || character.isEmpty())
                {
                    win.popUps(this, "请选择分割符");
                    return;
                }
                surround = Text.surround(text, character, new String[]{"[", "]"});
                if (surround == null)
                {
                    surround = text;
                }
                textArea.setText(surround);
            }

        }

        private class PopupReplaceWindow extends JDialog
        {
            /**
             *
             */
            private static final long serialVersionUID = 7475440184952331358L;
            JLabel label, label1, label2, label3, label4;
            JTextField field, field1;
            JCheckBox box;
            JButton button, button1, button2, button3;
            JPanel panel, panel1, panel2, panel3;

            PopupReplaceWindow(Frame owner, String title, boolean modal)
            {
                super(owner, title, modal);
                setSize(386, 178);
                setResizable(false);
                setLocationRelativeTo(win);
                Container contentPane = getContentPane();
                label = new JLabel("查找内容:");
                label1 = new JLabel("替换为：");
                label2 = new JLabel();
                label3 = new JLabel();
                label4 = new JLabel();
                field = new JTextField();
                field1 = new JTextField();
                box = new JCheckBox("区分大小写");
                button = new JButton("查找下一个");
                button1 = new JButton("替换");
                button2 = new JButton("全部替换");
                button3 = new JButton("取消");

                panel = new JPanel();
                panel1 = new JPanel();
                panel2 = new JPanel();
                panel3 = new JPanel();

                contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                panel.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 5));
                panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
                panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 0));

                label.setPreferredSize(new Dimension(75, 20));
                label1.setPreferredSize(new Dimension(75, 20));
                field.setPreferredSize(new Dimension(165, 20));
                field1.setPreferredSize(new Dimension(165, 20));
                button.setPreferredSize(new Dimension(110, 20));
                button1.setPreferredSize(new Dimension(110, 20));

                panel.setPreferredSize(new Dimension(380, 56));
                panel1.setPreferredSize(new Dimension(380, 94));
                panel2.setPreferredSize(new Dimension(140, 90));
                panel3.setPreferredSize(new Dimension(140, 90));

                label2.setPreferredSize(new Dimension(130, 30));
                label3.setPreferredSize(new Dimension(98, 90));
                label4.setPreferredSize(new Dimension(110, 5));
                box.setPreferredSize(new Dimension(130, 20));
                button2.setPreferredSize(new Dimension(110, 20));
                button3.setPreferredSize(new Dimension(110, 20));

                panel.add(label);
                panel.add(field);
                panel.add(button);
                panel.add(label1);
                panel.add(field1);
                panel.add(button1);

                panel2.add(label2);
                panel2.add(box);

                panel3.add(button2);
                panel3.add(label4);
                panel3.add(button3);

                panel1.add(panel2);
                panel1.add(label3);
                panel1.add(panel3);

                contentPane.add(panel);
                contentPane.add(panel1);

                addMonitor();

            }

            private void addMonitor()
            {
                this.button.addActionListener(new SeekMonitor(null));
                this.button1.addActionListener(new SeekMonitor(Style.REPLACING_A));
                this.button2.addActionListener(new SeekMonitor(Style.ALL_REPLACEMENT));
                this.button3.addActionListener(this::actionPerformed);

            }

            void actionPerformed(ActionEvent e)
            {
                this.dispose();
            }

            class SeekMonitor implements ActionListener
            {
                Style ReplaceStyle;

                SeekMonitor(Style ReplaceStyle)
                {
                    this.ReplaceStyle = ReplaceStyle;

                }

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    final int selectionEnd, position, location, start;
                    final boolean isSelect;
                    String data, text, data_1;
                    char[] charArray;

                    isSelect = box.isSelected();
                    text = win.textArea.getText();
                    data = field.getText();
                    data_1 = field1.getText();

                    selectionEnd = win.textArea.getSelectionEnd();
                    if (text.isEmpty())
                    {
                        win.popUps(win, "找不到" + data);
                        return;
                    }
                    if (selectionEnd == 0)
                    {
                        position = win.textArea.getCaretPosition();
                        start = position;
                    }
                    else
                    {
                        if (ReplaceStyle == Style.REPLACING_A)
                        {
                            win.textArea.replaceSelection(data_1);
                            position = win.textArea.getSelectionStart() + data_1.length();
                        }
                        else
                        {
                            position = selectionEnd;
                        }
                        start = win.textArea.getSelectionStart();
                    }
                    if (!isSelect)
                    {
                        text = win.textArea.getText().toUpperCase();
                        data = data.toUpperCase();
                    }
                    location = text.indexOf(data, position);
                    if (location == -1)
                    {
                        win.popUps(win, "找不到" + data);
                        return;
                    }
                    win.textArea.requestFocus();
                    if (ReplaceStyle != Style.ALL_REPLACEMENT)
                    {
                        win.textArea.select(location, location + data.length());
                    }

                    if (ReplaceStyle == Style.ALL_REPLACEMENT)
                    {
                        charArray = text.substring(start).toCharArray();
                        int[][] ints = MyFile.seekChars(charArray, data.toCharArray(), 0);
                        char[] chars = new char[charArray.length + 1000000];
                        MyFile.replace(chars, charArray, new String[]{data_1}, ints);
                        win.textArea.replaceRange(new String(chars).trim(), start, win.textArea.getText().length());
                    }

                }

            }

        }

        // 设置
        private class PopupSetCatalogue extends JDialog
        {

            private static final long serialVersionUID = 1L;
            Container contentPane;
            JFormattedTextField field, field_1;
            JLabel label, label_1, label_2, label_3;
            JButton button, button_1;
            TextArea area;
            JPanel panel, panel_1, panel_2;

            PopupSetCatalogue(Frame owner, String title, boolean modal, String text, int i, int length)
            {
                super(owner, title, modal);
                setSize(370, 249);
                setLocationRelativeTo(win);
                setResizable(false);
                contentPane = getContentPane();
                FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
                contentPane.setLayout(layout);
                final NumberFormat integerInstance = NumberFormat.getIntegerInstance();
                final NumberFormat integerInstance1 = NumberFormat.getIntegerInstance();
                integerInstance.setGroupingUsed(false);
                integerInstance1.setGroupingUsed(false);
                field = new JFormattedTextField(integerInstance);
                field_1 = new JFormattedTextField(integerInstance1);
                label = new JLabel("起始位置：");
                label_1 = new JLabel("文本长度：");
                label_2 = new JLabel("查找内容：");
                label_3 = new JLabel();
                button = new JButton("确认");
                button_1 = new JButton("取消");
                area = new TextArea(text);
                panel = new JPanel();
                panel_1 = new JPanel();
                panel_2 = new JPanel();

                field.setText(String.valueOf(i));
                field_1.setText(String.valueOf(length));

                panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 12));
                panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 5));
                panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

                panel.setPreferredSize(new Dimension(364, 185));
                panel_1.setPreferredSize(new Dimension(364, 35));
                label.setPreferredSize(new Dimension(70, 20));
                field.setPreferredSize(new Dimension(80, 20));
                label_3.setPreferredSize(new Dimension(1, 1));
                label_1.setPreferredSize(new Dimension(70, 20));
                field_1.setPreferredSize(new Dimension(80, 20));
                label_2.setPreferredSize(new Dimension(80, 20));
                panel_2.setPreferredSize(new Dimension(80, 130));
                area.setPreferredSize(new Dimension(250, 130));
                button.setPreferredSize(new Dimension(60, 25));
                button_1.setPreferredSize(new Dimension(60, 25));

                area.setFont(new Font("微软雅黑", Font.PLAIN, 12));

                panel_2.add(label_2);

                panel.add(label);
                panel.add(field);
                panel.add(label_3);
                panel.add(label_1);
                panel.add(field_1);
                panel.add(panel_2);
                panel.add(area);

                panel_1.add(button);
                panel_1.add(button_1);

                //                panel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                //                panel_1.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                //                panel_2.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));

                contentPane.add(panel);
                contentPane.add(panel_1);

                button_1.addActionListener(this::actionPerformed);
                button.addActionListener(this::actionPerformed1);
                addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        dispose();
                    }
                });

            }

            private void actionPerformed1(ActionEvent actionEvent)
            {
                if (win.textArea.getDocument().getLength() == 0)
                {
                    JOptionPane.showMessageDialog(this, "请打开文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String text, text_1, text_2;
                String[] textArray;
                int i, length;
                int[][] ints;

                text = field.getText().trim();
                text_1 = field_1.getText().trim();
                text_2 = area.getText();
                i = text.isEmpty() ? 0 : Integer.parseInt(text);
                if (!field.isEditValid() || !field_1.isEditValid())
                {
                    JOptionPane.showMessageDialog(this, "您输入了不合法的内容", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (text_1.isEmpty() || text_2.isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "内容不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                length = Integer.parseInt(text_1);
                if (length <= 0)
                {
                    JOptionPane.showMessageDialog(this, "文本长度必须大于0", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                textArray = Test.seek(text_2.toCharArray(), '[', ']');
                if (textArray.length == 0)
                {
                    JOptionPane.showMessageDialog(this, "格式错误", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                ints = MyFile.seeks(win.textArea.getText().toCharArray(), textArray, i, length);
                if (ints.length == 0)
                {
                    JOptionPane.showMessageDialog(this, "找不到此目录", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String[] strings = Text.readToArray(win.textArea.getText().toCharArray(), ints);
                win.setModel(strings, ints);
                Menu.this.textArrays = textArray;
                Menu.this.i1 = i;
                Menu.this.lengths = length;
                Menu.this.text = text_2;
                this.dispose();
            }

            void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        }

        private class PopupSeekWindow extends JDialog
        {
            private static final long serialVersionUID = 1L;
            final JLabel label, label_1;
            final JButton button, button_1;
            final JPanel panel, panel_1, panel_2;
            final JCheckBox box;
            final JRadioButton radioButton, radioButton_1;
            final JTextField field;
            Window win = Window.this;
            ButtonGroup group;

            PopupSeekWindow(Frame owner, String title, boolean modal)
            {
                super(owner, title, modal);
                Container contentPane = getContentPane();
                FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
                contentPane.setLayout(layout);
                setSize(400, 140);
                setLocationRelativeTo(win);
                setResizable(false);
                this.label = new JLabel("查找内容：");
                this.label_1 = new JLabel();
                this.button = new JButton("查找下一个");
                this.button_1 = new JButton("取消");
                this.panel = new JPanel();
                this.panel_1 = new JPanel();
                this.panel_2 = new JPanel();
                this.box = new JCheckBox("区分大小写");
                this.radioButton = new JRadioButton("向上");
                this.radioButton_1 = new JRadioButton("向下");
                this.radioButton_1.setSelected(true);
                this.field = new JTextField();
                this.group = new ButtonGroup();

                this.label.setPreferredSize(new Dimension(84, 19));
                this.label_1.setPreferredSize(new Dimension(84, 40));
                this.field.setPreferredSize(new Dimension(184, 19));
                this.button.setPreferredSize(new Dimension(100, 19));
                this.panel_1.setPreferredSize(new Dimension(110, 76));
                this.panel.setPreferredSize(new Dimension(158, 50));

                this.panel_2.setPreferredSize(new Dimension(100, 76));
                this.box.setPreferredSize(new Dimension(90, 20));
                this.button_1.setPreferredSize(new Dimension(100, 19));
                this.radioButton.setPreferredSize(new Dimension(54, 17));
                this.radioButton_1.setPreferredSize(new Dimension(54, 17));

                this.button.setMnemonic(KeyEvent.VK_F);
                this.box.setMnemonic(KeyEvent.VK_C);
                this.radioButton.setMnemonic(KeyEvent.VK_U);
                this.radioButton_1.setMnemonic(KeyEvent.VK_D);

                this.group.add(radioButton);
                this.group.add(radioButton_1);

                panel.setLayout(new FlowLayout());
                panel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "方向", TitledBorder.LEFT,
                                TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 12), Color.CYAN));
                panel.add(radioButton);
                panel.add(radioButton_1);

                panel_1.setLayout(new FlowLayout(FlowLayout.LEFT));
                panel_1.add(label_1);
                panel_1.add(box);

                panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 8));
                panel_2.add(button_1);

                contentPane.add(label);
                contentPane.add(field);
                contentPane.add(button);
                contentPane.add(panel_1);
                contentPane.add(panel);
                contentPane.add(panel_2);

                this.button.addActionListener(new SeekMonitor());
                this.button_1.addActionListener(this::actionPerformed);
                this.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        dispose();
                    }
                });
            }

            /**
             * 取消键监听
             *
             * @param e 1
             */
            private void actionPerformed(ActionEvent e)
            {
                dispose();
            }

            /**
             * the is Monitor of find next
             */
            class SeekMonitor implements ActionListener
            {
                PopupSeekWindow popupSeekWindow = PopupSeekWindow.this;

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String text, fieldText;
                    int i, position;
                    boolean isUp = false;

                    text = win.textArea.getText();
                    fieldText = popupSeekWindow.field.getText();

                    if (radioButton.isSelected())
                    {
                        isUp = true;
                    }
                    if (text.isEmpty() || fieldText.isEmpty())
                    {
                        JOptionPane.showMessageDialog(PopupSeekWindow.this, "找不到" + fieldText, "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if (isUp)
                    {
                        i = win.textArea.getSelectionStart() - 1;
                    }
                    else
                    {
                        i = win.textArea.getSelectionEnd();
                    }

                    if (!box.isSelected())
                    {
                        text = text.toUpperCase();
                        fieldText = fieldText.toUpperCase();
                    }

                    if (isUp)
                    {
                        position = text.lastIndexOf(fieldText, i);
                    }
                    else
                    {
                        position = text.indexOf(fieldText, i);
                    }
                    if (position == -1)
                    {
                        JOptionPane.showMessageDialog(PopupSeekWindow.this, "找不到" + fieldText, "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    win.textArea.requestFocus();
                    win.textArea.select(position, position + fieldText.length());

                }
            }

        }

        /**
         * 设置字体
         */
        private class PopupSetFontWindow extends JDialog
        {

            private static final long serialVersionUID = 1L;
            final JLabel label, label1, label2, label_3, jLabel, jLabel_1;
            final JTextField fontText, styleText, textSize;
            final JList<String> list, list_1, list_2;
            final JScrollPane fontPane, stylePane, sizePane;
            final JButton okButton, cancel;
            final JComboBox<String> comboBox;
            Window win = Window.this;
            Font font;
            int i;

            PopupSetFontWindow(Frame owner, String title, boolean modal)
            {

                super(owner, title, modal);
                setSize(400, 520);
                setLocationRelativeTo(win);
                setResizable(false);
                Container contentPane = getContentPane();
                FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
                flowLayout.setHgap(0);
                flowLayout.setVgap(0);
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                contentPane.setLayout(flowLayout);
                label = new JLabel("  字体");
                label.setPreferredSize(new Dimension(165, 20));
                label1 = new JLabel(" 字形");
                label1.setPreferredSize(new Dimension(140, 20));
                label2 = new JLabel("大小");
                label2.setPreferredSize(new Dimension(30, 20));
                label_3 = new JLabel("AbAdeNf");
                jLabel = new JLabel();
                jLabel.setPreferredSize(new Dimension(170, 1));
                jLabel_1 = new JLabel("脚本：");
                jLabel_1.setPreferredSize(new Dimension(190, 20));

                fontText = new JTextField(14);
                styleText = new JTextField(12);
                textSize = new JTextField(4);
                okButton = new JButton("确认");
                cancel = new JButton("取消");
                this.list = new JList<>();
                this.list_1 = new JList<>();
                this.list_2 = new JList<>();
                this.fontPane = new JScrollPane(list);
                this.fontPane.setPreferredSize(new Dimension(158, 150));
                this.stylePane = new JScrollPane(list_1);
                this.stylePane.setPreferredSize(new Dimension(136, 130));
                this.sizePane = new JScrollPane(list_2);
                this.sizePane.setPreferredSize(new Dimension(48, 100));
                comboBox = new JComboBox<>();
                comboBox.setPreferredSize(new Dimension(190, 20));
                comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"西欧语言", "中文"}));
                JPanel jPanel = new JPanel();
                jPanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "示例", TitledBorder.LEFT,
                                TitledBorder.TOP, new Font("微软雅黑", Font.PLAIN, 12), Color.CYAN));
                jPanel.setLayout(new GridLayout(1, 1));
                // this.setPane(jPanel, 10, 10, 190, 80, FlowLayout.CENTER);
                jPanel.setPreferredSize(new Dimension(190, 80));
                jPanel.add(label_3);
                String[] value = new String[]{"仿宋", "宋体", "微软雅黑", "楷体", "黑体", "新宋体"};
                String[] value_1 = new String[]{"常规", "倾斜", "粗体", "粗体 倾斜"};
                String[] value_2 = new String[73];
                for (int i = 5; i < 78; i++)
                {
                    value_2[i - 5] = String.valueOf(i);
                }

                this.addModel(list, value);
                this.addModel(list_1, value_1);
                this.addModel(list_2, value_2);

                font = win.textArea.getFont();
                String fontName = font.getFontName();
                fontText.setText(fontName);
                fontText.selectAll();
                ListModel<String> model = list.getModel();
                for (int i = 0; i < model.getSize(); i++)
                {
                    if (fontName.equals(model.getElementAt(i)))
                    {
                        list.setSelectedIndex(i);
                        break;
                    }
                }

                int stile = font.getStyle();
                switch (stile)
                {
                    case Font.PLAIN:
                        this.styleText.setText("常规");
                        this.list_1.setSelectedIndex(0);
                        break;
                    case Font.BOLD:
                        this.styleText.setText("粗体");
                        this.list_1.setSelectedIndex(1);
                        break;
                    case Font.ITALIC:
                        this.styleText.setText("斜体");
                        this.list_1.setSelectedIndex(2);
                        break;
                    case Font.BOLD + Font.ITALIC:
                        this.styleText.setText("粗体 倾斜");
                        this.list_1.setSelectedIndex(3);
                        break;
                    default:
                        break;
                }
                this.styleText.selectAll();
                String values = String.valueOf(font.getSize());
                this.textSize.setText(values);
                this.textSize.selectAll();
                ListModel<String> model1 = this.list_2.getModel();
                for (int i = 0; i < model1.getSize(); i++)
                {
                    if (values.equals(model1.getElementAt(i)))
                    {
                        this.list_2.setSelectedIndex(i);
                        break;
                    }
                }

                JPanel panel = new JPanel();
                JPanel pane2 = new JPanel();
                JPanel pane3 = new JPanel();
                JPanel pane4 = new JPanel();

                this.setPane(panel, 10, 0, 394, 30, FlowLayout.LEFT);
                panel.add(label);
                panel.add(label1);
                panel.add(label2);

                this.setPane(pane2, 12, 0, 394, 200, FlowLayout.LEFT);
                pane2.add(fontText);
                pane2.add(styleText);
                pane2.add(textSize);
                pane2.add(this.fontPane);
                pane2.add(this.stylePane);
                pane2.add(this.sizePane);

                this.setPane(pane3, 12, 4, 210, 211, FlowLayout.LEFT);
                pane3.add(jPanel);
                // pane3.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                pane3.add(jLabel_1);
                pane3.add(comboBox);

                this.setPane(pane4, 20, 12, 394, 50, FlowLayout.RIGHT);
                // pane4.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
                pane4.add(okButton);
                pane4.add(cancel);

                contentPane.add(panel);
                contentPane.add(pane2);
                contentPane.add(jLabel);
                contentPane.add(pane3);
                contentPane.add(pane4);

                cancel.addActionListener(this::actionPerformed);
                comboBox.addActionListener(this::actionPerformed1);
                list.addListSelectionListener(this::actionPerformed_2);
                list_1.addListSelectionListener(this::actionPerformed_2);
                list_2.addListSelectionListener(this::actionPerformed_2);
                okButton.addActionListener(this::actionPerformed3);
                this.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                        dispose();
                    }
                });
            }

            private void actionPerformed_2(ListSelectionEvent listSelectionEvent)
            {
                i++;
                if (i == 2)
                {
                    i = 0;
                    String selectedValue = list.getSelectedValue();
                    String selectedValue1 = list_1.getSelectedValue();
                    String selectedValue2 = list_2.getSelectedValue();
                    this.fontText.setText(selectedValue);
                    this.styleText.setText(selectedValue1);
                    this.textSize.setText(selectedValue2);
                    int s = font.getStyle();
                    switch (selectedValue1)
                    {
                        case "常规":
                            s = Font.PLAIN;
                            break;
                        case "倾斜":
                            s = Font.ITALIC;
                            break;
                        case "粗体":
                            s = Font.BOLD;
                            break;
                        case "粗体 倾斜":
                            s = Font.ITALIC + Font.BOLD;
                            break;
                        default:
                            break;
                    }

                    this.label_3.setFont(new Font(selectedValue, s, Integer.parseInt(selectedValue2.trim())));

                }

            }

            private void setPane(JPanel pane, int level, int vertical, int width, int height, int align)
            {
                FlowLayout layout = new FlowLayout(align);
                layout.setAlignOnBaseline(true);
                layout.setHgap(level);
                layout.setVgap(vertical);
                pane.setLayout(layout);
                pane.setPreferredSize(new Dimension(width, height));

            }

            private void addModel(JList<String> list, String[] values)
            {
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setModel(new AbstractListModel<String>()
                {
                    private static final long serialVersionUID = 1L;
                    final String[] value = values;

                    @Override
                    public int getSize()
                    {
                        return value.length;
                    }

                    @Override
                    public String getElementAt(int index)
                    {
                        return value[index];
                    }
                });
            }

            /**
             * 取消键监听
             *
             * @param e 1
             */
            private void actionPerformed(ActionEvent e)
            {
                dispose();
            }

            /**
             * 确认键监听
             *
             * @param e 1
             */
            private void actionPerformed3(ActionEvent e)
            {
                win.textArea.setFont(this.label_3.getFont());
                dispose();
            }

            private void actionPerformed1(ActionEvent e)
            {
                int select = comboBox.getSelectedIndex();
                if (select == 0)
                {
                    this.label_3.setText("AbAdeNf");
                }
                else
                {
                    this.label_3.setText("中国制造");
                }
            }

        }

        // menu
        private class PopupMenu extends MouseAdapter implements ActionListener
        {
            JPopupMenu menu;
            JMenuItem item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9, item_10, item_11, item_12,
                    item_13, item_14, item_15, item_16;
            Menu menus = Menu.this;
            JMenu jMenu;

            PopupMenu()
            {
                this.menu = new JPopupMenu();
                this.jMenu = menus.newMenu("编码");
                this.jMenu.setPreferredSize(new Dimension(50, 25));

                this.item_1 = menus.newItem("撤销");
                this.item_2 = menus.newItem("重做");
                this.item_3 = menus.newItem("剪切");
                this.item_4 = menus.newItem("复制");
                this.item_5 = menus.newItem("粘贴");
                this.item_6 = menus.newItem("删除");
                this.item_7 = menus.newItem("全选");
                this.item_8 = menus.newItem("UTF-8");
                this.item_9 = menus.newItem("GBK");
                this.item_10 = menus.newItem("Unicode");
                this.item_11 = menus.newItem("ASCII");
                this.item_12 = menus.newItem("GB2312");
                this.item_13 = menus.newItem("UTF-32");
                this.item_14 = menus.newItem("US-ASCII");
                this.item_15 = menus.newItem("GB18030");
                this.item_16 = menus.newItem("ISO-8859-1");

                /*




                 */

                this.jMenu.add(this.item_8);
                this.jMenu.add(this.item_9);
                this.jMenu.add(this.item_10);
                this.jMenu.add(this.item_11);
                this.jMenu.add(this.item_12);
                this.jMenu.add(this.item_13);
                this.jMenu.add(this.item_14);
                this.jMenu.add(this.item_15);

                this.menu.add(this.item_1);
                this.menu.add(this.item_2);
                this.menu.addSeparator();
                this.menu.add(this.item_3);
                this.menu.add(this.item_4);
                this.menu.add(this.item_5);
                this.menu.add(this.item_6);
                this.menu.addSeparator();
                this.menu.add(jMenu);
                this.menu.addSeparator();
                this.menu.add(this.item_7);

                this.addMonitor();
            }

            private void addMonitor()
            {
                win.textArea.addMouseListener(this);
                this.item_1.addActionListener(menus::actionPerformed4);
                this.item_2.addActionListener(menus::actionPerformed_14);
                this.item_3.addActionListener(menus::actionPerformed_18);
                this.item_4.addActionListener(menus::actionPerformed_15);
                this.item_5.addActionListener(menus::actionPerformed_16);
                this.item_6.addActionListener(menus::actionPerformed_17);
                this.item_7.addActionListener(menus::actionPerformed_19);
                this.item_8.addActionListener(this);
                this.item_9.addActionListener(this);
                this.item_10.addActionListener(this);
                this.item_11.addActionListener(this);
                this.item_12.addActionListener(this);
                this.item_13.addActionListener(this);
                this.item_14.addActionListener(this);
                this.item_15.addActionListener(this);
                this.item_16.addActionListener(this);

            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String coding = win.encoder;

                if (e.getSource() == this.item_8)
                {
                    coding = "UTF-8";

                }
                else if (e.getSource() == this.item_9)
                {
                    coding = "GBK";

                }
                else if (e.getSource() == this.item_10)
                {
                    coding = "unicode";

                }
                else if (e.getSource() == this.item_11)
                {
                    coding = "ASCII";

                }
                else if (e.getSource() == this.item_12)
                {
                    coding = "GB2312";

                }
                else if (e.getSource() == this.item_13)
                {

                    coding = "UTF-32";

                }
                else if (e.getSource() == this.item_14)
                {
                    coding = "US-ASCII";

                }
                else if (e.getSource() == this.item_15)
                {
                    coding = "GB18030";

                }
                else if (e.getSource() == this.item_16)
                {
                    coding = "ISO-8859-1";
                }
                if (coding.equals(win.encoder))
                {
                    return;
                }
                try
                {
                    // String encode = URLEncoder.encode(win.textArea.getText(), coding);
                    byte[] bytes = win.textArea.getText().getBytes(coding);
                    win.textArea.setText(new String(bytes, coding));
                    win.encoder = coding;
                } catch (UnsupportedEncodingException ex)
                {
                    ex.printStackTrace();
                }
            }

            private void checkIfEnabled()
            {
                String text = win.textArea.getSelectedText();
                if (text == null)
                {
                    this.item_3.setEnabled(false);
                    this.item_4.setEnabled(false);
                    this.item_6.setEnabled(false);
                }
                else
                {
                    this.item_3.setEnabled(true);
                    this.item_4.setEnabled(true);
                    this.item_6.setEnabled(true);
                }
                this.item_1.setEnabled(revocation.isUndo());
                this.item_2.setEnabled(revocation.isRedo());

                item_5.setEnabled(systemClipboard.getContents(this) != null);
                this.item_7.setEnabled(!win.textArea.getText().isEmpty());
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                final int number = 3;
                if (e.getButton() == number)
                {
                    checkIfEnabled();
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        }

        public class PopupWhether extends JDialog
        {

            private static final long serialVersionUID = 1L;
            JTextArea area1;
            JButton button, button_1, button_2;
            JPanel panel;
            Frame owner;

            PopupWhether(Frame owner, String title)
            {
                super(owner, title, true);
                this.owner = owner;
                this.setSize(360, 170);
                this.setResizable(false);
                this.setLocationRelativeTo(owner);

                Container contentPane = this.getContentPane();
                this.area1 = new JTextArea();
                this.button = new JButton("保存");
                this.button_1 = new JButton("不保存");
                this.button_2 = new JButton("取消");
                this.panel = new JPanel();
                this.button.requestFocusInWindow();

                contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
                this.panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 7));
                this.area1.setPreferredSize(new Dimension(354, 105));
                this.panel.setPreferredSize(new Dimension(354, 37));
                this.button.setPreferredSize(new Dimension(60, 22));
                this.button_1.setPreferredSize(new Dimension(70, 22));
                this.button_2.setPreferredSize(new Dimension(60, 22));

                this.area1.setFont(new Font("宋体", Font.PLAIN, 15));
                this.area1.setEditable(false);
                this.area1.setLineWrap(true);

                this.panel.add(button);
                this.panel.add(button_1);
                this.panel.add(button_2);

                contentPane.add(this.area1);
                contentPane.add(this.panel);

                //                this.panel.setBorder(new LineBorder(Color.magenta, 1));
                //                this.area1.setBorder(new LineBorder(Color.magenta, 1));
                this.monitor();

            }

            // monitor
            private void monitor()
            {
                this.button.addActionListener(this::actionPerformed);
                this.button_1.addActionListener(this::actionPerformed_1);
                this.button_2.addActionListener(this::actionPerformed_2);
            }

            private void actionPerformed_2(ActionEvent actionEvent)
            {
                this.dispose();
            }

            private void actionPerformed_1(ActionEvent actionEvent)
            {
                this.owner.dispose();
            }

            void actionPerformed(ActionEvent e)
            {
                FileChannel channel = null;
                try
                {
                    if (win.textArea.getDocument().getLength() == 0)
                    {
                        return;
                    }
                    if (win.file == null)
                    {
                        FileSelection1 fileSelection1 = new FileSelection1();
                        int i = fileSelection1.showOpenDialog(win);
                        if (i == JFileChooser.APPROVE_OPTION)
                        {
                            win.file = fileSelection1.getSelectedFile();
                        }
                        if (i == JFileChooser.CANCEL_OPTION)
                        {
                            return;
                        }
                    }
                    ByteBuffer wrap = ByteBuffer.wrap(win.textArea.getText().getBytes());
                    channel = FileChannel.open(Paths.get(win.file.getPath()), StandardOpenOption.WRITE);
                    wrap.flip();
                    channel.write(wrap);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                } finally
                {
                    try
                    {
                        win.dispose();
                        if (channel != null)
                        {
                            channel.close();
                        }
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }

            }

            @Override
            public void setName(String name)
            {
                this.area1.setText(name);
            }
        }

        /**
         * 窗口关闭侦听
         */
        private class WindowAdapters extends WindowAdapter
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println(e.getOldState());
                if (e.getSource() == win)
                {
                    String path;
                    if (win.textArea.getDocument().getLength() == 0)
                    {
                        win.dispose();
                        return;
                    }
                    if (win.file == null)
                    {
                        path = "\n是否更改保存到  无标题？";
                    }
                    else
                    {
                        path = "\n 是否将更改保存到\n " + win.file.getPath();
                    }

                    dialog7.setName(path);
                    dialog7.setVisible(true);
                }
            }

            @Override
            public void windowClosed(WindowEvent e)
            {

            }
        }

        private class RightClickMenu extends MouseAdapter
        {
            Window win = Window.this;
            Menu menus = Menu.this;
            JPopupMenu menu;
            JMenuItem item, item_1, item_2, item_3;
            int i;

            private RightClickMenu()
            {
                menu = new JPopupMenu();
                select = new ArrayList<>(1000);
                item = menus.newItem("刷新目录");
                item_1 = menus.newItem("全选");
                item_2 = menus.newItem("选择");
                item_3 = menus.newItem("取消");

                menu.add(item);
                menu.addSeparator();
                menu.add(item_1);
                menu.add(item_2);
                menu.add(item_3);

                this.i = win.textArea.getText().length();
                addMonitor();

            }

            private void addMonitor()
            {
                list.addMouseListener(this);
                item.addActionListener(this::itemMonitor);
                win.textArea.addCaretListener(this::caretUpdate);
                item_2.addActionListener(this::itemMonitor_1);
                item_1.addActionListener(this::itemMonitor2);
                item_3.addActionListener(this::itemMonitor3);

            }

            private void itemMonitor3(ActionEvent actionEvent)
            {
                int[] ints;
                String text, text1, text3;

                ints = list.getSelectedIndices();
                if (ints.length == 0)
                {
                    return;
                }
                for (int i : ints)
                {
                    text = win.model.get(i);
                    text3 = text.substring(0, 1);
                    if ("√".equals(text3))
                    {
                        text1 = text.substring(1);
                        win.model.setElementAt(text1, i);
                        select.remove((Integer) i);
                    }
                }
            }

            private void itemMonitor2(ActionEvent actionEvent)
            {
                if (win.ints == null)
                {
                    return;
                }
                int i1;

                i1 = list.getSelectedIndex();
                list.setSelectionInterval(0, win.model.getSize() - 1);
                win.textArea.setCaretPosition(i1);
            }

            private void itemMonitor_1(ActionEvent actionEvent)
            {
                int[] ints = list.getSelectedIndices();
                String string, s;
                if (ints.length == 0)
                {
                    return;
                }
                for (int i : ints)
                {
                    string = win.model.get(i);
                    s = string.substring(0, 1);
                    if ("√".equals(s))
                    {
                        continue;
                    }
                    win.model.setElementAt("√" + string.trim(), i);
                    select.add(i);
                }
            }

            void caretUpdate(CaretEvent e)
            {
                int length = win.textArea.getText().length();
                if (length == 0 || ints == null || ints.length == 0)
                {
                    return;
                }
                if (menus.isSelect)
                {
                    if (i != length)
                    {
                        this.itemMonitor(null);
                        i = length;
                    }
                }
            }

            /**
             * 刷新目录
             *
             * @param actionEvent 1
             */
            private void itemMonitor(ActionEvent actionEvent)
            {
                String text;
                String[] textArray, strings;
                int i, length;

                text = win.textArea.getText();
                textArray = menus.textArrays;
                i = menus.i1;
                length = menus.lengths;
                if (text.isEmpty())
                {
                    win.popUps(win.list, "请打开文本");
                    return;
                }
                if (textArray == null || textArray.length == 0 || length == 0)
                {
                    win.popUps(win.list, "请先设置目录");
                    return;
                }
                int[][] seeks = MyFile.seeks(text.toCharArray(), textArray, i, length);
                if (seeks.length == 0)
                {
                    win.popUps(win.list, "找不到目录");
                    return;
                }

                strings = Text.readToArray(win.textArea.getText().toCharArray(), seeks);
                win.setModel(strings, seeks);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == 3)//如果是鼠标右键点击
                {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        }

    }

}