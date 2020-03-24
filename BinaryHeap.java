import java.util.*;

public class BinaryHeap<AnyType extends Comparable<? super AnyType>> extends AbstractQueue<AnyType>
{
    private static final int DEFAULT_CAPACITY = 100;
    private int currentSize;      // Nombre d'elements
    private AnyType [ ] array;    // Tableau contenant les donnees (premier element a l'indice 1)
    private boolean min;
    private int modifications;    // Nombre de modifications apportees a ce monceau

    @SuppressWarnings("unchecked")
    public BinaryHeap( boolean min ){
        this.min = min;
        currentSize = 0;
        array = (AnyType[]) new Comparable[ DEFAULT_CAPACITY + 1];
    }

    @SuppressWarnings("unchecked")
    public BinaryHeap( AnyType[] items, boolean min ){

        this.min = min;
        currentSize = items.length;

        // COMPLETEZ
        array = (AnyType[]) new Comparable[currentSize+1];
        for(int i = 0; i < items.length; i++) {
            array[i+1] = items[i];
        }

        // invoquez buildMinHeap() ou buildMaxHeap() en fonction du parametre min;
        if (min)
            buildMinHeap();
        else
            buildMaxHeap();

        modifications = 0;
    }

    public boolean offer( AnyType x ){
        if (x == null)
            throw new NullPointerException("Cannot insert null in a BinaryHeap");

        if( currentSize + 1 == array.length )
            doubleArray();

        // COMPLETEZ
        int hole = ++currentSize;
        for( ; hole > 1 && x.compareTo( array[ hole / 2 ] ) < 0; hole /= 2)
            array[ hole ] = array[ hole / 2 ];
        array[ hole ] = x;

        modifications++;

        return true;    //idk
    }

    public AnyType peek(){
        if(!isEmpty())
            return array[1];

        return null;
    }

    public AnyType poll(){
        //COMPLETEZ
        if(isEmpty())
            return null;

        modifications++;

        AnyType minItem = array[1];
        array[1] = array[currentSize--];
        if (min)
            buildMinHeap();
        else
            buildMaxHeap();
        return minItem;
    }

    public Iterator<AnyType> iterator(){
        return new HeapIterator();
    }

    private void buildMinHeap(){
        //COMPLETEZ
        for( int i = currentSize / 2; i > 0; i-- )
            percolateDownMinHeap( i, currentSize );
    }

    private void buildMaxHeap(){
        //COMPLETEZ
        for( int i = currentSize / 2; i > 0; i-- )
            percolateDownMaxHeap( i, currentSize );
    }

    public boolean isEmpty(){
        return currentSize == 0;
    }

    public int size(){
        return currentSize;
    }

    public void clear(){
        currentSize = 0;
        modifications = 0;
        array = (AnyType[]) new Comparable[ DEFAULT_CAPACITY + 1];
    }

    private static int leftChild( int i, boolean heapIndexing ){
        return ( heapIndexing ? 2*i : 2*i+1 );
    }

    private void swapReferences( int index1, int index2 ){
        swapReferences(array, index1, index2);
    }

    private static <AnyType extends Comparable<? super AnyType>>
    void swapReferences( AnyType[] array, int index1, int index2 ){

        AnyType tmp = array[ index1 ];
        array[ index1 ] = array[ index2 ];
        array[ index2 ] = tmp;
    }

    @SuppressWarnings("unchecked")
    private void doubleArray(){
        AnyType [ ] newArray;

        newArray = (AnyType []) new Comparable[ array.length * 2 ];
        for( int i = 0; i < array.length; i++ )
            newArray[ i ] = array[ i ];
        array = newArray;
    }


    /**
     * @param hole    Position a percoler
     * @param size    Indice max du tableau
     */
    private void percolateDownMinHeap( int hole, int size ){
        percolateDownMinHeap(array, hole, size, true);
    }

