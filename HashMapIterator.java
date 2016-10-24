
import java.util.Iterator;
import java.util.NoSuchElementException;
/*Iterator that is used to iterate through a Hash-Map or a Hash-Set. 
  Iterates through generic types K(keys) or V(values) based on an enum type determined by a boolean passed into the constructor.*/
public class HashMapIterator<K,V> implements Iterator{
    /*Hash-Map that is currently being iterated through.*/
    private HashMap associate;
    /*The current index of the item that was last returned by next.*/
    private int currentIndex=-1;
    /*An enum that is used to denotate if the iterator is iterating through the keys or values in the hash-map.*/
    private HashMap.IteratorType type;
    /*Constructor for a hash-map iterator object.
      @param:isKey is a boolean that is used to determine what kind of traversal is being done via enum.
             map is the hash-map object that is going to be iterated through.*/
    public HashMapIterator(boolean isKey,HashMap map){
        associate=map;
        /*if isKey is true, then the type is iteration is through the keys,else it is values.*/
        type=(isKey) ? HashMap.IteratorType.Key:HashMap.IteratorType.Value; 
    }
    /*Called in order to determine if the hash-map has a next valid item.
      @return: returns true if the hash-map has a next value,else it returns false.*/
    @Override
    public boolean hasNext(){
        for(int i=currentIndex+1;i<associate.getCurrentCap();i++){
            if(associate.hashItems[i]!=null){
                return true;
            }            
        }
        return false;
    }
    /*Returns the next item of iteration in the hash-map. If none exists, then an exception is thrown.
      @return:returns the next item of iteration from the hash-amp.*/
    @Override
    public Object next() throws NoSuchElementException{
        for(int i=currentIndex+1;i<associate.getCurrentCap();i++){
            if(associate.hashItems[i]!=null){
                currentIndex=i;
                if(type==HashMap.IteratorType.Key){
                    return associate.hashItems[i].getKey();
                }else{
                    return associate.hashItems[i].getValue();
                }
            }
        }
        throw new NoSuchElementException();
    }
}
