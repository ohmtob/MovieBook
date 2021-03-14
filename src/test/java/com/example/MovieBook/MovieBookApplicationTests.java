package com.example.MovieBook;

import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieBookApplicationTests {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	public void contextLoads() {}

	@Test
	public void testMoviesCount() {
		int count = movieRepository.getMovieCount();
		Assert.assertEquals("Movie count should be 3", 22, count);
	}

	@Test
	public void testGetMovies() {
		List<Movie> movies = movieRepository.getAllMovies();
		Assert.assertEquals("There should be 22 movies in the list", 22, movies.size());
	}

	@Test
	public void testUserCount() {
		int count = userRepository.getUserCount();
		Assert.assertEquals("User count should be 6", 6, count);
	}

	@Test
	public void testUserPassword() {
		String password = userRepository.getUserPassword("Alexander");
		Assert.assertEquals("Password should be 123","123",password);
	}

	@Test
	public void testUserByName(){
		String username = userRepository.getUserByName("Linus", "Lind");
		Assert.assertEquals("Username should be pleonasmen","pleonasmen",username);
	}

	@Test
	public void testUsersByCountry(){
		List<User> users = userRepository.getAllUsersByCountry("Sweden", "Precise");
		Assert.assertEquals("Should be 3 users",3,users.size());
	}

	@Test
	public void testUserByFullName(){
		List<User> users = userRepository.getUserByNameWholeName("Alexander Wirdemo");
		Assert.assertEquals("Should be Alexander","Alexander",users.get(0).getUsername());
	}

	@Test
	public void testUserByUsername(){
		List<User> users = userRepository.getUsersByUsername("lexander", "Contains");
		Assert.assertEquals("Should be Alexander Wirdemo","Alexander Wirdemo",users.get(0).getFirstName()+" "+users.get(0).getLastName());
	}

	@Test
	public void testGetMovieByCountry() {
		List<Movie> movies = movieRepository.getMoviesByCountry("USA", "Precise");
		Assert.assertEquals("Movies from USA should be 17", 17, movies.size());
	}

	@Test
	public void testGetMovieByYear() {
		List<Movie> movies = movieRepository.getMoviesByYear(1993);
		Assert.assertEquals("Movies year 1993 should be 1", 1, movies.size());
	}

	@Test
	public void testGetMovieByGenre() {
		List<Movie> movies = movieRepository.getMoviesByGenre("Action", "Precise");
		Assert.assertEquals("Movies which is action should be 5", 5, movies.size());
	}

	@Test
	public void testGetMovieWithId() {
		Movie movie = movieRepository.getMovieById(2);
		Assert.assertEquals("Movie with id=2 should have title Reservoir Dogs", "Reservoir Dogs", movie.getTitle());
	}

	@Test
	public void testGetMovieWithTitle() {
		List<Movie> movies = movieRepository.getMoviesByName("Reservoir Dogs", "Precise");
		Assert.assertEquals("Movie with name Reservoir Dogs should be 1", 1, movies.size());
	}

	@Test
	public void testGetMovieWithTitleFlexible() {
		List<Movie> movies = movieRepository.getMoviesByName("eservoir Dog", "Contains");
		Assert.assertEquals("Movie with name Reservoir Dogs should be 1", 1, movies.size());
	}

	@Test
	public void testGetMovieWithDirector() {
		List<Movie> movies = movieRepository.getMovieByDirectorWholeName("Quentin Tarantino");
		Assert.assertEquals("Movies by Quentin Tarantino should be 3", 3, movies.size());
	}

	@Test
	public void testGetMovieWithDirectorFlexible() {
		List<Movie> movies = movieRepository.getMovieByDirectorContaining("uentin");
		Assert.assertEquals("Movies by Quentin Tarantino should be 3", 3, movies.size());
	}

	@Test
	public void testGetMovieByLanguage() {
		List<Movie> movies = movieRepository.getMoviesByLanguage("English", "Precise");
		Assert.assertEquals("Movies with English should be 17", 17, movies.size());
	}

	@Test
	public void testGetMovieByActor() {
		List<Movie> movies = movieRepository.getMoviesByActorWholeName("Harvey Keitel");
		Assert.assertEquals("Movies with Harvey Keitel should be 2", 2, movies.size());
	}

	@Test
	public void testGetMovieWithActorFlexible() {
		List<Movie> movies = movieRepository.getMoviesByActorContaining("eite");
		Assert.assertEquals("Movies by Harvey Keitel should be 2", 2, movies.size());
	}

	@Test
	public void testGetUserHasSeenMovies() {
		List<Movie> movies = movieRepository.getSeenMoviesByUser("Alexander");
		Assert.assertEquals("Movies that Alexander has seen should be 1", 1, movies.size());
	}

	@Test
	public void testGetUserWishlist() {
		List<Movie> movies = movieRepository.getWishListByUser("pleonasmen");
		Assert.assertEquals("Movies that Alexander has seen should be 3", 3, movies.size());
	}

	@Test
	public void testGetFollowedUsers() {
		List<User> users = userRepository.getFriendListByUser(3);
		Assert.assertEquals("Users that Linus follows should be 3", 3, users.size());
	}

	@Test
	public void getUserByUserName() {
		User user = userRepository.getUserByUsername("pleonasmen");
		Assert.assertEquals("ID of pleonasmen should be 3", 3, user.getId());
	}

	@Test
	public void getUserByEmail() {
		User user = userRepository.getUserByEmail("linus@uzbekistan.uz");
		Assert.assertEquals("ID of User should be 3", 3, user.getId());
	}


	/*
	@Test
	public void testGetBooksByCustomer() {
		List<Book> books = bookRepository.getBooksByCustomer("Donald");
		Assert.assertEquals("Book purchased by customer Donald should be Douglas Adams", "Douglas Adams", books.get(0).getAuthor());
	}



	@Test
	public void testAddBook() {
		int count = bookRepository.getBooksCount();
		bookRepository.addBook(new Book(null, "New Book Title", "Test Class", 1));
		List<Book> books = bookRepository.getBooks();

		Assert.assertEquals("Books count is increased with 1", (count+1), books.size());

		Book lastBook = books.get(books.size()-1);

		Assert.assertEquals("Last book is the book created by this method", "Test Class", lastBook.getAuthor());
	}


	@Test
	public void testGetBooksMeta() {
		int count = bookRepository.getBooksCount();
		List<Book> books = bookRepository.getBooksMeta();
		Assert.assertEquals("There should be 3 books in the list", count, books.size());
	}
*/




}
