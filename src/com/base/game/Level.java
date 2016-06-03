package com.base.game;

import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

import javax.swing.Timer;

import com.base.engine.AudioPlayer;
import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.ImageTile;
import com.base.engine.Input;
import com.base.engine.Light;
import com.base.engine.LightBox;
import com.base.engine.Renderer;

/**
 * 定义一个颜色，表示可以状态会变的方块，每隔一秒作为Ground出现，注意修改loadLevel中的代码
 * Load map data( the word "Level" probably means "map")
 * Map data is stored in file "tileData.png",
 * in which one pixel represents for one block in the game.
 * Different map elements, such as ground, light, magma and enemy,
 * is represented by the color of the pixel:
 * White(0xffffff): Nothing
 * Black(0x000000): Ground
 * Red(0xff0000): Magma
 * (0x...待定义)：ChangableTile
 * .... TODO
 * Other: Light
 */
public class Level
{
	public static final int TS = 48;

	private static Image mapData = new Image("/images/tileData.png");
	private final int levelW = 200;
	private final int levelH = 12;
	private GeometryTile[] tiles = new GeometryTile[levelW * levelH];
	
	public void changeTiles(int x, int y, GeometryTile tile) {
		tiles[x + y * levelW] = tile;
	}

	public void changeTiles(int pos, GeometryTile tile) {
		tiles[pos] = tile;
	}

	private Camera camera;

	private GeometryTile air;
	private Image deadScreen = new Image("/images/deadScreen.png");
	private Image winScreen = new Image("/images/winScreen.png");

	private ArrayList<GameObject> go = new ArrayList<GameObject>();
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	private AudioPlayer music = new AudioPlayer("/sound/music.wav");
	
	private Player player;

	private Boss boss;
	
	private Timer timeshower;

	final static Set<MapTile> tileSet = new HashSet<MapTile>() {{
		try {
			add(new GeometryTile("air", 0xffffffff, "/images/air.png", GeometryTile.ACCESSIBLE));
			add(new GeometryTile("soil", 0xff000000, "/images/soil.png", GeometryTile.INACCESSIBLE));
			add(new GeometryTile("soil2", 0xffC0C0C0, "/images/soil2.png", GeometryTile.INACCESSIBLE));
			add(new GeometryTile("lava", 0xffff0000, "/images/lava.png", GeometryTile.DANGEROUS));
			add(new GeometryTile("snag", 0xff666666, "/images/lava.png",  GeometryTile.DANGEROUS)); // 钉子
			add(new GameObjectTile("player", 0xff00ff00, "com.base.game.Player"));
			add(new GameObjectTile("enemy", 0xffff00ff, "com.base.game.Enemy"));
			add(new GameObjectTile("jetpack", 0xff0000ff, "com.base.game.JetPack"));
			add(new GameObjectTile("boss", 0xff00ffff, "com.base.game.Boss"));
			add(new GameObjectTile("subtitle_trigger", 0xff00C000, "com.base.game.SubtitleTrigger"));
		} catch (ClassNotFoundException e) {
			System.err.println("Cannot load class.");
			System.exit(1);
		}
	}};
	
	public Level()
	{
		loadLevel(true, true);
		player = (Player) getObject("player");
		camera = new Camera(player);
		boss = (Boss) getObject("boss");
		music.loop();
	}

	/**
	 * get the boss object
	 * @return
     */
	public Boss getBoss() {
		if(boss != null && boss.isDead())
			return null;
		return boss;
	}

	/**
	 * get the boss object
	 * @return
	 */
	public Player getPlayer() {
		if(player != null && player.isDead())
			return null;
		return player;
	}

	/**
	 * save the game
	 * @param path save file path
	 * @throws IOException
     */
	public void save(String path) throws IOException {
		System.out.println("Saving...");
		File file = new File(path);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		for(int i=0; i<tiles.length; i++) {
			out.writeObject(tiles[i].getTag());
		}
		out.writeObject(go);
		out.writeObject(lights);
		out.close();
	}

