package org.example.ui.doctor;

import com.vaadin.ui.*;
import org.example.model.dao.DoctorDAOImpl;
import org.example.model.entity.Doctor;

import java.sql.SQLException;
import java.util.Map;

public class ViewDoctor extends VerticalLayout {

    private Grid<Doctor> grid;
    private HorizontalLayout buttonsLayout;
    private final DoctorDAOImpl doctorDAO;
    private Map<Long, Integer> statistics;
    private VerticalLayout statisticsLayout;

    public ViewDoctor() {
        super();
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        doctorDAO = new DoctorDAOImpl();

        initGrid();
        initButtons();

        addComponents(grid, buttonsLayout);

        update();
    }

    private void initGrid() {
        grid = new Grid<>();
        grid.setWidth("100%");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Doctor::getName).setCaption("Имя");
        grid.addColumn(Doctor::getSurname).setCaption("Фамилия");
        grid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
        grid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
    }

    private void initButtons() {
        buttonsLayout = new HorizontalLayout();
        buttonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button addButton = new Button("Добавить");
        addButton.addClickListener(clickEvent -> add());

        Button editButton = new Button("Изменить");
        editButton.setDescription("Выберите доктора в таблице");
        editButton.addClickListener(clickEvent -> edit());

        Button deleteButton = new Button("Удалить");
        deleteButton.setDescription("Выберите доктора в таблице");
        deleteButton.addClickListener(clickEvent -> delete());

        Button statisticButton = new Button("Показать статистику");
        statisticButton.addClickListener(clickEvent -> showStatistics());

        buttonsLayout.addComponents(addButton, editButton, deleteButton, statisticButton);
        addComponents(buttonsLayout);
    }

    public void update() {
        try {
            grid.setItems(doctorDAO.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void add() {
        removeStatistics();
        UI.getCurrent().addWindow(new AddDoctor());
    }

    private void edit() {
        removeStatistics();
        if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
            UI.getCurrent().addWindow(new EditDoctor(grid.getSelectionModel().getFirstSelectedItem().get()));
    }

    private void delete() {
        removeStatistics();
        try {
            if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
                doctorDAO.delete(grid.getSelectionModel().getFirstSelectedItem().get());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        update();
    }

    private void showStatistics() {
        if (statisticsLayout != null)
            removeStatistics();
        statisticsLayout = new VerticalLayout();
        statisticsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        try {
            statistics = doctorDAO.getStatistics();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Long, Integer> temp : statistics.entrySet()) {
            try {
                addComponent(new Label(
                        doctorDAO.get(temp.getKey()).getSurname() + " " +
                                doctorDAO.get(temp.getKey()).getName() + " " +
                                doctorDAO.get(temp.getKey()).getPatronymic() + " " +
                                "выписал(а) " + temp.getValue() + " рецепта(ов)"
                ));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        addComponent(statisticsLayout);
    }

    private void removeStatistics() {
        removeAllComponents();
        addComponents(grid, buttonsLayout);
    }

}
