package se.slam.ecommerce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import se.slam.ecommerce.model.Order;
import se.slam.ecommerce.model.Product;
import se.slam.ecommerce.model.User;
import se.slam.ecommerce.repository.OrderRepository;
import se.slam.ecommerce.repository.ProductRepository;
import se.slam.ecommerce.repository.UserRepository;
import se.slam.ecommerce.repository.checkedexception.RepositoryException;
import se.slam.ecommerce.repository.uncheckedexception.ECommerceServiceException;

public final class ECommerceService
{

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public ECommerceService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository)
	{
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//USER KOOOOOOOOOOOD HÄR UNDER:
	
	public User addUser(User user)
    {
        try
        {
            if(correctUser(user, false ))
            {
                userRepository.create(user);
            }
        }
        catch (RepositoryException e)
        {
            throw new ECommerceServiceException("Not correct user input, can't add this user");
        }
        return user;
    }


    public User updateUser(User user)
    {
        try
        {
            if (correctUser(user, true))
            {
                userRepository.create(user);
            }
        }
        catch (RepositoryException e)
        {
           throw new ECommerceServiceException("Can't update user");
        }
        return user;
    }

    private boolean correctUser(User user, boolean update) throws RepositoryException
    {
        boolean noPassword = user.getPassword() == null;
        boolean notCorrectUsername = user.getUsername().trim().length() > 30  || user.getUsername() == null;
        boolean noUserId = userRepository.get(user.getUserId()) == null;
        if (update)
        {
            if (noPassword || notCorrectUsername || noUserId)
            {
                return false;
            }
            else
            {
                checkPassword(user.getPassword());
            }
        }
        else
        {
            if (noPassword || notCorrectUsername || noUserId)
            {
                return false;
            }
            else
            {
                checkPassword(user.getPassword());
            }
        }
        return true;
    }

    private boolean checkPassword(String password)
    {
        char a;
        int count = 0;
        for (int i = 0; i < password.length(); i++)
        {
            a = password.charAt(i);
            if (!Character.isLetterOrDigit(a))
            {
                return false;
            }
            else if (Character.isDigit(a))
            {
                count++;
            }
        }
        if (count < 2)
        {
            return false;
        }
        return true;
    }

    public User findUserById(String userId)
    {
        try
        {
            return userRepository.get(userId);
        }
        catch (RepositoryException e)
        {
            throw new ECommerceServiceException("User does not exist");
        }
    }

    public void deleteUser(User user)
    {
        try
        {
            if (userRepository.exists(user.getUsername()))
            {
                userRepository.delete(user.getUsername());
            }
            else
            {
                throw new ECommerceServiceException("Can not delete user that does not exist");
            }
        }
        catch (RepositoryException e)
        {
            throw new ECommerceServiceException("Can not delete user that does not exist");
        }
    }


    public List<User> getAllUsers()
    {
        try
        {
            return userRepository.getAll();
        }
        catch (RepositoryException e)
        {
            throw new ECommerceServiceException("No saved users exists ", e);
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// PRODUCT KOOOOOOOOOOOD HÄR UNDER:
	

    	//creates a new product and add it to storage, throws runtime exception
    	public Product createProduct(String productName, double price, String productId){
    		
    		try{
    		productRepository.exists(productName);
    		}
    		catch(RepositoryException e) {
    			throw new ECommerceServiceException("Can't add. The product with ID: " + productId + " already exists in storage");	
    		}
    		
    		Product product = new Product(productName, price, productId);
    		
    		//add new product to the repository(store)
    		try{
    			productRepository.create(product);
    		}
    		catch(RepositoryException e) {
    			throw new ECommerceServiceException("Can't add. The product with ID: " + productId + " already exists in storage");	
    		}
    		
    		return product;			
    	}

    	//get a product by using productID, throws runtime exception
    	public Product getProduct(String productId) {
    		try {
    			return productRepository.get(productId);
    		}
    		catch(RepositoryException e) {
    			throw new ECommerceServiceException("The product with ID: " + productId + " is not found in storage", e);
    		}
    	}

    	//get all products from storage, throws runtime exception
    	public List<Product> getAllProduct(){
    		try {
    			return productRepository.getAll();  
    		}
    		catch(RepositoryException e) {
    			throw new ECommerceServiceException("The storage is empty", e);
    		}
    	}
    	
    	//delete product, throws runtime exception
    	public void deleteProduct(String productId) {
    		try {
    			if (productRepository.exists(productId)) {
    				productRepository.delete(productId);
    			}else{
    				throw new ECommerceServiceException("Cannot delete a product which is not in storage");
    			}
    		}
    		catch(RepositoryException e) {
    			throw new ECommerceServiceException("Cannot delete a product which is not in storage", e);
    		}
    	}
        
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////	
// ORDER KOOOOOOOOOOOD HÄR UNDER:

	public Order createOrder(Order order) //throws ECommerceServiceException
	{
		if (correctOrder(order))
		{
			
			try
			{
				order.setOrderId(UUID.randomUUID().toString());
				orderRepository.createOrder(order);
				return order;
			}
			catch (RepositoryException e)
			{
				throw new ECommerceServiceException("Not a correct order");
			}
			
		}
		else
		{
			throw new ECommerceServiceException("Not a correct order");
		}
	}

	private final boolean correctOrder(Order order)
	{
		if (order.getOrderSum() <= 50000 && order.getOrderRows().isEmpty() == false)
		{
			return true;
		}

		return false;
	}

//	public List<Order> readOrder()
//	{
//		return orderRepository.getAll();
//	}

	public Order findOrderById(String orderId)
	{
			try
			{
				return orderRepository.get(orderId);
			}
			catch (RepositoryException e)
			{
				throw new ECommerceServiceException("No such order");
			}
		
	}

	public void deleteOrder(String orderId)
	{
			try
			{
				orderRepository.delete(orderId);
			}
			catch (RepositoryException e)
			{
				throw new ECommerceServiceException("No such order");
			}
	}
	
	public Order updateOrder(Order order)
	{
			try
			{
				orderRepository.createOrder(order);
				return order;
			}
			catch (RepositoryException e)
			{
				throw new ECommerceServiceException("No such order");
			}
			
	}

	public Collection<ArrayList<Order>> getAllOrders()
	{
			try
			{
				return orderRepository.getAllOrders();
			}
			catch (RepositoryException e)
			{
				throw new ECommerceServiceException("No orders");
			}
	}
	
	public List<Order> getAllOrders(User user)
	{
		try
		{
			return orderRepository.getAllOrdersFromUser(user);
		}
		catch (RepositoryException e)
		{
			throw new ECommerceServiceException("No such user or no order");
			
		}
//		if (null != orderRepository.getAllOrdersFromUser(user))
//		{
//			return orderRepository.getAllOrdersFromUser(user);
//		}
//		else
//		{
//			throw new ECommerceServiceException("No such user or order");
//		}
	}
}