	/**
	 * load the game from @param path.
	 * @param path
	 * @throws IOException
     */
	public void load(String path) throws IOException {
		File file = new File(path);
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
		go = new ArrayList<>();
		try {
			for(int i=0; i<tiles.length; i++) {
				tiles[i] = (GeometryTile) getTile((String) oin.readObject());
			}
			go = (ArrayList<GameObject>) oin.readObject();
			lights = (ArrayList<Light>) oin.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		oin.close();

		player = (Player) getObject("player");
		camera = new Camera(player);
		boss = (Boss) getObject("boss");
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

		/*
		double time = GameTimer.getRunTime() / 1000;
		int sec = (int)Math.floor(time);
		if (time - sec == 0)
		{
			if(sec % 2 == 0)
				for(int i = 0; i < levelW * levelH; i++)
				{
					if (changingTiles[i]==1)
						changeTiles(i, 1);
				}
			else
				for(int i = 0; i < levelW * levelH; i++)
				{
					if (changingTiles[i]==1)
						changeTiles(i, 0);
				}
		}
		*/
	}

	public void render(GameContainer gc, Renderer r)
	{
		r.setTranslate(camera.getPos().getX(), camera.getPos().getY());
		
		for(int x = (int) (-camera.getPos().getX() / Level.TS); x < (int) (-camera.getPos().getX() / Level.TS) + gc.getWidth() / Level.TS + 2; x++)
		{
			for(int y = (int) (-camera.getPos().getY() / Level.TS); y < (int) (-camera.getPos().getY() / Level.TS) + gc.getHeight() / Level.TS; y++)
			{
				if(x >= levelW)
					return;
				if(y >= levelH || y < 0)
					continue;
				r.drawImage(tiles[x + y * levelW].getImage(), x * TS, y * TS);
			}
		}
		
		
		for(int i = 0; i < go.size(); i++)
		{
			go.get(i).render(gc, r, this);
		}

		/*
		for(int x = 0; x < levelW; x++)
		{
			for(int y = 0; y < levelH; y++)
			{
				if(tiles[x + y * levelW] == 2)
				{
					r.drawImage(lava, x * TS, y * TS);
					// draw lights for lava
					for(int i = 0; i < Level.TS; i++)
					{
						r.drawLightBox(lightBox, (x * Level.TS) + i, (y + 1) * Level.TS);
					}
				}
			}
		}
		*/
		
		for(int i = 0; i < lights.size(); i++)
		{
			Light l = lights.get(i);
			
			if(l.x * Level.TS + camera.getPos().getX() >= 0 || l.x * Level.TS + camera.getPos().getX() < gc.getWidth())
			r.drawLight(lights.get(i), lights.get(i).x * Level.TS + 4, lights.get(i).y * Level.TS + 4);
		}
		
		if(player.isDead() && (boss == null || !boss.isDead()))
		{
			r.drawImage(deadScreen, (int)-camera.getPos().getX(), (int)-camera.getPos().getY());
		}
		
		if(boss != null && (boss == null || !boss.isDead()))
		{
			r.drawImage(winScreen, (int)-camera.getPos().getX(), (int)-camera.getPos().getY());
		}
	}

	/**
	 * get the accessibility of (x,y)
	 * @param x
	 * @param y
     * @return
     */
	public int getAccessibility(int x, int y)
	{
		if(x < 0 || x >= levelW || y < 0 || y >= levelH)
			return GeometryTile.INACCESSIBLE;

		if(tiles[x + y * levelW] == null)
			return GeometryTile.ACCESSIBLE;
		
		return tiles[x + y * levelW].getAccessibility();
	}
	
	public void addObject(GameObject gameObject)
	{
		go.add(gameObject);
	}

	/**
	 * load the map from .png file.
	 * @param loadMap whether geometry tile should be loaded.
	 * @param loadGameObject whether game object tile should be loaded.
     */
	public void loadLevel(boolean loadMap, boolean loadGameObject)
	{
		Map<Integer, MapTile> tileMap = new HashMap<>();
		for(MapTile i: tileSet) {
			tileMap.put(i.getColorCode(), i);
		}

		for(int x = 0; x < mapData.w; x++)
		{
			for(int y = 0; y < mapData.h; y++)
			{
				int colorCode = mapData.p[x + y * mapData.w];
				if(!tileMap.containsKey(colorCode)) {
					if(loadMap) {
						lights.add(new Light(mapData.p[x + y * mapData.w], 6 * TS, x, y));
					}
					continue;
				}

				MapTile tile = tileMap.get(colorCode);
				if((tile instanceof GeometryTile) && loadMap) {
					tiles[x + y * mapData.w] = (GeometryTile) tile;
				} else if ((tile instanceof GameObjectTile) && loadGameObject) {
					try {
						Class<?> objClass = ((GameObjectTile) tile).getTargetClass();
						Constructor con = objClass.getConstructor(Integer.TYPE, Integer.TYPE);
						go.add((GameObject) con.newInstance(x, y));
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Can not load object: " +
								((GameObjectTile) tile).getTargetClass().getName());
						System.exit(1);
					}
				}
			}
		}

		air = (GeometryTile) getTile("air");
		for(int i=0; i<tiles.length; i++) {
			if(tiles[i] == null)
				tiles[i] = air;
		}

		//changingTiles[10+200*6]=1;  //for test
		//changingTiles[10+200*7]=1;  //for test
		GameTimer timer = new GameTimer();
		go.add(timer);
		timeshower = new Timer(1, timer);
		GameTimer.setStartTime(System.currentTimeMillis());
		GameTimer.setBufferTime(0);
		timeshower.start();
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

	/**
	 * get specific tile.
	 * @param tag
	 * @return
     */
	MapTile getTile(String tag)
	{
		for(MapTile i: tileSet)
		{
			if(i.getTag().equals(tag))
				return i;
		}
		return null;
	}
}
