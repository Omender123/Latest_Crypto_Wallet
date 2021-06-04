package com.crypto.croytowallet.Model;

public class CurrencyModel {
    String symbols,countryName,Currency;

    public CurrencyModel(String symbols, String countryName, String currency) {
        this.symbols = symbols;
        this.countryName = countryName;
        Currency = currency;
    }

    public CurrencyModel() {
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }
}
