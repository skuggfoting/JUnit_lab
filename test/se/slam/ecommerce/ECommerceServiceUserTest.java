package se.slam.ecommerce;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.slam.ecommerce.ECommerceService;
import se.slam.ecommerce.model.User;
import se.slam.ecommerce.repository.OrderRepository;
import se.slam.ecommerce.repository.ProductRepository;
import se.slam.ecommerce.repository.UserRepository;
import se.slam.ecommerce.repository.checkedexception.RepositoryException;
import se.slam.ecommerce.repository.uncheckedexception.ECommerceServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ECommerceServiceUserTest
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

	// setup new users
	User userOne = new User("MySocialSNr", "MyName", "Secret82");
	User userTwo = new User("MySocialSNr", "MyName", "Secret82");

	@Test
	public void shouldThrowECommerceServiceExceptionIfUserStorageIsEmpty() throws RepositoryException
	{
		// setup expectation
		thrown.expect(ECommerceServiceException.class);
		thrown.expectMessage("No saved users exists ");

		// setup mock object
		when(userRepoMock.getAll()).thenThrow(new RepositoryException(""));

		// test method
		eCommerceService.getAllUsers();
	}

	@Test
	public void shouldAddUserWithCorrectUserInput() throws RepositoryException
	{
		// test method
		User userThree = eCommerceService.addUser(userOne);

		// assert equal
		assertThat(userThree, equalTo(userOne));

		// verify method invocation
		verify(userRepoMock).create(userOne);
	}

	@Test
	public void canGetUserById() throws RepositoryException
	{
		// setup mock object
		when(userRepoMock.get(userOne.getUserId())).thenReturn(userOne);

		// test method
		User userThree = eCommerceService.findUserById(userOne.getUserId());

		// assert equal
		assertThat(userThree.getUserId(), equalTo(userOne.getUserId()));

		// verify method invocation
		verify(userRepoMock).get(userOne.getUserId());
	}

	@Test
	public void canGetAllUsers() throws RepositoryException
	{
		// setup list
		List<User> userList = new ArrayList<>();
		userList.add(userOne);
		userList.add(userTwo);

		// setup mock object
		when(userRepoMock.getAll()).thenReturn(userList);

		// test method
		List<User> userListMock = eCommerceService.getAllUsers();

		// assert equal
		assertThat(userListMock, equalTo(userList));
	}

	@Test
	public void canUpdateUser() throws RepositoryException
	{
		// setup mock object
		when(userRepoMock.exists(userOne.getUsername())).thenReturn(true);

		// test method
		eCommerceService.updateUser(userOne);

		// verify method invocation
		verify(userRepoMock).exists(userOne.getUsername());
		verify(userRepoMock).create(userOne); //Lägg till update i crud och kör det istället
	}

	@Test
	public void canDeleteUser() throws RepositoryException
	{
		// setup mock object
		when(userRepoMock.exists(userOne.getUsername())).thenReturn(true);

		// test method
		eCommerceService.deleteUser(userOne.getUsername());

		// verify method invocation
		verify(userRepoMock).delete(userOne.getUsername());
		verify(userRepoMock).exists(userOne.getUsername());
	}

	@Test
	public void twoUsersThatAreLogicalTheSameShouldBeEqual()
	{
		// assert equal
		assertThat(userOne, is(equalTo(userTwo)));
	}

	@Test
	public void twoUsersThatAreEqualShouldProduceSameHashCode()
	{
		// assert that equal generate same hashcode
		assertThat(userOne, is(equalTo(userTwo)));
		assertThat(userOne.hashCode(), is(equalTo(userTwo.hashCode())));
	}
}