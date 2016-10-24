


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import java.io.PrintWriter;

public class Kruskal{
    /*width and height(in squares), and total width and height(in pixels).*/
    protected int width,height,totalWi,totalHei;
    /*List of walls in the current maze.*/
    protected ArrayList<Connection> walls;
    /*List of rooms in the current maze.*/
    protected ArrayList<Room> rums;
    /*The complete maze in a hashset.*/
    protected HashSet<Room> maze;
    /*Partial object that stores the rooms that still need to be connected together.*/
    protected ArrayList<HashSet<Room>> buildSet;
    /*Used to determine if the user is watching the maze being built.*/
    public boolean building;
    /*The time until another wall is removed.*/
    protected float itTime;
    /*Constructor*/
    public Kruskal(int w,int h,int winWidth,int winHeight){
        width=w;
        height=h;
        totalWi=winWidth;
        totalHei=winHeight;
        walls=new ArrayList();
        building=false;
        itTime=.2f;
        maze=new HashSet(20,20,.8);
    }
    /*Instant build for a maze, hides the building from the user.*/
    public void instant(){
        maze=null;
        walls=null;
        rums=null;        
        maze=makeRooms(createSets(createGraph(createMaze(width,height))));
    }
    /*Creates a 2d array of rooms that can be rendered to a surface.*/
    public Room[][] createMaze(int width,int height){
        building=true;
        int x=0,y=0;
        float xStep=(float)totalWi/(float)width,yStep=(float)totalHei/(float)height;
        Room[][] rooms=new Room[width][height];
        while(x<width){
            while(y<height){
                rooms[x][y]=new Room(x*xStep,y*yStep,xStep,yStep,x*width+y);
                y++;
            }
            y=0;
            x++;
        }
        return rooms;
    }
    /*Update method that allows the user to watch the maze being build, updates every 200 milliseconds.*/
    public void buildMaze(float milli){
        itTime-=milli;
        if(itTime<0){
            itTime=.2f;
            buildSet.add(getRanAdj(buildSet));
            if(buildSet.size()==1){
                maze=buildSet.remove(0);
                building=false;
                buildSet=null;
            }
        }
    }    
    /*Starts rendering a maze the viewer can see.*/
    public void startBuild(){
        maze=null;
        walls=null;
        rums=null;
        buildSet=createSets(createGraph(createMaze(width,height)));
    }
    /*Creates a hashset for each room object and returns an arraylist containing each hashset.*/
    public ArrayList<HashSet<Room>> createSets(ArrayList<Room> rooms){
        rums=rooms;
        ArrayList<HashSet<Room>> sets=new ArrayList();
        Iterator<Room> nodes=rooms.iterator();
        while(nodes.hasNext()){
            HashSet<Room> room=new HashSet(10,20,0.8f);
            room.add(nodes.next());
            sets.add(room);
        }
        return sets;
    }
    /*Used to build the maze 'instantaneously.*/
    public HashSet<Room> makeRooms(ArrayList<HashSet<Room>> ranRooms){
        while(ranRooms.size()!=1){
            ranRooms.add(getRanAdj(ranRooms));
        }
        building=false;
        return ranRooms.remove(0);
    }
    /*Writes the current maze to a randomly named text file.*/
    public void outputMaze() throws FileNotFoundException{        
        PrintWriter wr=new PrintWriter(new Random().nextInt(Integer.MAX_VALUE)+".txt");
        for(Room r:rums){
            //node name x y width height
            wr.println("n "+r.number+r.xCoor+" "+r.yCoor+" "+r.width+" "+r.height);
        }
        for(Connection wall: walls){
            wr.println("e "+wall.r1.number+" "+wall.r2.number+" "+7.0);
        }
        for(Connection wall:walls){
            wr.println("L "+wall.x1+" "+wall.y1+" "+wall.x2+" "+wall.y2);
        }
        wr.close();
    }
    /*Returns the union of a hashset containing the union of two rooms formerly separated by a wall.*/
    private HashSet<Room> getRanAdj(ArrayList<HashSet<Room>> all){
        boolean found=false;
        while(!found){
            Connection div=walls.get(new Random().nextInt(walls.size()));
            Room side1=div.r1,side2=div.r2;
            HashSet<Room> s1=find(side1,all),s2=find(side2,all);
            if(s1!=s2 && s1!=null && s2!=null){
                walls.remove(div);
                div.isWall=false;
                div.r1.removeWall(div);
                div.r2.removeWall(div);
                all.remove(s1);
                all.remove(s2);
                return s1.union(s2);
            }
            walls.add(div);
            
        }
        return null;
    }
    /*Returns the hashset that contains target.*/
    public HashSet<Room> find(Room target,ArrayList<HashSet<Room>> all){
        Iterator it=all.iterator();
        while(it.hasNext()){
            HashSet set=(HashSet<Room>)it.next();
            HashMapIterator iter=set.iterator(true);
            if(set.contains(target)){
                return set;
            }
        }  
        return null;
    }
    /*Build the rooms array and create walls for each room.*/
    public ArrayList<Room> createGraph(Room[][] rooms){
        ArrayList<Room> roomList=new ArrayList();
        walls=new ArrayList();
        int x=0,y=0;
        while(x<width){
            while(y<height){
                roomList.add(rooms[x][y]);
                y++;
            }
            y=0;
            x++;
        }
        int curX=0,curY=0,counter=0;
        while(true){
            Room r=rooms[curX][curY]; 
            Connection wl=null;
            if(curX-1>=0 && !isConnected(rooms[curX][curY], rooms[curX-1][curY])){
                wl=new Connection(r.xCoor,r.yCoor,r.xCoor,r.yCoor+r.height,r,rooms[curX-1][curY]);
                r.addConn(rooms[curX-1][curY]);
                r.addWall(wl);
                rooms[curX-1][curY].addConn(r);
                rooms[curX-1][curY].addWall(wl);
                walls.add(wl);
            }
            if(curX+1<width && !isConnected(rooms[curX][curY], rooms[curX+1][curY])){
                wl=new Connection(r.xCoor+r.width,r.yCoor,r.xCoor+r.width,r.yCoor+r.height,r,rooms[curX+1][curY]);
                r.addConn(rooms[curX+1][curY]);
                r.addWall(wl);
                rooms[curX+1][curY].addConn(r);
                rooms[curX+1][curY].addWall(wl);
                walls.add(wl);
            }               
            if(curY+1<height && !isConnected(rooms[curX][curY], rooms[curX][curY+1])){
                wl=new Connection(r.xCoor,r.yCoor+r.height,r.xCoor+r.width,r.yCoor+r.height,r,rooms[curX][curY+1]);
                r.addConn(rooms[curX][curY+1]);
                r.addWall(wl);
                rooms[curX][curY+1].addConn(r);
                rooms[curX][curY+1].addWall(wl);
                walls.add(wl);
            }
            if(curY-1>=0 && !isConnected(rooms[curX][curY], rooms[curX][curY-1])){
                wl=new Connection(r.xCoor,r.yCoor,r.xCoor+r.width,r.yCoor,r,rooms[curX][curY-1]);
                r.addConn(rooms[curX][curY-1]);
                r.addWall(wl);
                rooms[curX][curY-1].addConn(r);
                rooms[curX][curY-1].addWall(wl);
                walls.add(wl);           
            }
            curY++;
            if(curY==height){
                curX++;
                curY=0;
            }
            if(curX==width){
                break;
            }
            counter++;
        }
        return roomList;
    }
    /*Returns true if r1 and r2 have a common Connection.*/
    public boolean isConnected(Room r1,Room r2){
        return r1.adjacent(r2);
    }
    /*Draw the current maze/*/
    public void draw(Graphics g){
        /*Maze if rendered.*/
        if(!building){
            g.setColor(Color.magenta);
            g.drawRect(-1,-1, totalWi+1, totalHei+1);
            HashMapIterator it=maze.iterator(true);
            while(it.hasNext()){
                ((Room)it.next()).draw(g);
            }
            for(Connection wall:walls){
                wall.draw(g);
            }
        /*Maze if currently rendering.*/    
        }else{
            g.setColor(Color.magenta);
            g.drawRect(-1,-1, totalWi+1, totalHei+1);
            for(Room r:rums){
                r.draw(g);
            }
            for(Connection wall:walls){
                wall.draw(g);
            }
        }
    }    
    /*A room that acts similar to a 'node' in a graph class.*/
    public class Room{
        float xCoor,yCoor,width,height;
        /*Connecting edges to this room.*/
        ArrayList<Connection> walls;
        /*Rooms connected to this room by a Connection.*/
        ArrayList<Room> adjs;
        /*Name of the current room.*/
        String number;
        public Room(float x,float y,float w,float h,int count){
            xCoor=x;
            yCoor=y;
            width=w;
            height=h;
            walls=new ArrayList();
            adjs=new ArrayList();
            number="Room"+count+" ";
        }
        public void draw(Graphics g){
            g.setColor(Color.red);
            g.fillRect(xCoor, yCoor, width, height);
        }
        public void addConn(Room c){
            adjs.add(c);
        }
        public void addWall(Connection w){
            walls.add(w);
        }
        public boolean adjacent(Room other){
            for(Room neighbor:adjs){
                if(neighbor==other){
                    return true;
                }
            }
            return false;
        }
        public void removeWall(Connection wall){
            walls.remove(wall);
        }
    }
    /*A connection between two rooms, can represent a wall or connection.*/
    public class Connection{
        float x1,y1,x2,y2;
        public boolean isWall;
        public Room r1,r2;
        public Connection(float x,float y,float xx,float yy,Room r,Room o){
            x1=x;
            y1=y;
            x2=xx;
            y2=yy;
            isWall=true;
            r1=r;
            r2=o;
        }
        public void draw(Graphics g){
            if(isWall){
                g.setColor(Color.black);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
