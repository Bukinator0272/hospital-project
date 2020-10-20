package org.example.model.entity;

public class Doctor extends Human {

    private String specialization;

    public Doctor(String name, String surname, String patronymic, String specialization) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Doctor(Long id, String name, String surname, String patronymic, String specialization) {
        super(id, name, surname, patronymic);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
