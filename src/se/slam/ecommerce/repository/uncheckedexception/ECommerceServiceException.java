package se.slam.ecommerce.repository.uncheckedexception;

public class ECommerceServiceException extends RuntimeException
{
	private static final long serialVersionUID = -5623167601288818039L;

	public ECommerceServiceException(String message) 
	{
		super(message);
	}
	
	public ECommerceServiceException(String message, Throwable cause) 
	{
		super(message, cause);
	}
}
