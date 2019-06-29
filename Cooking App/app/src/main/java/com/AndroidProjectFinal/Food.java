package com.AndroidProjectFinal;

// klasa e cila perdoret per lidhje me tabelen Food ne databaze
public class Food
{
    private String CategoryId, Image, Name, Rate, Ingredients, Directions, Video,Prep,Difficulty,HowMany;

    public Food()
    {

    }

    public Food(String categoryId, String image, String name, String rate, String ingredients, String directions, String video,String prep,String difficulty,String howmany) {
        CategoryId = categoryId;
        Image = image;
        Name = name;
        Rate = rate;
        Ingredients = ingredients;
        Directions = directions;
        Video = video;
        Difficulty = difficulty;
        HowMany = howmany;
        Prep = prep;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public String getDirections() {
        return Directions;
    }

    public void setDirections(String directions) {
        Directions = directions;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getPrep() {
        return Prep;
    }

    public void setPrep(String prep) {
        Prep = prep;
    }

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public String getHowMany() {
        return HowMany;
    }

    public void setHowMany(String howMany) {
        HowMany = howMany;
    }
}