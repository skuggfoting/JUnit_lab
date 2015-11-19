package se.slam.ecommerce;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.slam.ecommerce.ECommerceService;
import se.slam.ecommerce.model.Order;
import se.slam.ecommerce.model.Product;
import se.slam.ecommerce.model.User;
import se.slam.ecommerce.repository.OrderRepository;
import se.slam.ecommerce.repository.ProductRepository;
import se.slam.ecommerce.repository.UserRepository;
import se.slam.ecommerce.repository.checkedexception.RepositoryException;
import se.slam.ecommerce.repository.uncheckedexception.ECommerceServiceException;

@RunWith(MockitoJUnitRunner.class)
public final class ECommerceServiceOrderTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private UserRepository userRepoMock;
	@Mock
	private ProductRepository productRepoMock;
	@Mock
	private OrderRepository orderRepoMock;
	@InjectMocks
	private ECommerceService eCommerceService;

	private final String userId = "820128";
	private final String userName = "anna al";
	private final String password = "secret";
	private User anna;
	private Product product1;
	private Order order;

	
	@Before
	public void setup()
	{
		anna = new User(userId, userName, password);
		product1 = new Product("computer", 20000.0, "9873414");
		order = new Order("20151028", anna);
		order.addOrderItems(product1, 2);
	}

//	@Test
//	public void canAddUser() throws UserInfoException 
//	{
//		thrown.expect(UserInfoException.class);
//		thrown.expectMessage("Not a correct user");
//		when(userRepoMock.exists(userName)).thenReturn(true);
//		// User anna = new User(userId, userName, password);
//
//		eCommerceService.addUser(anna);
//		//
//		// assertThat(anna.getUserId(), equalTo(userId));
//		// assertThat(anna.getUsername(), equalTo(userName));
//		// assertThat(anna.getPassword(), equalTo(password));
//
//		verify(userRepoMock).create(anna);
//	}
	
// MIN KOOOOOOOOOOOD HÄR UNDER:

//Create order-tester	
	
	@Test//Check success
	public void canAddOrder() throws RepositoryException
	{
		
		//Lägg till Id-koll!!!
		
		when(orderRepoMock.createOrder(order)).thenReturn(order);
		assertThat(order.getOrderId(), equalTo(null));
		
		Order orderIdFromRepo = eCommerceService.createOrder(order);

		assertThat(orderIdFromRepo.getOrderId(), equalTo(order.getOrderId()));
		
		verify(orderRepoMock).createOrder(order);
	}

	@Test//Check successful failure
	public void checkCorrectOrder() throws ECommerceServiceException
	{
		Product product2 = new Product("hat", 40000.87, "098312094");
		order.addOrderItems(product2);

		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("Not a correct order");

		eCommerceService.createOrder(order);
	}

	
//FindOrderForSpecificOrderId-tester
	
	@Test
	public void checkFindOrderById() throws RepositoryException
	{
		when(orderRepoMock.get(order.getOrderId())).thenReturn(order);
		Order orderFromRepo = eCommerceService.findOrderById(order.getOrderId());

		assertThat(orderFromRepo.getOrderId(), equalTo(order.getOrderId()));
		
		verify(orderRepoMock).get(order.getOrderId());
	}
	
	@Test
	public void checkFindOrderForSpecificOrderId() throws RepositoryException
	{
		when(orderRepoMock.get(order.getOrderId())).thenReturn(order);
		Order orderFromRepo = eCommerceService.findOrderById(order.getOrderId());
		
		assertThat(orderFromRepo, equalTo(order));
		
		verify(orderRepoMock).get(order.getOrderId());
	}
	
//Get all orders-tester
	
	@Test//Check success
	public void checkGetAllOrders() throws RepositoryException
	{
		//En List med alla ordrar för en specifik användare
		ArrayList<Order> userAllOrders = new ArrayList<>();
		userAllOrders.add(order);
		
		//En Map med alla användare och deras resp. ordrar
		HashMap<String, ArrayList<Order>> allOrders = new HashMap<>();
		
		//Placerar alla användarens ordrar i Map
		allOrders.put(anna.getUserId(), userAllOrders);
		
		//En Collection av alla användares ordrar tillsammans
		Collection<ArrayList<Order>> allUsersOrders = allOrders.values();
		
		when(orderRepoMock.getAllOrders()).thenReturn(allUsersOrders);
		Collection<ArrayList<Order>> orderFromRepo = eCommerceService.getAllOrders();

		assertThat(orderFromRepo, (equalTo(allUsersOrders)));

		verify(orderRepoMock).getAllOrders();
	}
	
	@Test//Check failure
	public void shouldThrowOrderInfoExceptionWhenTyingToGetAllWhenNoOrdersExist() throws RepositoryException
	{
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("No orders");
		
		when(orderRepoMock.getAllOrders()).thenThrow(new RepositoryException(""));
		
		eCommerceService.getAllOrders();
		
		verify(orderRepoMock).getAllOrders();
	}

//Get all orders for specific user-tester
	
	@Test//Check success
	public void checkGetAllOrdersForSpecificUser() throws RepositoryException
	{
		ArrayList<Order> userAllOrders = new ArrayList<>();
		userAllOrders.add(order);
		
		when(userRepoMock.get(anna.getUserId())).thenReturn(anna);
		when(orderRepoMock.getAllOrdersFromUser(anna)).thenReturn(userAllOrders);
		Collection<Order> orderFromRepo = eCommerceService.getAllOrders(anna);

		assertThat(orderFromRepo, equalTo(userAllOrders));

		verify(orderRepoMock).getAllOrdersFromUser(anna);
	}
	
	@Test//Check failure
	public void shouldThrowOrderInfoExceptionWhenTryingToGetAnOrderForASpecificUserThatDoesNotExist() throws RepositoryException
	{
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("No such user or no order");
		
		when(orderRepoMock.getAllOrdersFromUser(anna)).thenThrow(new RepositoryException(""));
		
		eCommerceService.getAllOrders(anna);
		
		verify(orderRepoMock).get(anna.getUserId());
	}
	
//Delete-tester
	
	@Test
	public void shouldThrowOrderInfoExceptionWhenTyingToDeleteAnOrderThatDoesNotExist() throws RepositoryException
	{
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("No such order");
		
		when(orderRepoMock.delete(order.getOrderId())).thenThrow(new RepositoryException(""));
		
		eCommerceService.deleteOrder(order.getOrderId());
		
		verify(orderRepoMock).get(order.getOrderId());
	}
	
	@Test
	public void verifyCallOfDelete() throws RepositoryException
	{
		when(orderRepoMock.get(order.getOrderId())).thenReturn(order);
		
		eCommerceService.deleteOrder(order.getOrderId());
		
		verify(orderRepoMock).delete(order.getOrderId());
	}
	
	
//Update-tester:
	
	@Test
	public void shouldThrowOrderInfoExceptionWhenTyingToUpdateAnOrderThatDoesNotExist() throws RepositoryException
	{
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("No such order");
		
		when(orderRepoMock.createOrder(order)).thenThrow(new RepositoryException(""));
		
		eCommerceService.updateOrder(order);
		
		verify(orderRepoMock).get(order.getOrderId());
	}
	
	@Test
	public void verifyCallOfUpdate() throws RepositoryException
	{
		when(orderRepoMock.get(order.getOrderId())).thenReturn(order);
		
		eCommerceService.updateOrder(order);
		
		verify(orderRepoMock).createOrder(order);
	}
}








