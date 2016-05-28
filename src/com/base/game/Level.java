package com.base.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.base.engine.AudioPlayer;
import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.ImageTile;
import com.base.engine.Input;
import com.base.engine.Light;
import com.base.engine.LightBox;
import com.base.engine.Renderer;

/**
 * Load map data( the word "Level" probably means "map")
 * Map data is stored in file "tileData.png",
 * in which one pixel represents for one block in the game.
 * Different map elements, such as ground, light, magma and enemy,
 * is represented by the color of the pixel:
 * White(0xffffff): Nothing
 * Black(0x000000): Ground
 * Red(0xff0000): Magma
 * .... TODO
 * Other: Light
 */
public class Level
{
	public static final int TS = 8;

	private static Image mapData = new Image("/images/tileData.png");
	private final int levelW = 200;
	private final int levelH = 12;
	private int[] tiles = new int[levelW * levelH];
	
	private Camera camera;
	
	private ImageTile tileSheet = new ImageTile("/images/tileSheet.png", 8, 8);
	private Image deadScreen = new Image("/images/deadScreen.png");
	private Image startScreen = new Image("/images/startScreen.png");
	private Image winScreen = new Image("/images/winScreen.png");
	private Image optionScreen = new Image("/images/optionMenu.png");
	private LightBox lightBox = new LightBox(100, 0xffff6600);
	
	private ArrayList<GameObject> go = new ArrayList<GameObject>();
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	private AudioPlayer music = new AudioPlayer("/sound/music.wav");
	
	private Player player;
	private Boss boss;
	
	public Level()
	{
		loadLevel(true, true);
		player = (Player) getObject("player");
		camera = new Camera(player);
		boss = (Boss) getObject("boss");
		music.loop();
	}
	
	public void update(GameContainer gc, float delta)
	{
		if(player.isDead())
		{
			camera.getPos().setX(camera.getPos().getX() + delta * 50);
			
			if(Input.isKeyDown(KeyEvent.VK_SPACE))
			{
				go.clear();
				loadLevel(false, true);
				player = (Player) getObject("player");
				boss = (Boss) getObject("boss");
				camera = new Camera(player);
			}
		}
		
		Physics.update();
		
		for(int i = 0; i < go.size(); i++)
		{
			if(go.get(i).isDead())
			{
				go.remove(i);
				continue;
			}
			
			go.get(i).update(gc, delta, this);
		}
		
		camera.update(gc);
	}

	public void render(GameContainer gc, Renderer r)
	{
		r.setTranslate(camera.getPos().getX(), camera.getPos().getY());
		
		for(int x = (int) (-camera.getPos().getX() / Level.TS); x < (int) (-camera.getPos().getX() / Level.TS) + gc.getWidth() / Level.TS + 1; x++)
		{
			for(int y = (int) (-camera.getPos().getY() / Level.TS); y < (int) (-camera.getPos().getY() / Level.TS) + gc.getHeight() / Level.TS; y++)
			{
				if(x >= levelW)
					return;
				if(tiles[x + y * levelW] == 1)
				{
					tileSheet.lb = 2;
					r.drawImageTile(tileSheet, 1, 0, x * tileSheet.tW, y * tileSheet.tH);
				}
				else if(tiles[x + y * levelW] == 0)
				{
					tileSheet.lb = 0;
					r.drawImageTile(tileSheet, 0, 0, x * tileSheet.tW, y * tileSheet.tH);
				}
				else
				{
					tileSheet.lb = 0;
					r.drawImageTile(tileSheet, 3, 0, x * tileSheet.tW, y * tileSheet.tH);
				}
			}
		}
		
		r.drawImage(startScreen, 160, 0);
		r.drawImage(optionScreen, 0, 0);
		
		for(int i = 0; i < go.size(); i++)
		{
			go.get(i).render(gc, r, this);
		}
		
		for(int x = 0; x < levelW; x++)
		{
			for(int y = 0; y < levelH; y++)
			{
				if(tiles[x + y * levelW] == 2)
				{
					tileSheet.lb = 0;
					r.drawImageTile(tileSheet, 2, 0, x * tileSheet.tW, y * tileSheet.tH);
					for(int i = 0; i < Level.TS; i++)
					{
						r.drawLightBox(lightBox, (x * Level.TS) + i, (y + 1) * Level.TS);
					}
				}
			}
		}
		
		for(int i = 0; i < lights.size(); i++)
		{
			Light l = lights.get(i);
			
			if(l.x * Level.TS + camera.getPos().getX() >= 0 || l.x * Level.TS + camera.getPos().getX() < gc.getWidth())
			r.drawLight(lights.get(i), lights.get(i).x * Level.TS + 4, lights.get(i).y * Level.TS + 4);
		}
		
		if(player.isDead() && !boss.isDead())
		{
			r.drawImage(deadScreen, (int)-camera.getPos().getX(), (int)-camera.getPos().getY());
		}
		
		if(boss.isDead())
		{
			r.drawImage(winScreen, (int)-camera.getPos().getX(), (int)-camera.getPos().getY());
		}
	}
	
	public int getTile(int x, int y)
	{
		if(x < 0 || x >= levelW || y < 0 || y >= levelH)
			return 1;
		
		return tiles[x + y * levelW];
	}
	
	public void addObject(GameObject gameObject)
	{
		go.add(gameObject);
	}

	public void loadLevel(boolean loadMap, boolean loadGameObject)
	{
		for(int x = 0; x < mapData.w; x++)
		{
			for(int y = 0; y < mapData.h; y++)
			{
				if(mapData.p[x + y * mapData.w] == 0xff000000 && loadMap)
				{
					tiles[x + y * mapData.w] = 1;
				}
				else if(mapData.p[x + y * mapData.w] == 0xffff0000 && loadMap)
				{
					tiles[x + y * mapData.w] = 2;
				}
				else if(mapData.p[x + y * mapData.w] == 0xff00ff00 && loadGameObject)
				{
					go.add(new Player(x,y));
				}
				else if(mapData.p[x + y * mapData.w] == 0xffff00ff && loadGameObject)
				{
					go.add(new Enemy(x,y));
				}
				else if(mapData.p[x + y * mapData.w] == 0xffffffff && loadMap)
				{
					tiles[x + y * mapData.w] = 0;
				}
				else if(mapData.p[x + y * mapData.w] == 0xff666666 && loadMap)
				{
					tiles[x + y * mapData.w] = 3;
				}
				else if(mapData.p[x + y * mapData.w] == 0xff0000ff && loadGameObject)
				{
					go.add(new JetPack(x,y));
				}
				else if(mapData.p[x + y * mapData.w] == 0xff00ffff && loadGameObject)
				{
					go.add(new Boss(x,y));
				}
				else if(loadMap)
				{
					lights.add(new Light(mapData.p[x + y * mapData.w], 50, x, y));
				}
			}
		}
	}
	
	public GameObject getObject(String tag)
	{
		for(int i = 0; i < go.size(); i++)
		{
			if(go.get(i).getTag().equals(tag))
				return go.get(i);
		}
		
		return null;
	}
}
