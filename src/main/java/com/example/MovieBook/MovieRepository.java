package com.example.MovieBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A repository class that handles all the requests to and from the database.
 *
 *
 */

@Repository
public class MovieRepository {

    @Autowired
    private SiteLogic siteLogic;

    private String connstr = "jdbc:sqlserver://localhost;databasename=MovieBook;user=admindb;password=123123";


    /*
    Index of methods:

    1. Helper methods
        1.1 createMovie(ResultSet rs)
    2. Data retrieval methods
        2.1 getMovieCount()
        2.2 getAllMovies()
        2.3 getMovieById(int id)
        2.4 getMoviesByName(String title, String detailedSearch)
        2.5 getMovieByDirectorWholeName(String wholeName)
        2.6 getMovieByDirectorContaining(String name)
        2.7 getMoviesByLanguage(String language, String detailedSearch)
        2.8 getMoviesByCountry(String country, String detailedSearch)
        2.9 getMoviesByYear(int year)
        2.10 getMoviesByGenre(String genre, String detailedSearch)
        2.11 getMoviesByActorWholeName(String actorName)
        2.12 getMoviesByActorContaining(String actorName)
        2.13 getSeenMoviesByUser(String username)
        2.14 getWishListByUser(String username)
    3. Data injections methods
        3.1 addMovieToMovieList(HttpSession session, int id, String type)
        3.2 removeMovieFromMovieList(HttpSession session, int id, String type)

     */

    // 1. Helper methods

    /**
     * Helper method to create a Movie object instantiated with data from the ResultSet
     *
     * @param rs A resultset containing the information of a query
     */
    private Movie createMovie(ResultSet rs) throws SQLException {
        return new Movie(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("country"),
                rs.getString("movielanguage"),
                rs.getInt("CREATEDYEAR"),
                rs.getBoolean("forChildren"));
    }

    // 2. Data retrieval methods

