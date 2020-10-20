package org.example.ui.recipe;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.example.model.dao.RecipeDAOImpl;
import org.example.model.entity.Recipe;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class ViewRecipe extends VerticalLayout {

    private final RecipeDAOImpl recipeDAO;
    private HorizontalLayout filterLayout;
    private Grid<Recipe> grid;
    private HorizontalLayout buttonsLayout;

    public ViewRecipe() {
        super();
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        recipeDAO = new RecipeDAOImpl();

        initFilter();
        initGrid();
        initButtons();
        addComponents(filterLayout, grid, buttonsLayout);

        update();
    }

    private void initFilter() {
        filterLayout = new HorizontalLayout();
        filterLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label filterLabel = new Label("Фильтр: ");

        TextField patientTextField = new TextField();
        patientTextField.setPlaceholder("Пациент");

        TextField priorityTextField = new TextField();
        priorityTextField.setPlaceholder("Приоритет");

        TextField descriptionTextField = new TextField();
        descriptionTextField.setPlaceholder("Описание");

        Button confirmButton = new Button("Применить");
        confirmButton.addClickListener(clickEvent -> update(
                patientTextField.getValue(),
                priorityTextField.getValue(),
                descriptionTextField.getValue()
        ));
        confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        filterLayout.addComponents(filterLabel, patientTextField, priorityTextField, descriptionTextField, confirmButton);
    }

    private void initGrid() {
        grid = new Grid<>();
        grid.setWidth("100%");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addColumn(Recipe::getDescription).setCaption("Описание");

        Grid.Column<Recipe, String> patientsFullName = grid.addColumn(recipe ->
                recipe.getPatient().getSurname() + " " +
                        recipe.getPatient().getName() + " " +
                        recipe.getPatient().getPatronymic()
        );
        patientsFullName.setCaption("Пациент");

        Grid.Column<Recipe, String> doctorsFullName = grid.addColumn(recipe ->
                recipe.getDoctor().getSurname() + " " +
                        recipe.getDoctor().getName() + " " +
                        recipe.getDoctor().getPatronymic()
        );
        doctorsFullName.setCaption("Врач");

        grid.addColumn(Recipe::getCreationDate).setCaption("Дата создания");

        Grid.Column<Recipe, String> periodOfValidity = grid.addColumn(this::setDate);
        periodOfValidity.setCaption("Срок действия");

        grid.addColumn(Recipe::getPriority).setCaption("Приоритет");
    }

    private String setDate(Recipe recipe) {
        Period period = Period.between(recipe.getCreationDate(), recipe.getPeriodOfValidity());
        String date = "";
        if (period.isNegative() || period.isZero())
            return "Просрочен: " + recipe.getPeriodOfValidity();
        Period tempPeriod = Period.between(LocalDate.now(), recipe.getPeriodOfValidity());
        if (tempPeriod.isNegative())
            return "Просрочен: " + recipe.getPeriodOfValidity();
        if (period.getYears() != 0)
            date += period.getYears() + "г ";
        if (period.getMonths() != 0)
            date += period.getMonths() + "м ";
        if (period.getDays() != 0)
            date += period.getDays() + "д ";
        return date;
    }

    private void initButtons() {
        buttonsLayout = new HorizontalLayout();
        buttonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button addButton = new Button("Добавить");
        addButton.addClickListener(clickEvent -> add());

        Button editButton = new Button("Изменить");
        editButton.setDescription("Выберите рецепт в таблице");
        editButton.addClickListener(clickEvent -> edit());

        Button deleteButton = new Button("Удалить");
        deleteButton.setDescription("Выберите рецепт в таблице");
        deleteButton.addClickListener(clickEvent -> delete());

        buttonsLayout.addComponents(addButton, editButton, deleteButton);
        addComponents(buttonsLayout);
    }

    public void update() {
        try {
            grid.setItems(recipeDAO.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(String patient, String priority, String description) {
        try {
            grid.setItems(recipeDAO.getAll(patient, priority, description));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void add() {
        UI.getCurrent().addWindow(new AddRecipe());
    }

    private void edit() {
        if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
            UI.getCurrent().addWindow(new EditRecipe(grid.getSelectionModel().getFirstSelectedItem().get()));
    }

    private void delete() {
        try {
            if (grid.getSelectionModel().getFirstSelectedItem().isPresent())
                recipeDAO.delete(grid.getSelectionModel().getFirstSelectedItem().get());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        update();
    }

}
