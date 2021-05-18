package com.esprit.knowme_mobile.entities;


public class Ingredient {
    private Integer id;
    private String description;
    private Menu menu;

    public Ingredient() {

    }

    public Ingredient(String description, Menu menu) {
        this.description = description;
        this.menu = menu;

    }

    public Ingredient(Integer id, String description, Menu menu) {
        this.id = id;
        this.description = description;
        this.menu = menu;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", menu=" + menu +
                '}';
    }

}
