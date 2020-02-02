package ca.judacribz.week2weekend.models;

public class Category {
    String
            name,
            description;
    int numSpecies;

    public Category(String name, String description, int numSpecies) {
        this.name = name;
        this.description = description;
        this.numSpecies = numSpecies;
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

    public int getNumSpecies() {
        return numSpecies;
    }

    public void setNumSpecies(int numSpecies) {
        this.numSpecies = numSpecies;
    }
}
