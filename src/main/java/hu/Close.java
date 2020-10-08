package hu;

import java.io.Closeable;
import java.io.IOException;

public class Close
{
	public static void close(Closeable... closeables)
	{
		for (Closeable s : closeables)
		{
			if (s != null)
			{
				try
				{
					s.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

}
