package com.example.MovieBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A repository class that handles all the requests to and from the database
 * concerning user data.
 *
 *
 */

@Repository
public class UserRepository {

    private String connstr = "jdbc:sqlserver://localhost;databasename=MovieBook;user=admindb;password=123123";
    private List<User> users = new ArrayList<>();

    @Autowired
    private SiteLogic siteLogic;

    /*
    Index of methods:

    1. Helper methods
        1.1 createUser(ResultSet rs)
        1.2 createPublicUser(ResultSet rs)
    2. Data retrieval methods
        2.1 getUserCount()
        2.2 getUserPassword(String username)
        2.3 getUserByName(String firstname, String lastname)
        2.4 getUsersByUsername(String username, String detailedSearch)
        2.5 getUserByNameWholeName(String wholeName)
        2.6 getUserByNameContaining(String name)
        2.7 getAllUsersByCountry(String country, String detailedSearch)
        2.8 getAllUsersByGender(String gender)
        2.9 getUserByUsername(String username)
        2.10 getUserByEmail(String emailAddress)
        2.11 getFriendListByUser(long userId)
        2.12 getUserById(int id)
    3. Data injections methods
        3.1 addFriend(HttpSession session, int id)
        3.2 removeFriend(HttpSession session, int id)
        3.3 addUser(User user)

     */

    // 1. Helper methods

    /**
     * Helper method to create a User object instantiated with data from the ResultSet
     *
     * @param rs A resultset containing the information of a query
     */
    private User createUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("gender").charAt(0), /// kolla upp denna
                rs.getInt("age"),
                rs.getString("username"),
                rs.getString("country"),
                rs.getString("emailaddress"),
                rs.getString("password")
        );
    }

    private User createPublicUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("gender").charAt(0), /// kolla upp denna
                rs.getInt("age"),
                rs.getString("username"),
                rs.getString("country")
        );
    }

    // 2. Data retrieval methods
/*
    /**
     * A method that retrieves the amount of movies in the database
     *
     * @return count the amount of movies in the database
     */
    public int getUserCount() {
        int count = 0;

        try(Connection conn = DriverManager.getConnection(connstr);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM MOVIE_USER");) {
            System.out.println(rs);
            while(rs.next()) {
                count = rs.getInt("count");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getUserPassword(String username) {
        String password = "";

        try(Connection conn = DriverManager.getConnection(connstr);
        PreparedStatement stmt = conn.prepareStatement("SELECT password FROM MOVIE_USER WHERE userName = ?");
            ) {
            //System.out.println(rs);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                password = rs.getString("password");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return password;
    }

    public String getUserByName(String firstname, String lastname){
        String username = "";

        try(Connection conn = DriverManager.getConnection(connstr);
        PreparedStatement stmt = conn.prepareStatement("SELECT userName FROM Movie_User WHERE firstName = ? AND lastName = ?");
        ) {
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return username;
    }

    public List<User> getUsersByUsername(String username, String detailedSearch){
        if(detailedSearch.equals("Contains")) {
            username = "%" + username + "%";
        }
        List<User> users = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE username LIKE ?");
        ) {
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                users.add(createUser(rs));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getUserByNameWholeName(String wholeName) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM MOVIE_USER WHERE MOVIE_USER.FIRSTNAME=? AND MOVIE_USER.LASTNAME=?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            String names[] = wholeName.split(" ");
            if(names.length == 2) {
                System.out.println(names[0]);
                System.out.println(names[1]);

                stmt.setString(1, names[0]);
                stmt.setString(2, names[1]);

                stmt.setString(1, names[0]);
                stmt.setString(2, names[1]);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    users.add(createUser(rs));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getUserByNameContaining(String name) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM MOVIE_USER WHERE MOVIE_USER.FIRSTNAME LIKE ? OR MOVIE_USER.LASTNAME LIKE ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {

                name = "%" + name + "%";

                stmt.setString(1, name);
                stmt.setString(2, name);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    users.add(createUser(rs));
                }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllUsersByCountry(String country, String detailedSearch){
        if(detailedSearch.equals("Contains")) {
            country = "%" + country + "%";
        }
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connstr);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE country LIKE ?");
             ){
            stmt.setString(1,country);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                users.add(createPublicUser(rs));
            }
        }
         catch(Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getAllUsersByGender(String gender){
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connstr);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE gender = ?");
        ){
            stmt.setString(1,gender);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                users.add(createPublicUser(rs));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByUsername(String username) {

        User user = null;
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE userName = ?");
        )
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = createUser(rs);            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;

    }

    public User getUserByEmail(String emailAddress) {
        User user = null;
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE emailAddress = ?");
        )
        {
            stmt.setString(1, emailAddress);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = createUser(rs);            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;

    }

    public List<User> getFriendListByUser(long userId) {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT id, firstname, lastname, age, gender, username, country, followedUSer FROM Movie_User JOIN User_Follows_User ON Movie_User.id = User_Follows_User.followedUser WHERE userWhoFollows = ?;";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                users.add(createPublicUser(rs));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * A method that retrieves a movie based on it's id
     *
     * @return movie the movie requested
     */
    public User getUserById(int id) {
        User user = null;
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Movie_User WHERE Id = ?");)
        {
            stmt.setString(1, Integer.toString(id));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = createPublicUser(rs);            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;

    }

    // 3 Data injection methods

    public boolean addFriend(HttpSession session, int id) {
        String sql = "INSERT INTO USER_FOLLOWS_USER (FOLLOWEDUSER, USERWHOFOLLOWS) VALUES (?,?);";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
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

    public boolean removeFriend(HttpSession session, int id) {
        String sql ="DELETE FROM USER_FOLLOWS_USER WHERE FOLLOWEDUSER = ? AND USERWHOFOLLOWS = ?;";

        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
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

    public void addUser(User user) {
        String sql = "INSERT INTO MOVIE_USER (FIRSTNAME, LASTNAME, GENDER, AGE, USERNAME, COUNTRY, EMAILADDRESS, PASSWORD) VALUES (?,?,?,?,?,?,?,?);";
        try(Connection conn = DriverManager.getConnection(connstr);
            PreparedStatement stmt = conn.prepareStatement(sql);
        )
        {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, Character.toString(user.getGender()));
            stmt.setInt(4, user.getAge());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, user.getCountry());
            stmt.setString(7, user.getEmailAddress());
            stmt.setString(8, user.getPassword());
            stmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
