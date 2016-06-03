package com.base.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by draplater on 16-6-3.
 */
public class TextImage extends Image {
    public TextImage(String text) {
        BufferedImage image = new BufferedImage(720, 24, BufferedImage.TYPE_INT_ARGB);
        super.w = image.getWidth();
        super.h = image.getHeight();
        int[] data = ((DataBufferInt) image.getData().getDataBuffer()).getData();
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, super.w, super.h);
        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.RED);
        g.drawString(text, 0, 20);
        g.dispose();
        for(int i=0; i< data.length; i++)
            if((data[i] & 0x00ffffff) ==  0x00000000)
                data[i] = 0x00000000;
        super.p = image.getRGB(0, 0, super.w, super.h, null, 0, super.w);

        image.flush();
    }
}