    /**
     * A method that retrieves the amount of movies in the database
     *
     * @return count the amount of movies in the database
     */
    public int getMovieCount() {
       int count = 0;

       try(Connection conn = DriverManager.getConnection(connstr);
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM MOVIE");) {
           System.out.println(rs);
            while(rs.next()) {
                count = rs.getInt("count");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * A method that retrieves a list of all the movies in the data base
     *
     * @return movies a list of all the movies in the database
     */
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<Movie>();
        try (Connection conn = DriverManager.getConnection(connstr);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM MOVIE");)
                {
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * A method that retrieves a movie based on it's id
     *
     * @return movie the movie requested
     */
    public Movie getMovieById(int id) {
        Movie movie = null;
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie WHERE Id = ?");
        )
        {
            stmt.setString(1, Integer.toString(id));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                movie = createMovie(rs);            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movie;

    }

    /**
     * A method that retrieves a list of movies based on it's name
     *
     * @return movie a list of movies with the requested name
     */
    public List<Movie> getMoviesByName(String title, String detailedSearch) {

        if(detailedSearch.equals("Contains")) {
            title = "%" + title + "%";
        }
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie WHERE title LIKE ?");
        )
        {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of a movies based on it's director
     *
     * @return movies the list of movies with a requested director
     */
    public List<Movie> getMovieByDirectorWholeName(String wholeName) {

        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MOVIE.ID, TITLE, COUNTRY, MOVIELANGUAGE, CREATEDYEAR, FORCHILDREN, DIRECTOR.FIRSTNAME, DIRECTOR.LASTNAME FROM MOVIE JOIN MOVIE_HAS_DIRECTOR ON MOVIE_HAS_DIRECTOR.MOVIEID = MOVIE.ID JOIN DIRECTOR ON MOVIE_HAS_DIRECTOR.DIRECTORID = DIRECTOR.ID WHERE DIRECTOR.FIRSTNAME LIKE ? AND DIRECTOR.LASTNAME LIKE ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            String names[] = wholeName.split(" ");
            if(names.length == 2) {

                stmt.setString(1, names[0]);
                stmt.setString(2, names[1]);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    movies.add(createMovie(rs));
                    System.out.println(movies.size());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * A method that retrieves a list of a movies based on it's director
     *
     * @return movies the list of movies with a requested director
     */
    public List<Movie> getMovieByDirectorContaining(String name) {

        name = "%" + name + "%";

        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT MOVIE.ID, TITLE, COUNTRY, MOVIELANGUAGE, CREATEDYEAR, FORCHILDREN, DIRECTOR.FIRSTNAME, DIRECTOR.LASTNAME FROM MOVIE JOIN MOVIE_HAS_DIRECTOR ON MOVIE_HAS_DIRECTOR.MOVIEID = MOVIE.ID JOIN DIRECTOR ON MOVIE_HAS_DIRECTOR.DIRECTORID = DIRECTOR.ID WHERE DIRECTOR.FIRSTNAME LIKE ? OR DIRECTOR.LASTNAME LIKE ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {


                stmt.setString(1, name);
                stmt.setString(2, name);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    movies.add(createMovie(rs));
                    System.out.println(movies.size());

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * A method that retrieves a list of movies based on it's language
     *
     * @return movie a list of movies with the requested language
     */
    public List<Movie> getMoviesByLanguage(String language, String detailedSearch) {
        if(detailedSearch.equals("Contains")) {
            language = "%" + language + "%";
        }
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie WHERE movielanguage LIKE ?");
        )
        {
            stmt.setString(1, language);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
        }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of movies based on it's country
     *
     * @return movie a list of movies with the requested country
     */
    public List<Movie> getMoviesByCountry(String country, String detailedSearch) {
        if(detailedSearch.equals("Contains")) {
            country = "%" + country + "%";
        }
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie WHERE country LIKE ?");
        )
        {
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of movies based on it's year
     *
     * @return movie a list of movies with the requested year
     */
    public List<Movie> getMoviesByYear(int year) {
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie WHERE createdyear = ?");
        )
        {
            stmt.setString(1, Integer.toString(year));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of movies based on it's genre
     *
     * @return movie a list of movies with the requested genre
     */
    public List<Movie> getMoviesByGenre(String genre, String detailedSearch) {
        if(detailedSearch.equals("Contains")) {
            genre = "%" + genre + "%";
        }
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT movie.id, title, country, movielanguage, createdYear, forChildren FROM Movie JOIN Movie_has_genre ON Movie_has_genre.movieId = movie.id JOIN Genre ON Genre.id = Movie_has_genre.genreId WHERE genreName LIKE ?;");
        )
        {
            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of movies based on an actor
     *
     * @return movie a list of movies with the requested actor
     */
    public List<Movie> getMoviesByActorWholeName(String actorName) {
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT movie.id, title, country, movielanguage, createdYear, forChildren FROM Movie JOIN Movie_has_actor ON Movie_has_actor.movieId = movie.id JOIN Actor ON Actor.id = Movie_has_actor.actorId WHERE Actor.firstName LIKE ? AND Actor.lastName LIKE ?;");
        )
            {
                String names[] = actorName.split(" ");
                if(names.length == 2) {

                    stmt.setString(1, names[0]);
                    stmt.setString(2, names[1]);

                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        movies.add(createMovie(rs));
                        System.out.println(movies.size());
                    }
                }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }


    /**
     * A method that retrieves a list of movies based on if an actor's name contained
     * the string passed to the methods
     *
     * @param actorName a search string
     * @return movie a list of movies with the requested actor
     */
    public List<Movie> getMoviesByActorContaining(String actorName) {
        actorName = "%" + actorName + "%";
        List<Movie> movies = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT movie.id, title, country, movielanguage, createdYear, forChildren FROM Movie JOIN Movie_has_actor ON Movie_has_actor.movieId = movie.id JOIN Actor ON Actor.id = Movie_has_actor.actorId WHERE Actor.firstName LIKE ? OR Actor.lastName LIKE ?;");
        )
        {
                stmt.setString(1, actorName);
                stmt.setString(2, actorName);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    movies.add(createMovie(rs));
                    System.out.println(movies.size());
                }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }


    /**
     * A method that retrieves a list of movies seen by a user
     *
     * @param username the username of the user
     * @return movie a list of movies seen by a requested user
     */

    public List<Movie> getSeenMoviesByUser(String username) {
        List<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT Movie.id, title, movielanguage, movie.country, createdYear, forChildren, rating FROM MOvie JOIN User_has_seen_movie ON MOvie.id = User_has_seen_movie.movieId JOIN MOvie_user ON Movie_User.id = User_has_seen_movie.userId WHERE movie_user.username = ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;

    }

    /**
     * A method that retrieves a list of movies a user wishes to see
     *
     * @param username the username of the user
     * @return movie a wish list of movies by a requested user
     */

    public List<Movie> getWishListByUser(String username) {
        List<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT Movie.id, title, movielanguage, movie.country, createdYear, forChildren FROM MOvie JOIN Movie_wishlist ON MOvie.id = movie_wishlist.movieId JOIN MOvie_user ON Movie_User.id = movie_wishlist.userId WHERE movie_user.username = ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                movies.add(createMovie(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    // 3. Data injection methods

    /**
     * A method that adds movies to a user's movielist
     *
     * @param session the current session
     * @param id the id of the movie
     * @param type the type of movielist that the movie should add to
     */
    public boolean addMovieToMovieList(HttpSession session, int id, String type) {
        String sql = null;
        if(type.equals("wished")) {
            sql = "INSERT INTO MOVIE_WISHLIST (MOVIEID, USERID) VALUES (?,?);";
        } else if (type.equals("hasSeen")) {
            sql = "INSERT INTO USER_HAS_SEEN_MOVIE (MOVIEID, USERID) VALUES (?,?);";
        }
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            System.out.println(id);
            User user = (User) session.getAttribute("user");
            stmt.setInt(1, id);
            stmt.setLong(2, user.getId());

            stmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeMovieFromMovieList(HttpSession session, int id, String type) {
        String sql = null;
        if(type.equals("wished")) {
            sql = "DELETE FROM MOVIE_WISHLIST WHERE MOVIEID = ? AND USERID = ?;";
        } else if (type.equals("hasSeen")) {
            sql = "DELETE FROM USER_HAS_SEEN_MOVIE WHERE MOVIEID = ? AND USERID = ?;";
        }
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {

            User user = (User) session.getAttribute("user");
            System.out.println(id + " " + user.getId());
            stmt.setInt(1, id);
            stmt.setLong(2, user.getId());

            stmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


/*


    public List<Movie> getMoviePage(int page, int pageSize) {
        int from = Math.max(0,page*pageSize);
        int to = Math.min(getBooksCount(),(page+1)*pageSize);

        return getBooks().subList(from, to);
    }

    public int numberOfPages(int pageSize) {

        return (int)Math.ceil((double) (getBooksCount() / pageSize));
    }*/

}
