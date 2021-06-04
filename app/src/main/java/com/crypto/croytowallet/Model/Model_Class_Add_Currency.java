package com.crypto.croytowallet.Model;

public class Model_Class_Add_Currency {
    String Currency_Title,Title_Des,Image,CoinId;
    Boolean checked;

    public Model_Class_Add_Currency(String currency_Title, String title_Des, String image, String coinId, Boolean checked) {
        this.Currency_Title = currency_Title;
        this.Title_Des = title_Des;
        this.Image = image;
        this.CoinId = coinId;
        this.checked = checked;
    }

    public Model_Class_Add_Currency() {
    }

    public String getCurrency_Title() {
        return Currency_Title;
    }

    public void setCurrency_Title(String currency_Title) {
        Currency_Title = currency_Title;
    }

    public String getTitle_Des() {
        return Title_Des;
    }

    public void setTitle_Des(String title_Des) {
        Title_Des = title_Des;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCoinId() {
        return CoinId;
    }

    public void setCoinId(String coinId) {
        CoinId = coinId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
