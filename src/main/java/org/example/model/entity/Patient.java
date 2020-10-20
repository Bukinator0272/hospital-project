package org.example.model.entity;

public class Patient extends Human {

    private String phoneNumber;

    public Patient(String name, String surname, String patronymic, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    public Patient(Long id, String name, String surname, String patronymic, String phoneNumber) {
        super(id, name, surname, patronymic);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
