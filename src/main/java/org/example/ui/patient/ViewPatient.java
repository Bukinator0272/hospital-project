package org.example.ui.patient;

import com.vaadin.ui.*;
import org.example.model.dao.PatientDAOImpl;
import org.example.model.entity.Patient;

import java.sql.SQLException;

public class ViewPatient extends VerticalLayout {

    private Grid<Patient> grid;
    private HorizontalLayout buttonsLayout;
    private final PatientDAOImpl patientDAO;

    public ViewPatient() {
        super();
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        patientDAO = new PatientDAOImpl();

        initGrid();
        initButtons();
        addComponents(grid, buttonsLayout);

        update();
    }

    private void initGrid() {
        grid = new Grid<>();
        grid.setWidth("100%");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Patient::getName).setCaption("Имя");
        grid.addColumn(Patient::getSurname).setCaption("Фамилия");
        grid.addColumn(Patient::getPatronymic).setCaption("Отчество");
        grid.addColumn(Patient::getPhoneNumber).setCaption("Номер телефона");
    }

    private void initButtons() {
        buttonsLayout = new HorizontalLayout();
        buttonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button addButton = new Button("Добавить");
        addButton.addClickListener(clickEvent -> add());

        Button editButton = new Button("Изменить");
        editButton.setDescription("Выберите пациента в таблице");
        editButton.addClickListener(clickEvent -> edit());

        Button deleteButton = new Button("Удалить");
        deleteButton.setDescription("Выберите пациента в таблице");
        deleteButton.addClickListener(clickEvent -> delete());

        buttonsLayout.addComponents(addButton, editButton, deleteButton);
        addComponents(buttonsLayout);
    }

    public void update() {
        try {
            grid.setItems(patientDAO.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void add() {
        UI.getCurrent().addWindow(new AddPatient());
    }

    private void edit() {
        if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
            UI.getCurrent().addWindow(new EditPatient(grid.getSelectionModel().getFirstSelectedItem().get()));
    }

    private void delete() {
        try {
            if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
                patientDAO.delete(grid.getSelectionModel().getFirstSelectedItem().get());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        update();
    }

}