    /**
     * @param array   Tableau d'element
     * @param hole    Position a percoler
     * @param size    Indice max du tableau
     * @param heapIndexing  True si les elements commencent a l'index 1, false sinon
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void percolateDownMinHeap( AnyType[] array, int hole, int size, boolean heapIndexing )
    {
        //COMPLETEZ
        int child;
        AnyType tmp = array[ hole ];
        for( ; leftChild(hole, heapIndexing) <= size; hole = child )
        {
            child = leftChild(hole, heapIndexing); //Considérer fils de gauche
            if( child != size && // il y a deux fils
                    array[ child + 1 ].compareTo( array[ child ] ) < 0 ) //et fils droit<fils gauche
                child++; //Considérer fils droit
            if( array[ child ].compareTo( tmp ) < 0 )//fils considéré< élément à percoler
                array[ hole ] = array[ child ];//Remonter le fils courrent de un niveau
            else
                break; //sortir de la boucle. L’élément à percoler sera inséré à position hole
        }
        array[ hole ] = tmp; // Insérer l’élément à percoler à la position hole
    }

    /**
     * @param hole    Position a percoler
     * @param size    Indice max du tableau
     */
    private void percolateDownMaxHeap( int hole, int size ){
        percolateDownMaxHeap(array, hole, size, true);
    }

    /**
     * @param array         Tableau d'element
     * @param hole          Position a percoler
     * @param size          Indice max du tableau
     * @param heapIndexing  True si les elements commencent a l'index 1, false sinon
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void percolateDownMaxHeap( AnyType[] array, int hole, int size, boolean heapIndexing )
    {
        //COMPLETEZ
        int child;
        AnyType tmp = array[ hole ];
        for( ; leftChild(hole, heapIndexing) <= size; hole = child )
        {
            child = leftChild(hole, heapIndexing); //Considérer fils de gauche
            if( child != size && // il y a deux fils
                    array[ child + 1 ].compareTo( array[ child ] ) > 0 ) //et fils droit>fils gauche
                child++; //Considérer fils droit
            if( array[ child ].compareTo( tmp ) > 0 )//fils considéré< élément à percoler
                array[ hole ] = array[ child ];//Remonter le fils courrent de un niveau
            else
                break; //sortir de la boucle. L’élément à percoler sera inséré à position hole
        }
        array[ hole ] = tmp; // Insérer l’élément à percoler à la position hole
    }

    public static <AnyType extends Comparable<? super AnyType>>
    void heapSort( AnyType[] a )
    {
        //COMPLETEZ
        for( int i = a.length / 2; i >= 0; i-- )
            percolateDownMaxHeap( a, i, a.length-1, false);

        for( int i = a.length - 1; i > 0; i-- )
        {
            swapReferences( a, 0, i );
            percolateDownMaxHeap( a, 0, i-1 , false);
        }
    }

    public static <AnyType extends Comparable<? super AnyType>>
    void heapSortReverse( AnyType[] a )
    {
        //COMPLETEZ
        for( int i = a.length/2 ; i >= 0; i-- )
            percolateDownMinHeap( a, i, a.length-1, false);

        for( int i = a.length - 1; i > 0; i-- )
        {
            swapReferences( a, 0, i );
            percolateDownMinHeap( a, 0, i-1 , false);
        }
    }

    public String nonRecursivePrintFancyTree()
    {
        String outputString = "";

        //COMPLETEZ

        return outputString;
    }

    public String printFancyTree()
    {
        return printFancyTree(1, "");
    }

    private String printFancyTree( int index, String prefix)
    {
        String outputString = "";

        outputString = prefix + "|__";

        if( index <= currentSize )
        {
            boolean isLeaf = index > currentSize/2;

            outputString += array[ index ] + "\n";

            String _prefix = prefix;

            if( index%2 == 0 )
                _prefix += "|  "; // un | et trois espace
            else
                _prefix += "   " ; // quatre espaces

            if( !isLeaf ) {
                outputString += printFancyTree( 2*index, _prefix);
                outputString += printFancyTree( 2*index + 1, _prefix);
            }
        }
        else
            outputString += "null\n";

        return outputString;
    }

    private class HeapIterator implements Iterator { //not static

        int pointedIndex = 0;
        int originalModifications = modifications;

        public boolean hasNext() {



            boolean hasNext = false;
            if(pointedIndex < currentSize)
                hasNext = true;
            return hasNext;
        }

        public Object next() throws NoSuchElementException,
                ConcurrentModificationException,
                UnsupportedOperationException {

            if (originalModifications != modifications)
                throw new ConcurrentModificationException();

            //COMPLETEZ
            pointedIndex++;
            return array[pointedIndex];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
