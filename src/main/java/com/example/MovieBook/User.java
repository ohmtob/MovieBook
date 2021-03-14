package com.example.MovieBook;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private char gender;
    private int age;
    private String username;
    private String country;
    private String emailAddress;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, char gender, int age, String username, String country, String emailAddress, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setAge(age);
        setUsername(username);
        setCountry(country);
        setEmailAddress(emailAddress);
        setPassword(password);
    }

    public User(long id, String firstName, String lastName, char gender, int age, String username, String country, String emailAddress, String password) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setAge(age);
        setUsername(username);
        setCountry(country);
        setEmailAddress(emailAddress);
        setPassword(password);
    }

    public User(int id, String firstName, String lastName, char gender, int age, String username, String country) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setAge(age);
        setUsername(username);
        setCountry(country);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
