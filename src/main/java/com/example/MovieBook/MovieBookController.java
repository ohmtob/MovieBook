package com.example.MovieBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MovieBookController {

    @Autowired
    SiteLogic siteLogic;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;


    // First page mappings, the login page and create account page


    @GetMapping("/")
    public String start(){

        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session){
        model.addAttribute("loginPageVisible", true);
        session.setAttribute("accountPageVisible", false);

        return "login";
    }

    @GetMapping("/createaccount")
    public String createAccount(Model model, HttpSession session) {
        model.addAttribute("loginPageVisible", false);

        String[] allChoices = {"Male", "Female", "Other"};
        model.addAttribute("allChoices", allChoices);
        session.setAttribute("accountPageVisible", true);
        return "login";
    }

    @PostMapping("/createdAccount")
    public String createdAccount(HttpSession session, Model model, @RequestParam String firstName, String lastName, String gender, String age, String country, String emailAddress, String username, String password) {
        model.addAttribute("usernameTaken", false);
        model.addAttribute("emailTaken", false);
        model.addAttribute("success", false);
        boolean notFail = true;
        User user = null;
        try {
            user = new User(firstName, lastName, gender.charAt(0), Integer.parseInt(age), username, country, emailAddress, password);
        } catch (Exception e) {
            e.printStackTrace();
            notFail = false;

            System.out.println("Failed");
        }

        String[] allChoices = {"Male", "Female", "Other"};
        model.addAttribute("allChoices", allChoices);

        if(userRepository.getUserByUsername(username) != null) {
            model.addAttribute("usernameTaken", true);
        } else if (userRepository.getUserByEmail(emailAddress) != null) {
            System.out.println(userRepository.getUserByEmail(emailAddress));
            model.addAttribute("emailTaken", true);
        }   else if (!emailAddress.contains("@")) {
            model.addAttribute("emailNotCorrect", true);
        } else if (notFail){
            userRepository.addUser(user);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", true);
        }
        return "login";
    }



    // Content page


    /**
     * A post mapping method that logs in the user to the content page if successful and returns the user
     * to the login page if not successful.
     *
     * @param model
     * @param session
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String loginForm(Model model, HttpSession session, @RequestParam String username, @RequestParam String password){
        if(username.equals("")){
            model.addAttribute("loginPageVisible", true);
            return "login";

        }
        if(password.equals(userRepository.getUserPassword(username))) {
            if(session.getAttribute("user") == null) {
                siteLogic.setUserData(session, username);
            }
            return "UserProfile";
        }
        model.addAttribute("loginPageVisible", true);
    return"login";
    }


    @GetMapping("/UserProfile")
    public String firstPage(HttpSession session){
        if(session.getAttribute("user")!=null){
            session.setAttribute("pageShown", 2);
            siteLogic.clearSubList(session);
            return "UserProfile";
        }
        return "login";
    }

    // PROFILE PAGE

    @GetMapping("/myprofilepage")
    public String myProfilePage(HttpSession session){
        siteLogic.clearSubList(session);
        session.setAttribute("pageShown", 2);
        session.setAttribute("profileView", 0);
        return "UserProfile";
    }

    @GetMapping("/profilefriends")
    public String friendsPage(HttpSession session){
        if((Integer) session.getAttribute("profileView") == 1) {
            session.setAttribute("profileView", 0);
        } else {
            session.setAttribute("profileView", 1);
        }
        return "UserProfile";
    }

    @GetMapping("/seenmovies")
    public String seenMovies(HttpSession session) {
        if((Integer) session.getAttribute("profileView") == 2) {
            session.setAttribute("profileView", 0);
        } else {
            session.setAttribute("profileView", 2);
        }
        return "UserProfile";
    }

    @GetMapping("/wishlist")
    public String wishlistPage(HttpSession session) {
        if((Integer) session.getAttribute("profileView") == 3) {
            session.setAttribute("profileView", 0);
        } else {
            session.setAttribute("profileView", 3);
        }
        return "UserProfile";
    }

    // USER PAGE

    @GetMapping("/users")
    public String usersPage(HttpSession session,Model model){

        siteLogic.clearSubList(session);
        String[] allChoices = {"Name", "Username", "Country"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);

        session.setAttribute("pageShown", 3);

        return "UserProfile";
    }

    // Sub pages under users

    @GetMapping("/searchfriends")
    public String searchUers(HttpSession session, Model model){
        String[] allChoices = {"Name", "Username", "Country"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        session.setAttribute("friendSubPage", 1);
        return "UserProfile";
    }

    @PostMapping("/searchResultUsers")
    public String searchResultUsers(HttpSession session, Model model, @RequestParam String detailedSearch, String searchString, String choice) {
        List<User> users = null;
        String[] allChoices = {"Name", "Username", "Country"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);

        model.addAttribute("showUser", true);

        if(choice.equals("Name")) {
            if(detailedSearch.equals("Precise")) {
                users = userRepository.getUserByNameWholeName(searchString);
            } else {
                users = userRepository.getUserByNameContaining(searchString);
            }
        } else if(choice.equals("Username")) {
            users = userRepository.getUsersByUsername(searchString, detailedSearch);
        } else if(choice.equals("Country")) {
            users = userRepository.getAllUsersByCountry(searchString, detailedSearch);
        } else if(choice.equals("Gender")) {
//            users = userRepository.getAllUsersByGender(searchString);
        }

        if(users == null || users.size() == 0) {
            model.addAttribute("showUser", false);
            model.addAttribute("noUser", true);
        } else {
            session.setAttribute("activeUsers", users);
        }
        return"UserProfile";
    }

    @GetMapping("/suggestedfriends")
    public String suggestedFriends(HttpSession session, Model model){
        session.setAttribute("friendSubPage", 2);
        return "UserProfile";
    }

    @GetMapping("/myfriends")
    public String myFriends(HttpSession session, Model model){
        session.setAttribute("friendSubPage", 3);
        return "UserProfile";
    }

    // MOVIE PAGE

    @GetMapping("/movies")
    public String moviesPage(HttpSession session, Model model){
        siteLogic.clearSubList(session);
        String[] allChoices = {"Title", "Director", "Actor", "Genre", "Country", "Language", "Year"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        session.setAttribute("pageShown", 4);
        session.setAttribute("movieSubPage", 1);

        return "UserProfile";
    }

    // Sub pages under Movies

    @GetMapping("/searchmovies")
    public String search(HttpSession session, Model model){
        String[] allChoices = {"Title", "Director", "Actor", "Genre", "Country", "Language", "Year"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        session.setAttribute("movieSubPage", 1);
        return "UserProfile";
    }

    @PostMapping("/searchResult")
    public String searchResult(HttpSession session, Model model, @RequestParam String searchString, String detailedSearch, String choice){
        List<Movie> movies = null;

        String[] allChoices = {"Title", "Director", "Actor", "Genre", "Country", "Language", "Year"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);


        model.addAttribute("showMovie", true);


        if(choice.equals("Title")) {
            movies = movieRepository.getMoviesByName(searchString, detailedSearch);
        } else if(choice.equals("Director")) {
            if (detailedSearch.equals("Precise")) {
                movies = movieRepository.getMovieByDirectorWholeName(searchString);
            } else {
                movies = movieRepository.getMovieByDirectorContaining(searchString);
            }
        } else if(choice.equals("Actor")) {
            if (detailedSearch.equals("Precise")) {
                movies = movieRepository.getMoviesByActorWholeName(searchString);
            } else {
                movies = movieRepository.getMoviesByActorContaining(searchString);
            }
        } else if(choice.equals("Genre")) {
            movies = movieRepository.getMoviesByGenre(searchString, detailedSearch);
        } else if(choice.equals("Country")) {
            movies = movieRepository.getMoviesByCountry(searchString, detailedSearch);
        } else if(choice.equals("Language")) {
            movies = movieRepository.getMoviesByLanguage(searchString, detailedSearch);
        } else if(choice.equals("Year")) {
            try {
                movies = movieRepository.getMoviesByYear(Integer.parseInt(searchString));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if(movies == null || movies.size() == 0) {
            model.addAttribute("showMovie", false);
            model.addAttribute("noMovies", true);
        } else {
            session.setAttribute("activeMovies", movies);
        }
        return"UserProfile";
    }

    @GetMapping("/topmovies")
    public String topMovies(HttpSession session, Model model){
        session.setAttribute("movieSubPage", 2);
        return "UserProfile";
    }

    @GetMapping("/latestreleases")
    public String latestReleases(HttpSession session, Model model){
        session.setAttribute("movieSubPage", 3);
        return "UserProfile";
    }

    @GetMapping("/suggestedmovies")
    public String suggestedMovies(HttpSession session, Model model){
        session.setAttribute("movieSubPage", 4);
        return "UserProfile";
    }

    // LOGOUT METHOD

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session){
        session.invalidate();
        model.addAttribute("loginPageVisible", true);

        return "index";
    }

    // PROFILE PAGE FOR OTHER USERS

    @GetMapping("/gotofriend/{username}")
    public String goToFriend(HttpSession session, @PathVariable String username) {
        siteLogic.setFriendData(session, username);
        session.setAttribute("pageShown", 5);
        session.setAttribute("profileView", 0);

        siteLogic.clearSubList(session);
        return "UserProfile";

    }


    // Add & Remove methods


    @GetMapping("/addFriend/{id}")
    public String addFriend(Model model, HttpSession session, @PathVariable String id){
        if(userRepository.addFriend(session, Integer.parseInt(id))) {
            siteLogic.addToFriendList(session, Integer.parseInt(id));
        }
        String[] allChoices = {"Name", "Username", "Country"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        return "UserProfile";
    }

    @GetMapping("/addSeenMovie/{id}")
    public String addSeenMovie(Model model, HttpSession session, @PathVariable String id){
        if(movieRepository.addMovieToMovieList(session, Integer.parseInt(id), "hasSeen")) {
            siteLogic.addToSeenMovieList(session, Integer.parseInt(id));
        }
        String[] allChoices = {"Title", "Director", "Actor", "Genre", "Country", "Language", "Year"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        return "UserProfile";
    }

    @GetMapping("/addWishedMovie/{id}")
    public String addWishedMovie(Model model, HttpSession session, @PathVariable String id){
        if(movieRepository.addMovieToMovieList(session, Integer.parseInt(id), "wished")) {
            siteLogic.addToMovieWishList(session, Integer.parseInt(id));
        }
        String[] allChoices = {"Title", "Director", "Actor", "Genre", "Country", "Language", "Year"};
        model.addAttribute("allChoices", allChoices);
        String[] allTypes = {"Precise", "Contains"};
        model.addAttribute("allTypes", allTypes);
        return "UserProfile";
    }

    @GetMapping("/removeSeenMovie/{id}")
    public String removeSeenMovie(HttpSession session, @PathVariable String id){
        if( movieRepository.removeMovieFromMovieList(session, Integer.parseInt(id), "hasSeen")) {
            siteLogic.removeFromSeenMovieList(session, Integer.parseInt(id));
        }
        return "UserProfile";
    }

    @GetMapping("/removeWishedMovie/{id}")
    public String removeWishedMovie(HttpSession session, @PathVariable String id){
        if(movieRepository.removeMovieFromMovieList(session, Integer.parseInt(id), "wished")) {
            siteLogic.removeFromMovieWishList(session, Integer.parseInt(id));
        }
        return "UserProfile";
    }

    @GetMapping("/removeFriend/{id}")
    public String removeFriend(HttpSession session, @PathVariable String id){
        if(userRepository.removeFriend(session, Integer.parseInt(id))) {
            siteLogic.removeFromFriendList(session, Integer.parseInt(id));
        }
        return "UserProfile";
    }

/*
    @GetMapping("/gotomovie/{id}")
    public String goToMovie(HttpSession session, @PathVariable String id) {
        Movie movie = movieRepository.getMovieById(Integer.parseInt(id));
        session.setAttribute("activeMovie", movie);
        siteLogic.clearSubList(session);
        return "UserProfile";

    }
*/


}
