package zelcraft;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Aaron
 */

public class ZelCraft extends BasicGame {
    
    private static final int SCREEN_X = 640, SCREEN_Y = 480,
                                TILE_SIZE=32;
    
    private static final boolean FULLSCREEN_FLAG = true;
    
    private static final float SCROLL_SPEED = 0.3f;
     
    List<ZCDrawable> drawList;
    List<Image> tileSet;
    int numTiles;
    
    int [][] tileMap;
    int tileMapWidth, tileMapHeight,
        camX, camY;
    float camXFloat, camYFloat;
        
    @Override
    public void init(GameContainer gc) throws SlickException {
        tileSet = new ArrayList<>();
        
        SpriteSheet [] spriteSheet = new SpriteSheet [] {
            new SpriteSheet("data/tiles/set1.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/set2.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/set3.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/set4.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/set5.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/bonus.png",TILE_SIZE,TILE_SIZE),
            new SpriteSheet("data/tiles/bomb.png",TILE_SIZE,TILE_SIZE)
        };
    
        for(int s = 0; s < spriteSheet.length; s++) {
            for(int y = 0; y < spriteSheet[s].getVerticalCount(); y++) {
                for(int x = 0; x < spriteSheet[s].getHorizontalCount(); x++) {
                    tileSet.add(spriteSheet[s].getSubImage(x, y));
                }
            }
        }
        
        tileMapWidth = (int)SCREEN_X*4/TILE_SIZE;
        tileMapHeight = (int)SCREEN_Y*4/TILE_SIZE;
        numTiles = tileSet.size();
        
        tileMap = new int[tileMapWidth][tileMapHeight];
        
        for(int y = 0; y < tileMapHeight; y++) {
            for(int x = 0; x < tileMapWidth; x++) {
                tileMap[x][y] = (int)(Math.random()*numTiles);
            }
        }
        
        camX = 0; camY = 0;
                        
        drawList = new LinkedList<>();
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        Input input = gc.getInput();
        
        if(input.isKeyDown(Input.KEY_UP))camYFloat-=delta*SCROLL_SPEED;
        if(input.isKeyDown(Input.KEY_DOWN))camYFloat+=delta*SCROLL_SPEED;
        if(input.isKeyDown(Input.KEY_LEFT))camXFloat-=delta*SCROLL_SPEED;
        if(input.isKeyDown(Input.KEY_RIGHT))camXFloat+=delta*SCROLL_SPEED;
        
        int maxX = tileMapWidth*TILE_SIZE-SCREEN_X;
        int maxY = tileMapHeight*TILE_SIZE-SCREEN_Y;
        if(camXFloat < 0) camXFloat = 0;
        if(camXFloat > maxX) camXFloat = maxX;
        if(camYFloat < 0) camYFloat = 0;
        if(camYFloat > maxY) camYFloat = maxY;
        
        camX = (int)camXFloat;
        camY = (int)camYFloat;
        
        if(input.isKeyDown(Input.KEY_ESCAPE)) gc.exit();
        
        for (ZCDrawable nextUpdate : drawList) {
            nextUpdate.update(delta);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        for (ZCDrawable nextDraw : drawList) {
            nextDraw.draw();
        }
        int minX = (int)(camX/TILE_SIZE), minY = (int)(camY/TILE_SIZE),
            maxX = minX + SCREEN_X/TILE_SIZE+1, maxY = minY + SCREEN_X/TILE_SIZE+1;
        
        if(maxX > tileMapWidth) maxX = tileMapWidth;
        if(maxY > tileMapHeight) maxY = tileMapHeight;
        
        for(int y = minY; y < maxY; y++) {
            for(int x = minX; x < maxX; x++) {
                tileSet.get(tileMap[x][y]).draw(x*TILE_SIZE-camX,y*TILE_SIZE-camY);
            }
        }      
    }
        
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new ZelCraft());
            app.setDisplayMode(SCREEN_X, SCREEN_Y, FULLSCREEN_FLAG);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    public ZelCraft() {
        super("ZelCraft");
    }
}
