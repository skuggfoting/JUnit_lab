package se.slam.ecommerce.repository.checkedexception;

public class RepositoryException extends Exception
{

	private static final long serialVersionUID = -1587405498053999703L;

	public RepositoryException(String message)
	    {
	        super(message);
	    }

	    public RepositoryException(String message, Throwable cause)
	    {
	        super(message, cause);
	    }
}
