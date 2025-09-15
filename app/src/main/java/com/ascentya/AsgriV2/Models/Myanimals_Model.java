package com.ascentya.AsgriV2.Models;

public class Myanimals_Model {

    String animal_id, animal_name, animal_breed, animal_petname, animal_gender, animal_age, animal_prediseases, diseases_disc, created_at;

    public String getAnimal_id() {
        return animal_id;
    }

    public String getAnimal_breed() {
        return animal_breed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setAnimal_breed(String animal_breed) {
        this.animal_breed = animal_breed;
    }

    public String getAnimal_petname() {
        return animal_petname;
    }

    public void setAnimal_petname(String animal_petname) {
        this.animal_petname = animal_petname;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_gender() {
        return animal_gender;
    }

    public void setAnimal_gender(String animal_gender) {
        this.animal_gender = animal_gender;
    }

    public String getAnimal_age() {
        return animal_age;
    }

    public void setAnimal_age(String animal_age) {
        this.animal_age = animal_age;
    }

    public String getAnimal_prediseases() {
        return animal_prediseases;
    }

    public void setAnimal_prediseases(String animal_prediseases) {
        this.animal_prediseases = animal_prediseases;
    }

    public String getDiseases_disc() {
        return diseases_disc;
    }

    public void setDiseases_disc(String diseases_disc) {
        this.diseases_disc = diseases_disc;
    }
}
