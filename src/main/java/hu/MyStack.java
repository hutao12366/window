package hu;

/**
 * @author Administrator
 */
public class MyStack<T>
{
    private Object[] t;
    private int position;
    private int capacity;
    private int limit;
    private int reserve;


    public MyStack(int capacity)
    {
        this.capacity = capacity;
        t = new Object[capacity];
    }


    public MyStack()
    {
        this(30);
    }

    public void clear()
    {
        this.limit = 0;
        this.position = 0;
        this.reserve = 0;
        if (t.length > 30)
        {
            this.t = null;
            t = new Object[30];
            this.capacity = 30;
        }
    }

    public boolean isTop()
    {
        return this.position != limit;
    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public void setPointer(int index)
    {
        if (index < 0 || index > this.capacity)
        {
            throw new RuntimeException("indexes out of bound");
        }
        this.position = index;
    }

    public void pouch(T t)
    {
        if (this.position == capacity)
        {
            arrayDilatation();
        }
        this.t[position] = t;
        this.position++;
        this.limit = position;

    }

    private void arrayDilatation()
    {
        int count;
        if (this.reserve != 0)
        {
            count = this.reserve;
            this.reserve = 0;
        }
        else
        {
            count = this.capacity + this.capacity >> 1;
        }
        this.capacity = count;
        Object[] object = new Object[count];
        System.arraycopy(t, 0, object, 0, t.length);
        this.t = null;
        this.t = object;
    }

    public void reserve(int size)
    {
        if (size < position)
        {
            throw new RuntimeException("Doing so can result in data loss");
        }
        this.reserve = size;

    }

    public void sub()
    {
        --this.position;
    }

    public void inc()
    {
        ++this.position;
    }

    public int getIndex()
    {
        return position;
    }

    @SuppressWarnings("unchecked")
    public T pop()
    {
        this.position--;
        return (T) this.t[position];
    }

    @SuppressWarnings("unchecked")
    public T at(int i)
    {
        return (T) this.t[i];
    }

    public boolean empty()
    {
        return this.position != 0;
    }
}
