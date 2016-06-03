package com.base.game;

import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

import javax.swing.*;

public class SubtitleTrigger extends GameObject
{
	private float dX, dY;

	transient Game game;
	static String subtitleList[] = {
			"原本平静的生活被怪物的出现打破。现在就连城市里也出现了各种各样的畸变体。作为隐藏的炒鸡英雄的我，自然要为平民们找回和平。现在我要先清除城市里的怪物，并且搞清楚怪物出现的原因。",
			"一个变异的蘑菇！真是恶心。",
			"看来怪物的破坏导致了道路塌陷，我得小心不要掉下去。",
			"这户人家已经不在了，他们保护自己用的刀还在，希望有点用。",
			"路面完全不能走了，我得从坏掉的天桥上过去。真害怕这桥再塌下去。",
			"刚刚那只骷髅真是恐怖……这手枪一定是他生前留下的吧。就让我拿上它，继承它主人的遗志去消灭怪物吧。",
			"通过前方这座高架桥就能出城了。可惜上去的话要花点力气。我要小心千万别掉下来！",
			"城市里的怪物基本清除了。传闻说怪物来自于前方的某个洞穴，看来我必须去搞清楚。",

	};
	static int counter = 0;

	public SubtitleTrigger(int x, int y)
	{
		super.tilePos = new Vector2f(x,y);
		tag = "subtitle_trigger";
	}

	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		game = Game.getInstance();
		Physics.addObject(this);
	}

	@Override
	public void render(GameContainer gc, Renderer r, Level level)
	{
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("player"))
		{
			game.showSubtitle(subtitleList[counter++]);
			setDead(true);
		}
	}

	public static void reset() {
		counter = 0;
	}

	@Override
	public Vector2f getUpperLeft() {
		return new Vector2f(tilePos.getX(), 0);
	}

	@Override
	public Vector2f getLowerRight() {
		return new Vector2f(tilePos.getX(), 24);
	}
}
