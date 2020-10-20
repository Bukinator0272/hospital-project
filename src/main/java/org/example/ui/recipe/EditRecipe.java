package org.example.ui.recipe;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.example.model.dao.DoctorDAOImpl;
import org.example.model.dao.PatientDAOImpl;
import org.example.model.dao.RecipeDAOImpl;
import org.example.model.entity.Doctor;
import org.example.model.entity.Patient;
import org.example.model.entity.Recipe;
import org.example.ui.MainView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditRecipe extends Window {

    private final Recipe recipe;
    private FormLayout formLayout;
    private TextField description;
    private ComboBox<String> patientComboBox;
    private ComboBox<String> doctorComboBox;
    private DateField periodOfValidity;
    private RadioButtonGroup<String> priorityRadioButton;
    private HorizontalLayout buttonsLayout;
    private DoctorDAOImpl doctorDAO;
    private PatientDAOImpl patientDAO;
    private Map<Long, String> doctorsFullName;
    private Map<Long, String> patientsFullName;
    private Button confirmButton;
    private Button closeButton;
    private boolean isDescriptionChanged;
    private boolean isPatientComboBoxChanged;
    private boolean isDoctorComboBoxChanged;
    private boolean isPeriodOfValidityChanged;
    private boolean isPriorityRadioButtonChanged;

    public EditRecipe(Recipe recipe) {
        super("Редактирование рецепта");
        setHeight("500px");
        setWidth("500px");
        setStyleName("window");
        center();
        setDraggable(true);
        setResizable(false);
        setClosable(false);
        setModal(true);

        this.recipe = recipe;

        initForm();
        initButtons();

        formLayout.addComponents(
                description,
                patientComboBox,
                doctorComboBox,
                periodOfValidity,
                priorityRadioButton
        );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        verticalLayout.addComponents(formLayout, buttonsLayout);

        setContent(verticalLayout);
    }

    private void initForm() {
        doctorDAO = new DoctorDAOImpl();
        patientDAO = new PatientDAOImpl();

        formLayout = new FormLayout();
        formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        description = new TextField("Описание");
        description.setPlaceholder(recipe.getDescription());

        initPatientComboBox();
        initDoctorComboBox();

        periodOfValidity = new DateField("Действует до");
        periodOfValidity.setDateFormat("yyyy-MM-dd");
        periodOfValidity.setPlaceholder(recipe.getPeriodOfValidity().toString());

        priorityRadioButton = new RadioButtonGroup<>("Приоритет");
        priorityRadioButton.setItems("Нормальный", "Срочный", "Немедленный");

        initValueChangeListeners();
    }

    private void initPatientComboBox() {
        patientComboBox = new ComboBox<>("Выберите пациента");

        patientsFullName = new HashMap<>();
        List<Patient> patients = new ArrayList<>();

        try {
            patients = patientDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Patient temp : patients)
            patientsFullName.put(temp.getId(), temp.getSurname() + " " + temp.getName() + " " + temp.getPatronymic());

        patientComboBox.setItems(patientsFullName.values());
    }

    private void initDoctorComboBox() {
        doctorComboBox = new ComboBox<>("Выберите доктора");

        doctorsFullName = new HashMap<>();
        List<Doctor> doctors = new ArrayList<>();

        try {
            doctors = doctorDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Doctor temp : doctors)
            doctorsFullName.put(temp.getId(), temp.getSurname() + " " + temp.getName() + " " + temp.getPatronymic());

        doctorComboBox.setItems(doctorsFullName.values());
    }

    private void initButtons() {
        confirmButton = new Button("OK");
        confirmButton.addStyleName("friendly");
        confirmButton.setEnabled(false);

        closeButton = new Button("Отменить");
        closeButton.addStyleName("danger");
        closeButton.setEnabled(true);

        initCloseButtonEvents();
        initConfirmButtonEvents();

        buttonsLayout = new HorizontalLayout();
        buttonsLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        buttonsLayout.addComponents(confirmButton, closeButton);
    }

    void initCloseButtonEvents() {
        closeButton.addClickListener(clickEvent -> close());
        closeButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    void initConfirmButtonEvents() {
        confirmButton.addClickListener(clickEvent -> {
            setInput();
            try {
                new RecipeDAOImpl().update(recipe);
                ((MainView) UI.getCurrent()).getViewRecipe().update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            close();
        });
        confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void setInput() {
        if (!description.isEmpty())
            recipe.setDescription(description.getValue());
        if (patientComboBox.getValue() != null) {
            recipe.setPatient(setPatient());
        }
        if (doctorComboBox.getValue() != null) {
            recipe.setDoctor(setDoctor());
        }
        if (!periodOfValidity.isEmpty())
            recipe.setPeriodOfValidity(periodOfValidity.getValue());
        if (priorityRadioButton.getSelectedItem().isPresent())
            recipe.setPriority(priorityRadioButton.getSelectedItem().get());
    }

    private Patient setPatient() {
        Long tempPatientId = null;
        for (Map.Entry<Long, String> temp : patientsFullName.entrySet()) {
            if (temp.getValue().equals(patientComboBox.getValue()))
                tempPatientId = temp.getKey();
        }
        try {
            assert tempPatientId != null;
            return patientDAO.get(tempPatientId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Doctor setDoctor() {
        Long tempDoctorId = null;
        for (Map.Entry<Long, String> temp : doctorsFullName.entrySet()) {
            if (temp.getValue().equals(doctorComboBox.getValue())) {
                tempDoctorId = temp.getKey();
            }
        }
        try {
            assert tempDoctorId != null;
            return doctorDAO.get(tempDoctorId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initValueChangeListeners() {
        description.addValueChangeListener(valueChangeEvent -> {
            isDescriptionChanged = !description.isEmpty();
            formHasChanged();
        });
        patientComboBox.addValueChangeListener(valueChangeEvent -> {
            isPatientComboBoxChanged = !patientComboBox.isEmpty();
            formHasChanged();
        });
        doctorComboBox.addValueChangeListener(valueChangeEvent -> {
            isDoctorComboBoxChanged = !doctorComboBox.isEmpty();
            formHasChanged();
        });
        periodOfValidity.addValueChangeListener(valueChangeEvent -> {
            isPeriodOfValidityChanged = !periodOfValidity.isEmpty();
            formHasChanged();
        });
        priorityRadioButton.addValueChangeListener(valueChangeEvent -> {
            isPriorityRadioButtonChanged = !priorityRadioButton.isEmpty();
            formHasChanged();
        });
    }

    private void formHasChanged() {
        confirmButton.setEnabled(isDescriptionChanged || isPatientComboBoxChanged || isDoctorComboBoxChanged ||
                isPeriodOfValidityChanged || isPriorityRadioButtonChanged);
    }


}
