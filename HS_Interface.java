public interface HS_Interface
{
	public boolean add( String key ); // dupes must be rejected and return false
   
	public boolean remove( String key ); // if not found return false else remove & return true
   
	public boolean contains( String key ); // true if found false if not
   
	public boolean isEmpty(); // use the call to size
   
	public int size(); // number of keys currently stored in the container
   
   	public void clear();
} // end DictionaryInterface
