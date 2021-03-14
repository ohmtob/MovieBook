package com.example.MovieBook;

public class Movie {
    private long id;
    private String title;
    private String country;
    private String movieLanguage;
    private int year;
    private boolean forChildren;

    Movie(String title, String country, String movieLanguage, int year, boolean forChildren) {
        setTitle(title);
        setCountry(country);
        setMovieLanguage(movieLanguage);
        setYear(year);
        setForChildren(forChildren);
    }

    Movie(long id, String title, String country, String movieLanguage, int year, boolean forChildren) {
        setId(id);
        setTitle(title);
        setCountry(country);
        setMovieLanguage(movieLanguage);
        setYear(year);
        setForChildren(forChildren);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public void setMovieLanguage(String movieLanguage) {
        this.movieLanguage = movieLanguage;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isForChildren() {
        return forChildren;
    }

    public void setForChildren(boolean forChildren) {
        this.forChildren = forChildren;
    }
}
