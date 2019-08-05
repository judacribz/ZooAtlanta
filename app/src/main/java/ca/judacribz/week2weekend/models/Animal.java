package ca.judacribz.week2weekend.models;

public class Animal {
    String
            name,
            description,
            imgUrl,

            scientificName,
            diet,
            status,
            viewingHints,
            range,
            habitat;

    public Animal(String name,
                  String scientificName, String diet, String status, String range, String imgUrl) {
        this.name = name;
        this.scientificName = scientificName;
        this.diet = diet;
        this.status = status;
        this.range = range;
this.imgUrl = imgUrl;

    }


    public Animal(String name,
                  String description,
                  String imgUrl,
                  String scientificName,
                  String diet,
                  String status,
                  String viewingHints,
                  String range,
                  String habitat) {
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

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}
