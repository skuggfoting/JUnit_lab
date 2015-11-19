package se.slam.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import se.slam.ecommerce.model.OrderRow;
import se.slam.ecommerce.model.Product;

public final class Order 
{
	private String orderId;
	private String date;
	private User user;
	private List<OrderRow> orderRows;
	private double sum;
	
	public Order(String date, User user)
	{
		this.date = date;
		this.user = user;
		this.orderRows = new ArrayList<>();
	}
	
	public double getOrderSum()
	{
		for (OrderRow OrderRow: orderRows)
		{
			sum += OrderRow.getSum();
		} 
		return sum;
	}
	
	public String getOrderId()
	{
		return orderId;
	}
	
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	
	public String getDate()
	{
		return date;
	}
	
	// add any product of one piece
	public void addOrderItems(Product product)
	{
		addOrderItems(product, 1);

	}

	// add products of multiple pieces
	public void addOrderItems(Product product, int i)
	{
		orderRows.add(new OrderRow(product, i));
	}
    

	public List<OrderRow> getOrderRows() 
	{
		return new ArrayList<>(orderRows);
	}

    @Override
    public boolean equals(Object otherObj)
    {
        if (this == otherObj)
        {
            return true;
        }

        if (otherObj instanceof Order)
        {
            Order otherOrder = (Order) otherObj;
            return this.orderId.equals(otherOrder.orderId) && this.user.getUsername().equals(otherOrder.user.getUsername());
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        int result = 1;
        result += 37 * date.hashCode();
        result += 37 * user.getUsername().hashCode();
        return result;
    }
}
