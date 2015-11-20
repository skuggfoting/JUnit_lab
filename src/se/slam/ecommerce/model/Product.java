package se.slam.ecommerce.model;

public final class Product
{
	private final String productName;
	private final Double price;
	private final String productId;

	public Product(String productName, Double price, String productId)
	{
		this.productName = productName;
		this.price = price;
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public Double getPrice()
	{
		return price;
	}

	public String getProductId()
	{
		return productId;
	}

	@Override
	public boolean equals(Object otherObj)
	{
		if (this == otherObj)
		{
			return true;
		}

		if (otherObj instanceof Product)
		{
			Product otherProd = (Product) otherObj;
			return this.productId.equals(otherProd.productId) && this.productName.equals(otherProd.productName);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += 37 * productId.hashCode();
		result += 37 * productName.hashCode();
		return result;
	}
}
