package com.base.engine;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;


public class Window
{
	private static JFrame window;
	static JButton startButton, aboutButton;
	static JDialog about;	
        
	private BufferedImage image;
	private BufferedImage back;
	private JFrame frame;
	private Canvas canvas;
	private JPanel contentPanel;
	public Window(GameContainer gc)
	{
		window = new JFrame("请选择");
        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPanel = (JPanel) window.getContentPane();
        contentPanel.setLayout(new GridLayout(0, 2));
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(200, 200));
        back = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        about = new JDialog(window);
        aboutButton = new JButton("About the game");
       
        
        contentPanel.add(startButton);
        contentPanel.add(aboutButton);
        
        about.getContentPane().add(new JLabel("点击“Start”进入游戏界面，单击游戏界面即可开始游戏。\r\n"
        		+ "W、A、D分别代表向上、左、右移动。\r\n掉进熔浆，游戏就结束了！按“2”保存，按“4”读取。请打开音响，我们还有音乐哦！"));
		about.setSize(700, 150);
        
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
        
        
        aboutButton.addActionListener(e->{
        	about.setVisible(true);
        });
        
        
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(false);
		startButton.addActionListener(e -> {
        	window.dispose();
        	frame.setVisible(true);
        });
		
	}
	
	protected void paintComponent(Graphics g) {
        
           g.drawImage(image, 0, 0, contentPanel.getWidth(), contentPanel.getHeight(), null);
           
    }
	
	public void update()
	{
		BufferStrategy bs = canvas.getBufferStrategy();
		
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		g.setComposite(ac);
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);


		g.dispose();
		bs.show();
	}
	
	public void resize(GameContainer gc)
	{
		dispose();
		
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void dispose()
	{
		image.flush();
		frame.dispose();
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public JFrame getFrame()
	{
		return frame;
	}

	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public void setCanvas(Canvas canvas)
	{
		this.canvas = canvas;
	}
}
