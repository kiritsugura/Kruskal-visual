


/*Hash-set object that extends the hash-map class. Instead of having a key and a value, it contains the key as the key and value.*/
public class HashSet<K> extends HashMap{
    /*Constructor for  hash-set object. Passes the parameters to the super-hashmap-constructor.
      @param:initialCap is an int that is the initial number of spots avaliable in the hash-set. 
             increment is the amount that the capacity is increased by if the load factor is gone over.
             load is a number 1-0 that is used to denote when the has-set should be expanded,the load factor.*/
    public HashSet(int initialCap,int increment,double load){
        super(initialCap,increment,load);
    }
    /*Wrapper method that calls the super set method in order to add a value to the hash-set.*/
    public void add(K key){
        super.set(key,key);
    }
    /*Coverts this instance of the hash-set into a string.
      @return: returns a string representing the hash-set.*/
    @Override
    public String toString(){
        String vals="{";
        for(int i=0;i<totalSize;i++){
            if(hashItems[i]!=null){
                vals+=(hashItems[i].getKey()+",");
            }
        }
        vals=vals.substring(0,vals.length()-1)+"}";       
        return (vals.length()>2) ? vals:"{None}";
    }
    /*Establishes a union of the two hash-sets.
      @param: other is the other hash-set that is being unionized with the current one.
      @return: returns a hash-set that represents the union of the two hash-sets.*/
    public HashSet<K> union(HashSet<K> other){
        HashSet<K> union=new HashSet(totalSize,incrementSize,loadFactor);
        for(int i=0;i<totalSize;i++){
            if(hashItems[i]!=null)
                union.add((K)hashItems[i].getKey());
        }
        for(int in=0;in<other.totalSize;in++){
            if(other.hashItems[in]!=null && !contains((K)other.hashItems[in].getKey()))
                union.add((K)other.hashItems[in].getKey());
        }
        return union;
    }
    /*Establishes the intersections of two hash-sets.
      @param: other is another hash-set that is tested to determine intersection between it and this.
      @return: returns a hash-set that represents the intersection of this and other.*/
    public HashSet<K> intersection(HashSet<K> other){
        HashSet<K> intersection=new HashSet(totalSize,incrementSize,loadFactor);
        for(int i=0;i<totalSize;i++){
            if(hashItems[i]!=null){
//                Object val=other.get(hashItems[i].getKey());
//                if(val.equals(hashItems[i].getKey())){
//                    intersection.add((K)hashItems[i].getKey());
//                }
                /*If the hash value is negative,compensates and changes it to positive so it can represent an index.*/
                int negative=(hashItems[i].getKey().hashCode()>0) ? 1:-1;
                if(other.hashItems[negative*hashItems[i].getKey().hashCode()%other.totalSize]!=null){
                    /*If the two values are equal then and not null, then they are added to the intersection.*/
                    K key=((K)other.hashItems[negative*hashItems[i].getKey().hashCode()%other.totalSize].getKey()==(K)hashItems[i].getKey()) ? (K)hashItems[i].getKey():null;
                    if(key!=null){
                        intersection.add(key);
                    }
                }
            }
        }
        return intersection;    
    }
    /*Establishes the relative difference between two hash-sets.
      @param: other is the hash-set that this is being compared to in order get the relative difference.
      @return: returns a hash-set representing the relative difference of other and this.*/
    public HashSet<K> relativeDifference(HashSet<K> other){
        HashSet<K> difference=union(other);
        HashSet<K> intersection=intersection(other);
        for(int i=0;i<intersection.totalSize;i++){
            if(intersection.hashItems[i]!=null){
                difference.remove((K)intersection.hashItems[i].getKey());
            }
        }       
        return difference;        
    }
}
