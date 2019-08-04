package ca.judacribz.week2weekend.models;

import java.util.ArrayList;

public class Animal {
    String
            name,
            description,
            imgUrl,
            scientificName,
            diet,
            status,
            viewingHints;
    ArrayList<String>
            range,
            habitat;

    public Animal(String name,
                  String description,
                  String imgUrl,
                  String scientificName,
                  String diet,
                  String status,
                  String viewingHints,
                  ArrayList<String> range,
                  ArrayList<String> habitat) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.scientificName = scientificName;
        this.diet = diet;
        this.status = status;
        this.viewingHints = viewingHints;
        this.range = range;
        this.habitat = habitat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViewingHints() {
        return viewingHints;
    }

    public void setViewingHints(String viewingHints) {
        this.viewingHints = viewingHints;
    }

    public ArrayList<String> getRange() {
        return range;
    }

    public void setRange(ArrayList<String> range) {
        this.range = range;
    }

    public ArrayList<String> getHabitat() {
        return habitat;
    }

    public void setHabitat(ArrayList<String> habitat) {
        this.habitat = habitat;
    }
}
