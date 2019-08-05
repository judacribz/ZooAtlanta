package ca.judacribz.week2weekend.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Animal implements Parcelable {
    private static final String
            RETURN = " Returning",
            COMING = " Coming";

    private String
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
        setName(name);
        this.scientificName = scientificName;
        this.diet = diet;
        this.status = status;
        this.range = range;
        this.imgUrl = imgUrl;
    }

    protected Animal(Parcel in) {
        name = in.readString();
        description = in.readString();
        imgUrl = in.readString();
        scientificName = in.readString();
        diet = in.readString();
        status = in.readString();
        viewingHints = in.readString();
        range = in.readString();
        habitat = in.readString();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.contains(RETURN)) {
            this.name = name.substring(0, name.indexOf(RETURN));
        } else if (name.contains(COMING)) {
            name.substring(0, name.indexOf(COMING));
        } else {
            this.name = name;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imgUrl);
        dest.writeString(scientificName);
        dest.writeString(diet);
        dest.writeString(status);
        dest.writeString(viewingHints);
        dest.writeString(range);
        dest.writeString(habitat);
    }
}
