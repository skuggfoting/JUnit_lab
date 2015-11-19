package se.slam.ecommerce.repository;

import java.util.List;

import se.slam.ecommerce.repository.checkedexception.RepositoryException;

public interface CrudRepository <T>
{
	void create(T item) throws RepositoryException; 

	List<T> getAll() throws RepositoryException;
	
	boolean delete(String item) throws RepositoryException; 
	
	T get (String item) throws RepositoryException; 
	
	boolean exists (String item) throws RepositoryException;
}
