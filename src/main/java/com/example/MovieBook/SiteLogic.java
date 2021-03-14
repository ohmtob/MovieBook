package com.example.MovieBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Service
public class SiteLogic {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    public void setUserData(HttpSession session, String username) {
        session.setAttribute("user", userRepository.getUserByUsername(username));
        session.setAttribute("wishedMovies", movieRepository.getWishListByUser(username));
        session.setAttribute("seenMovies", movieRepository.getSeenMoviesByUser(username));
        User user = (User) session.getAttribute("user");
        session.setAttribute("friends", userRepository.getFriendListByUser(user.getId()));
        session.setAttribute("pageShown", 1);
    }

    public void setFriendData(HttpSession session, String username) {
        session.setAttribute("friend", userRepository.getUserByUsername(username));
        session.setAttribute("friendWishedMovies", movieRepository.getWishListByUser(username));
        session.setAttribute("friendSeenMovies", movieRepository.getSeenMoviesByUser(username));
        User user = (User) session.getAttribute("friend");
        session.setAttribute("friendFriends", userRepository.getFriendListByUser(user.getId()));
        session.setAttribute("friendpageShown", 1);
    }


    public void addToSeenMovieList(HttpSession session, int id) {
        List<Movie> movies = (ArrayList<Movie>) session.getAttribute("seenMovies");
        movies.add(movieRepository.getMovieById(id));
        session.setAttribute("seenMovies",movies);
    }

    public void addToMovieWishList(HttpSession session, int id) {
        List<Movie> movies = (ArrayList<Movie>) session.getAttribute("wishedMovies");
        movies.add(movieRepository.getMovieById(id));
        session.setAttribute("wishedMovies",movies);
    }

    public void removeFromSeenMovieList(HttpSession session, int id) {
        List<Movie> movies = (ArrayList<Movie>) session.getAttribute("seenMovies");
        for (int i = 0; i < movies.size(); i++) {
            if(movies.get(i).getId() == id) {
                movies.remove(i);
                break;
            }
        }
        session.setAttribute("seenMovies",movies);
    }

    public void removeFromMovieWishList(HttpSession session, int id) {
        List<Movie> movies = (ArrayList<Movie>) session.getAttribute("wishedMovies");
        for (int i = 0; i < movies.size(); i++) {
            if(movies.get(i).getId() == id) {
                movies.remove(i);
                break;
            }
        }
        session.setAttribute("wishedMovies",movies);
        }

    public void addToFriendList(HttpSession session, int id) {
        List<User> users = (ArrayList<User>) session.getAttribute("friends");
        users.add(userRepository.getUserById(id));
        session.setAttribute("friends",users);
    }

    public void removeFromFriendList(HttpSession session, int id) {
        List<User> users = (ArrayList<User>) session.getAttribute("friends");
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getId() == id) {
                users.remove(i);
                break;
            }
        }        session.setAttribute("friends",users);
    }

    public void clearSubList(HttpSession session) {
        session.setAttribute("movieSubPage", 0);
        session.setAttribute("userSubPage", 0);
        session.setAttribute("profilePage", 0);

    }
}
