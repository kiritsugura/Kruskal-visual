


import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;


public class Tester extends BasicGame implements MouseListener,KeyListener{
    private Kruskal kru;
    private float zoomFactor=1.0f,dx,dy,x,y;
    private boolean wasShifted,wasZM;
    public Tester(String title){
        super(title);
        kru=new Kruskal(32,24,640,480);
    }
    @Override
    public void mouseWheelMoved(int change){
        if(change>0 && zoomFactor<8.0){
            zoomFactor+=.20;
            x=0;
            y=0;
        }else if(zoomFactor>.60){
            zoomFactor-=.20;
            x=0;
            y=0;
        }
    }
    public void mouseClicked(int button, int x, int y, int clickCount){

    }
    @Override
    public void keyPressed(int key,char c){
        /*Translate the screen.*/
        if(c=='w'){
            dy=.80f;
            wasShifted=true;
        }else if(c=='d'){
            dx=-.80f;
            wasShifted=true;
        }else if(c=='s'){
            dy=-.80f;
            wasShifted=true;
        }else if(c=='a'){
            dx=.80f;
            wasShifted=true;
        /*Build the maze on screen.*/    
        }else if(c=='b'){
            kru.startBuild();
        /*Build the maze instantly*/    
        }else if(c=='i'){
            kru.instant();
        /*Output the maze to a file.*/
        }else if(c=='o'){
            try{kru.outputMaze();}catch(Exception e){e.printStackTrace();}
        }
    }
    @Override
    public void keyReleased(int key,char c){
        if(c=='w' && wasShifted){
            dy=0;
        }else if(c=='d' && wasShifted){
            dx=0;
        }else if(c=='s' && wasShifted){
            dy=0;
        }else if(c=='a' && wasShifted){
            dx=0;
        }    
        if(dx==0 && dy==0){
            wasShifted=false;
        }
    }   
    @Override
    public void init(GameContainer gc) throws SlickException{ 
        gc.getInput().addMouseListener(this);

    }
    @Override
    public void update(GameContainer gc, int i) throws SlickException { 
        x+=dx*(float)i;
        y+=dy*(float)i;   
        /*Show the maz being built.*/
        if(kru.building){
            kru.buildMaze((float)i);
        }
    }
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException{
        g.scale(zoomFactor,zoomFactor);
        g.translate(x, y);        
        kru.draw(g);
    }
	
    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            Tester my_app = new Tester("Maze");
            appgc = new AppGameContainer(my_app);
            appgc.setDisplayMode(800,600, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
    

