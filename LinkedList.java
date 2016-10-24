



import java.util.Iterator;
import java.util.NoSuchElementException;
/*The linked list class which is a data structure that stores a list in discontinuous memory.*/
public class LinkedList<E>{
    /*mBgin is the 'head'-or first item-of the List, while mEnd is the 'tail'-or last item-of the list.*/
    protected Node mBegin,mEnd;
    /*mSize stores the size of the current list.*/
    protected int mSize;
    /*Default Constructor that creates an empty list.*/
    public LinkedList(){
        mSize=0;
        mBegin=null;
        mEnd=null;
    }  
    /*Adds an Object of type E to the list end of the linked-list.*/
    public void addToEnd(E obj){
        if(mSize==0){                                   /*If the linked-list is empty.*/
            mSize++;    
            mEnd=new Node(obj);  
            mBegin=mEnd;
        }else{                                          /*In all other cases that the list is not empty.*/
            mSize++;
            Node newNode=new Node(obj);
            mEnd.mNext=newNode;
            newNode.mPrevious=mEnd;
            mEnd=newNode;
            if(mSize==1){
                mEnd=mBegin;
            }else if(mSize==2){
                mEnd=mBegin.mNext;
            }
        }
    }
    /*Adds an Object of type e to the start of the linked-list.*/
    public void addToBegin(E obj){
        if(mSize==0){                                   /*This applies if the linked-list is empty.*/
            mSize++;
            mBegin=new Node(obj);
            mEnd=mBegin;
        }else{                                          /*In all other cases in which the list is not empty.*/
            mSize++;
            Node newNode=new Node(obj);
            mBegin.mPrevious=newNode;
            newNode.mNext=mBegin;
            mBegin=newNode;
            if(mSize==1){
                mBegin=mEnd;
            }else if(mSize==2){
                mEnd=mBegin.mNext;
            }      
        }
    }
    /*Overriden toString method thatprints the linked-list,item by item,from mBegin to mEnd.*/
    @Override
    public String toString(){
        if(mSize==0)
            return"<empty>";
        else if(mSize==1)
            return "<["+mBegin.mValue+"]>";
        else{
            String listItems="<";
            Node loopingNode=mBegin;
            while(!(loopingNode==null)){
                listItems+="["+loopingNode.mValue+"]";
                loopingNode=loopingNode.mNext;
            }
            return(listItems+=">");
        }
    }
    /*Input parameter is an object of type E,that is to be removed from the linked list where found.
      Removes all items from the linked-list that are equal to 'toBeRemoved' of type E.
      Returns the number of items that were removed from the list.*/
    public int removeAll(E toBeRemoved){
        int numOfRemovals=0;
        Node loopingNode=mBegin;
        while(!(loopingNode==null)){
            if(toBeRemoved.equals(loopingNode.mValue)){
                numOfRemovals++;
                mSize--;
                if(loopingNode.mNext==null){
                    loopingNode.mPrevious.mNext=null;
                    mEnd=loopingNode.mPrevious;
                }else if(loopingNode.mPrevious==null){
                    mBegin=loopingNode.mNext;
                    loopingNode.mNext.mPrevious=null; 
                }else{
                    loopingNode.mNext.mPrevious=loopingNode.mPrevious;
                    loopingNode.mPrevious.mNext=loopingNode.mNext;
                }
            }
            loopingNode=loopingNode.mNext;
        }
        return numOfRemovals;
    }
    /*Input parameter is an object of type E,that is going to be counted.
      For each instance where 'objToSearchFor' appears,the counter goes up one.
      Returns the number of times that 'objToSearchFor' was found in the linked-list.*/
    public int count(E objToSearchFor){
        int numOfOccurances=0;
        Node loopingNode=mBegin;
        while(!(loopingNode==null)){
            if(objToSearchFor.equals(loopingNode.mValue))
                numOfOccurances++;
            loopingNode=loopingNode.mNext;
        }
        return numOfOccurances;
    }
    /*Returns a new forwards iterator that starts at mBegin.*/
    public LinkedListIterator iterator(){
        return new LinkedListIterator(mBegin,false,0);
    }
    /*Returns a new backward iterator that starts at mEnd.*/
    public LinkedListIterator riterator(){
        return new LinkedListIterator(mEnd,true,0);
    }    
    /*Input parameter 'index' is the index of the desired item in the list,while 'isFromFront' denotates if the count starts from the mEnd or mBegin.
      Throws an excpetion if the given index is out of range of the list.
      returns an iterator with the currentNode set to the desired index.*/
    public LinkedListIterator<E> getAt(int index,boolean isFromFront) throws IndexOutOfBoundsException{
        if(index<1||index>=mSize){
            throw new IndexOutOfBoundsException();
        }else if(isFromFront){
            Node loopingNode=mBegin;
            int currentIndex=1;
            while(!(loopingNode==null)){
                if(currentIndex==index){
                    return new LinkedListIterator(loopingNode,!isFromFront,index-1);
                }
                loopingNode=loopingNode.mNext;
                currentIndex++;
            }
        }else{
            Node loopingNode=mEnd;
            int currentIndex=1;
            while(!(loopingNode==null)){
                if(currentIndex==index){
                    return new LinkedListIterator(loopingNode,!isFromFront,index-1);
                }
                loopingNode=loopingNode.mPrevious;       
                currentIndex++;
            }                
        }
        return null;
    }
   /*input parameter iterator represents an index that the input parameter obj of type E will be added to the linked-list after.*/
   public void addAfter(LinkedListIterator<E> iterator,E obj){
        if(iterator.currentNode==null || iterator.currentNode.mNext==null){
            Node newestNode=new Node(obj); 
            newestNode.mPrevious=mEnd;
            mEnd.mNext=newestNode;
            mEnd=newestNode;
            iterator.currentNode=mEnd;
            mSize++;
            
        }else{
            Node newestNode=new Node(obj); 
            newestNode.mPrevious=iterator.currentNode;
            newestNode.mNext=iterator.currentNode.mNext;
            iterator.currentNode.mNext.mPrevious=newestNode;
            iterator.currentNode.mNext=newestNode;
            mSize++;
        }
    }   
   /*Iterator class for the linked-list.*/
    public class LinkedListIterator<T extends E> implements Iterator<E>{
        /*currentNode is the current 'index' stored by the iterator.*/
        protected Node currentNode;
        /*isReverse denotates whether the iterator is iterating forwards or backwards.*/
        protected boolean isReverse;
        /*currentSize is the current index of the curentNode.*/
        protected int currentSize;
        /*canRemove determines if the item can be removed withour an exception being thrown.*/
        protected boolean canRemove;
        /*Default constrcutor for the linked-list iterator.
          'current' is the node that the iterator is currently on. 
          'direction' represents the direction the iterator is iterating, ie-forwards or backwards.*/
        public LinkedListIterator(Node current,boolean direction,int itSize){
            currentNode=current;
            currentSize=itSize;
            canRemove=true;
            isReverse=direction;
            if(current==mEnd){
                isReverse=true;
            }else if(current==mBegin){
                isReverse=false;
            }
        }
        /*Returns true if the iterator has a next value to iterate.*/
        @Override
        public boolean hasNext(){
            return currentSize!=mSize;
        }
        /*Returns the next value to be iterated.
          If no next value exists, an exception will be thrown.*/      
        @Override
        public E next() throws NoSuchElementException{
            if(currentSize>=mSize||currentNode==null){
                throw new NoSuchElementException();
            }else if(isReverse){                        /*If the iteration is going from mEnd to mBegin.*/
                canRemove=true;
                if(currentNode==mBegin){
                    currentSize++;
                    currentNode=null;
                    return mBegin.mValue;    
                }
                currentNode=currentNode.mPrevious;
                currentSize++;
                return currentNode.mNext.mValue;
            }else{                                      /*If the iteration is going from mBegin to mEnd.*/
                canRemove=true;
                if(currentNode==mEnd){
                    currentSize++;
                    currentNode=null;
                    return mEnd.mValue;
                }
                currentNode=currentNode.mNext;
                currentSize++;
                return currentNode.mPrevious.mValue;
            }
        }   
        /*Remove method that removes the next value returned by next().
          Will throw an exception if remove is called twice without calling next, or if no next element exists.*/
        @Override
        public void remove() throws NoSuchElementException{
            if(mSize==0 || !canRemove){
                throw new NoSuchElementException();
            }else if(mSize==1){
                canRemove=false;
                mBegin=null;
                mEnd=null;
            }else{
                if(currentSize==mSize){                 /*If the iterator is removing the last item of iteration.*/
                    if(isReverse){
                        canRemove=false;
                        currentNode=mBegin;
                        mBegin=mBegin.mNext;
                        mBegin.mPrevious=null;
                        mSize--;                      
                    }else{
                        canRemove=false;
                        currentNode=mEnd;
                        mEnd=mEnd.mPrevious;
                        mEnd.mNext=null;
                        mSize--;
                    }
                }else if(currentSize==0){               /*If the item after the first item is removed.*/
                    if(isReverse){
                        canRemove=false;
                        currentNode.mPrevious=currentNode.mPrevious.mPrevious;
                        currentNode.mPrevious.mNext=currentNode;
                        mSize--;
                    }else{
                        canRemove=false;
                        currentNode.mNext=currentNode.mNext.mNext;
                        currentNode.mNext.mPrevious.mPrevious=currentNode;
                        mSize--;                   
                    }
                }else{                                  /*In the standard middle element cases.*/
                    if(isReverse){
                        canRemove=false;
                        currentNode.mNext.mPrevious=currentNode.mPrevious;
                        currentNode.mPrevious.mNext=currentNode.mNext;
                        mSize--;                        
                    }else{
                        canRemove=false;
                        currentNode.mPrevious.mNext=currentNode.mNext;
                        currentNode.mNext.mPrevious=currentNode.mPrevious;
                        mSize--;
                    }
                }
            }
        }
    }
    /*Node class that act as the elements of the list. Is a generic object-type, so a Node can be any class.*/
    protected class Node{   
        /*The nodes on each side of the current node object.*/
        Node mNext,mPrevious;
        /*The actual object that the node contains.*/
        E mValue;
        /*Default Constructor for the Node class.*/
        Node(E obj){
            mValue=obj;
        }
        
    }
}
