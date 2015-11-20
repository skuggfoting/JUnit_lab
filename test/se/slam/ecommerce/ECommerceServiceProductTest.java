package se.slam.ecommerce;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.slam.ecommerce.model.Product;
import se.slam.ecommerce.repository.ProductRepository;
import se.slam.ecommerce.repository.checkedexception.RepositoryException;
import se.slam.ecommerce.repository.uncheckedexception.ECommerceServiceException;

@RunWith(MockitoJUnitRunner.class)
public final class ECommerceServiceProductTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ProductRepository productRepositoryMock;

	@InjectMocks
	private ECommerceService eService;

	private final String productId = "1001";
	private final String productName = "pepsi";
	private final double price = 10.00;

	// Test exception on createProduct()
	@Test
	public void shouldThrowECommerceServiceExceptionWhenProductNameAlreadyExists() throws RepositoryException
	{
		// Setup expectation
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("Can't add. The product with ID: " + productId + " already exists in storage");

		// Setup mock object
		when(productRepositoryMock.exists(productName)).thenThrow(new RepositoryException(""));

		// Test method
		eService.createProduct(productName, price, productId);
	}

	// Test if createProduct() can add a new product
	@Test
	public void canCreateProduct() throws RepositoryException
	{
		// Setup mock object
		when(productRepositoryMock.exists(productName)).thenReturn(false);

		// test method
		Product product = eService.createProduct(productName, price, productId);

		// test assert result
		assertThat(product.getProductName(), equalTo(productName));
		assertThat(product.getProductId(), equalTo(productId));
		assertThat(product.getPrice(), equalTo(price));

		// Verify method invocation on mock object
		verify(productRepositoryMock).create(product);
	}

	// Test exception on updateProduct()
	@Test
	public void shouldThrowECommerceServiceExceptionWhenProductIdDoesNotExists() throws RepositoryException
	{
		thrown.expect(ECommerceServiceException.class);
//		thrown.expectMessage("Can't update a product that doesn't exist"); //bryt ut till ett nytt test
		thrown.expectMessage("a disk error has occured");

//		doThrow(new RepositoryException("")).when(productRepositoryMock).exists(productId);  //bryt ut till ett nytt test
		when(productRepositoryMock.exists(productId)).thenReturn(true);
		doThrow(new RepositoryException("")).when(productRepositoryMock).create(product1);

		eService.updateProduct(productName, price, productId);
	}

	Product product1 = new Product(productName, price, productId);
	Product product2 = new Product(productName, price, productId);

	// Test equals() on Product
	@Test
	public void productsWithIdenticalValuesShouldBeEqual()
	{
		assertThat(product1, equalTo(product2));
	}

	// Test hashcode() on Product
	@Test
	public void productsWhichAreEqualShouldProduceSameHashCode()
	{
		// test assert result
		assertThat(product1.getProductId().hashCode(), equalTo(product2.getProductId().hashCode()));
		assertThat(product1.getProductName().hashCode(), equalTo(product2.getProductName().hashCode()));
	}

	// Test delete() in product repository. Should throw run time service
	// exception.
	@Test
	public void shouldThrowECommerceServiceExceptionForNonExistingProductDeletion() throws RepositoryException
	{
		// setup expectations
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("Cannot delete a product which is not in storage");

		// setup mock object for void method
		when(productRepositoryMock.exists(productId)).thenReturn(true);
		when(productRepositoryMock.delete(productId)).thenThrow(new RepositoryException(""));

		// test method
		eService.deleteProduct(productId);
	}

	// Test exception on getProduct(productId)
	@Test
	public void shouldThrowECommerceServiceExceptionIfProductNotFound() throws RepositoryException
	{
		// setup expectation
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("The product with ID: " + productId + " is not found in storage");

		// setup mock object
		when(productRepositoryMock.get(productId)).thenThrow(new RepositoryException(""));

		// test method
		eService.getProduct(productId);
	}

	// Test exception on getAllProduct()
	@Test
	public void shouldThrowECommerceServiceExceptionIfStorageIsEmpty() throws RepositoryException
	{
		// setup expectation
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("The storage is empty");

		// setup mock object
		when(productRepositoryMock.getAll()).thenThrow(new RepositoryException(""));

		// test method
		eService.getAllProduct();
	}

	@Test
	public void canGetAllProduct() throws RepositoryException
	{
		List<Product> productList = new ArrayList<>();

		productList.add(product1);
		productList.add(product2);

		// Setup mock object

		when(productRepositoryMock.getAll()).thenReturn(productList);

		// test method
		assertThat(productList, equalTo(eService.getAllProduct()));
	}

	// Test delete() in product repository. Should return null if the exist()
	// invoked after delete()
	@Test
	public void canDeleteProduct() throws RepositoryException
	{
		// setup mock object for void method
		when(productRepositoryMock.exists(productId)).thenReturn(true);

		// test method
		eService.deleteProduct(productId);

		// Verify method invocation on mock object
		verify(productRepositoryMock).delete(productId);
	}

	// Test update() is not implemented
}