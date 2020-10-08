package hu;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * @author Administrator
 */
public class RJTextArea extends JTextArea
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final StringBuilder builder;
    private final Monitor monitor;
    private final MyStack<String> text;
    private final MyStack<int[]> cooed;


    RJTextArea(String text)
    {
        this.builder = new StringBuilder();
        this.monitor = new Monitor();
        this.text = new MyStack<>();
        this.cooed = new MyStack<>();
        this.getDocument().addDocumentListener(monitor);
        this.setText(text);

    }


    private void add(String text, int position, int length)
    {
        this.text.pouch(text);
        this.cooed.pouch(new int[]{position, length});
    }

    /**
     * 撤销
     */
    public void undo()
    {
        final int offset, length;
        final String text;
        this.monitor.flush();
        if (this.text.empty())
        {
            this.whetherMonitor(false);
            int[] pop = this.cooed.pop();
            offset = pop[0];
            length = pop[1];
            text = this.text.pop();
            if (length > 0)
            {
                this.replaceRange("", offset, offset + length);
            }
            else
            {
                this.insert(text, offset);
            }
            this.whetherMonitor(true);
            this.renewText();
        }
    }

    /**
     * 重置
     */
    public void reset() throws BadLocationException
    {
        this.whetherMonitor(false);
        System.out.println(monitor == null);
        assert monitor != null;
        monitor.flush();
        this.builder.delete(0, this.builder.length());
        this.cooed.clear();
        this.text.clear();
        this.getDocument().remove(0, this.getText().length());
        this.whetherMonitor(true);
    }


    /**
     * 重做
     */
    public void redo()
    {
        final int offset, length;
        final String text;
        if (this.text.isTop())
        {
            this.monitor.flush();
            this.whetherMonitor(false);
            int[] at = this.cooed.at(cooed.getIndex());
            offset = at[0];
            length = at[1];
            text = this.text.at(this.text.getIndex());
            this.text.inc();
            this.cooed.inc();
            if (length > 0)
            {
                this.insert(text, offset);
            }
            else
            {
                this.replaceRange("", offset, offset + Math.abs(length));
            }
            this.renewText();
            this.whetherMonitor(true);
        }
    }


    /**
     * 是否还能重做
     *
     * @return 1
     */
    public boolean isRedo()
    {
        return this.cooed.isTop();
    }

    /**
     * 是否还能撤销
     *
     * @return 1
     */
    public boolean isUndo()
    {
        this.monitor.flush();
        return this.cooed.empty();
    }


    /**
     * 更新
     */
    public void renewText()
    {
        builder.replace(0, builder.length(), this.getText());
    }

    /**
     * 开启或关闭侦听
     *
     * @param is 1
     */
    public void whetherMonitor(boolean is)
    {
        if (is)
        {
            this.getDocument().addDocumentListener(monitor);
        }
        else
        {
            this.getDocument().removeDocumentListener(monitor);
        }
    }

    public void setText(String text, boolean a)
    {
        if (a)
        {
            setText(text);
        }
        else
        {
            super.setText(text);
        }
    }

    @Override
    public void setText(String t)
    {
        try
        {

            this.reset();
            this.whetherMonitor(false);
            super.setText(t);
            this.renewText();
            this.whetherMonitor(true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class Monitor implements DocumentListener
    {
        final StringBuilder builder1;
        int length, location;
        int record;
        int mark = -1;

        public Monitor()
        {
            this.builder1 = new StringBuilder();
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            final String text, s;
            final int length, offset;

            length = e.getLength();
            offset = e.getOffset();
            text = RJTextArea.this.getText();
            s = text.substring(offset, offset + length);

            if (length == 1)
            {
                if (this.record == -1 || this.record == 0 || this.location == -1)
                {
                    if (this.record == -1)
                    {
                        this.flush();
                    }
                    this.record = 1;
                    this.mark = offset;
                    this.location = offset;
                }

                if (this.mark == offset)
                {
                    this.mark++;
                }
                else
                {
                    this.flush();
                    this.mark = offset + 1;
                    this.location = offset;
                }
                this.builder1.append(s);
                this.length++;
            }
            else
            {
                this.flush();
                add(s, offset, length);
            }
            renewText();
        }


        @Override
        public void removeUpdate(DocumentEvent e)
        {
            final int offset, length;
            final String s;
            offset = e.getOffset();
            length = e.getLength();


            s = builder.substring(offset, offset + length);


            if (length == 1)
            {
                if (this.record == 1 || this.record == 0)
                {
                    this.record = -1;
                    this.flush();
                    this.mark = offset;
                }

                if (this.mark == offset)
                {
                    this.builder1.append(s);
                    location = offset;
                    this.mark--;
                }
                else
                {
                    this.flush();
                    this.mark = offset - 1;
                    location = offset;
                    this.builder1.append(s);
                }
                this.length--;
            }
            else
            {
                this.flush();
                add(s, offset, -length);
            }
            renewText();
        }

        public void flush()
        {
            if (this.builder1.length() != 0)
            {
                add(builder1.reverse().toString(), this.location, this.length);
                this.builder1.setLength(0);
            }
            this.length = 0;
            this.location = -1;
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {

        }
    }

}
