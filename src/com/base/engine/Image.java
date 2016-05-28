package com.base.engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Image implements Serializable
{
	public int[] p;
	public int w;
	public int h;
	public int lb = 0;
	private String path;
	
	public Image(String path)
	{
		init(path);
	}

	private void init(String path) {
		this.path = path;
		BufferedImage image = null;

		try
		{
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		w = image.getWidth();
		h = image.getHeight();
		p = image.getRGB(0, 0, w, h, null, 0, w);

		image.flush();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeUTF(path);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		path = in.readUTF();
		init(path);
	}
}
