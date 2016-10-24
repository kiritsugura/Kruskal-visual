

import java.util.NoSuchElementException;
/*Hash-map that takes a key(K) and a value(v) and stores them in an array of nodes based on the hash-value.
  Sacrifices memory for lower Big O run-time.*/
public class HashMap<K,V>{
    /*An array that stores the node items as assigned by the getHash function(see below).*/
    protected Node[] hashItems;
    /*totalSize is the current size of hashItems, incrementZise is the size it is incremented when the load factor is exceeded.
      numOfItems is the current number of items in the hashItems array that are not null.*/
    protected int totalSize,incrementSize,numOfItems;
    /*The load factor of the hash-map(0-1), when it is exceeded, rehash is called to adjust the size of the hash-map.*/
    protected double loadFactor;
    protected boolean isRehash;
    /*An enum that denotes what type of iteration is being preformed on the current hahs-map, ie are keys(Key) or values(Value) being iterated.*/
    public enum IteratorType{
        Key,Value;
    }
    /*Constructor for the hash-map class,creates the array of nodes that will be used as the hash-map.
      @param:initialCap is an int that is the initial number of spots avaliable in the hash-amp. 
             increment is the amount that the capacity is increased by if the load factor is exceeded.
             load is a number 1-0 that is used to denote when the has-set should be expanded, the load factor.*/
    public HashMap(int initialCap,int increment,double load){
        totalSize=initialCap;
        incrementSize=increment;
        loadFactor=load;
        hashItems=new Node[totalSize];
        isRehash=false;
    }
    /*Recursive method that goes through the hash-map and finds the next open spot.
      @param: val is the index of the hash-map that the method is to search. value is the value being added,while key is the key assocaited with the value.
      @return: returns the index at which value and key should be added to.*/
    private int probe(int val,V value,K key){
        if(hashItems[val]==null){
            return val;
        }
        else if(val==totalSize-1){
            val=-1;
        }
        return probe(val+1,value,key);
    }
    /*Adds the key and value arguments to the hash-map.
      @param: value is the value being added to the hash-map, while key is the key associated with the value being added.*/
    public void set(K key,V value){
        int val=getHash(key);
        if(hashItems[val]==null){
            hashItems[val]=new Node(key,value);
            numOfItems++;
        }else if(hashItems[val].getKey()==key){
            hashItems[val]=new Node(key,value);
        }else{
            val=probe(val,value,key);
            hashItems[val]=new Node(key,value);
            numOfItems++;
        }
        if((double)((double)numOfItems/(double)totalSize)>=loadFactor && !isRehash){
            rehash();
        }
    }
    /*Returns if the key of type K is used within the hash-map.
      @return: returns true if the key is found in the hash-map, else returns false.*/
    public boolean contains(K key){        
        if(hashItems[getHash(key)]!=null && key==hashItems[getHash(key)].getKey()){
            return true;
        }else if(probeKey(key,getHash(key))){
            return true;
        }
        return false;
    }
    public boolean probeKey(K key,int index){
        if(index==totalSize){
            index=0;
        }
        if(hashItems[index]==null){
            return false;
        }
        if(hashItems[index].getKey()==key){
            return true;
        }
        return probeKey(key,index+1);
    }
    /*Rehashes the table, reassigning all the values in the current hash-map to a new larger hash-map.*/
    private void rehash(){
        Node[] newHashItems=copy();
        isRehash=true;
        totalSize+=incrementSize;
        hashItems=new Node[totalSize];
        for(int i=0;i<newHashItems.length;i++){
            if(newHashItems[i]!=null)
                this.set((K)newHashItems[i].getKey(),(V)newHashItems[i].getValue());
        }
        isRehash=false;
    }
    /*Copy an array to another one.*/
    private Node[] copy(){
        Node[] newArray=new Node[hashItems.length];
        for(int i=0;i<hashItems.length;i++){
            newArray[i]=hashItems[i];
        }
        hashItems=null;
        return newArray;
    }
    /*Returns a positive representation of the key(of type k).
      @return: returns the positive value of the hash-code key.*/
    private int getHash(K key){
        if(key.hashCode()%totalSize<0)
            return (key.hashCode()%totalSize)*-1;
        return key.hashCode()%totalSize;
    }
    /*@return: returns the total number of slots in use in the hash-map,also known as the logical size.*/
    public int getSize(){
        return numOfItems;
    }
    /*@return: returns the total number of slots in the hash-amp,also known as the pysical size.*/
    public int getCurrentCap(){
        return totalSize;
    }
    /*returns a value associated with a key.
      @param: key is the key of the object that is being reqeusted to be returned.
      @return: returns the value assocaited with the key if it exists, else returns null.*/
    public Object get(K key){
        int hash=getHash(key);
        if(hashItems[hash]!=null && key.equals(hashItems[hash].getKey())){
            return hashItems[hash].getValue();
        }
        if(hashItems[hash+1]!=null ||(hash+1==totalSize && hashItems[0]!=null)){
            for(int i=hash;hashItems[i]!=null;i++){
                if(i==totalSize){
                    i=0;
                }                
                if(key.equals(hashItems[i].getKey())){
                    return hashItems[i].getValue();
                }
            }
        }
        return null;
    }
    /*@param: i is an index for the hash-map.
      @return: returns the key that is stored in hash-map index i.*/
    public Object getKey(int i){
        return hashItems[i].getKey(); 
    }
    /*@param: i is an index for the hash-map.
      @return: returns the value that at hash-map index i.*/
    public Object getValue(int i){
        return hashItems[i].getValue();
    }
    /*Removes an object from the hash-map and returns it.
      @param: key is the key of the item that is requesting to be removed, if no element has that key, and exception is thrown.
      @return: returns the value of the object that was removed from the hash-map.*/
    public Object remove(K key) throws NoSuchElementException{
        int hash=getHash(key);
        if(hashItems[hash]!=null){
            if(!key.equals(hashItems[hash].getKey())){
                for(int i=hash;hashItems[i]!=null;i++){                    
                    if(key.equals(hashItems[i].getKey())){
                        hash=i;
                        break;
                    }
                    if(i==totalSize-1){
                        i=-1;
                    }
                }
            }
            Node temp=hashItems[hash];
            hashItems[hash]=null;
            for(int i=hash+1;i<hashItems.length;i++){
                if(hashItems[i]!=null){
                    Node oldNode=hashItems[i];
                    hashItems[i]=null;
                    set((K)oldNode.getKey(),(V)oldNode.getValue());
                    numOfItems--;
                }else{
                    break;
                }
            }
            return temp.getValue();
        }
        throw new NoSuchElementException();
    }
    /*String representation of the hash-map.
      @return: returns a String that represents the hash-map.*/
    @Override
    public String toString(){
        String vals="{";
        for(int i=0;i<totalSize;i++){
            if(hashItems[i]!=null){
                vals+=(hashItems[i].getKey()+":"+hashItems[i].getValue()+",");
            }
        }
        vals=vals.substring(0,vals.length()-1)+"}";       
        return (vals.length()>2) ? vals:"{None}";
    }
    /*Creates and returns an iterator for a hash-map.
      @param:isKeyLooping is a boolean that is used to determine what is being looped through,key(true) or values(false).
      @return; returns an iterator that iterates based on the isKeyLooping boolan.*/
    public HashMapIterator<K,V> iterator(boolean isKeyLooping){
        return new HashMapIterator(isKeyLooping,this);
    }
}
/*Class that is used to store the information(key,value) of each hash-map item. 
  K is a key type,while V represents the value type.*/
class Node<K,V>{
    /*Identifies the key that is stored in this current node.*/
    private K nodeKey;
    /*Identifies the value that is stored in this current node.*/
    private V nodeValue;
    /*Constructor for a node object.
      @param: key is a key,while value is the value that will be stored in this node.*/
    public Node(K key,V value){
        nodeKey=key;
        nodeValue=value;
    }
    /*@return: returns the key associated with the current node object.*/
    public K getKey(){
        return nodeKey;
    }
    /*@return: returns the value associated with the current node.*/
    public V getValue(){
        return nodeValue;
    }
}
