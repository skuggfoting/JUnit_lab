package se.slam.ecommerce.model;

public final class User 
{
	private final String userId;
	private final String username;
	private final String password;
	
	public User(String userId, String username, String password)
	{
		this.userId = userId;
		this.username = username;
		this.password = password;
	}

	public String getUserId() 
	{
		return userId;
	}

	public String getUsername() 
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}
	
	 @Override
	    public boolean equals(Object otherObj)
	    {
	        if (this == otherObj)
	        {
	            return true;
	        }

	        if (otherObj instanceof User )
	        {
	            User otherUser = (User) otherObj;
	            return this.userId.equals(otherUser.userId) && this.username.equals(otherUser.username);
	        }
	        return false;
	    }
	 
	 @Override
	    public int hashCode()
	    {
	        int result = 1;
	        result += 37 * userId.hashCode();
	        result += 37 * username.hashCode();
	        return result;
	    }
}
