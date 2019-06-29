package com.AndroidProjectFinal;

// klasa e cila perdoret per lidhje me tabelen Rating ne databaze
public class Rating
{
    private String username;
    private String foodId;
    private String rateValue;
    private String comment;

    public Rating()
    {

    }

    public Rating(String username, String foodId, String rateValue, String comment) {
        this.username = username;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
