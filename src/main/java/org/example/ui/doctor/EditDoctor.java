package org.example.ui.doctor;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.example.model.dao.DoctorDAOImpl;
import org.example.model.entity.Doctor;
import org.example.ui.MainView;

import java.sql.SQLException;

public class EditDoctor extends Window {

    private final Doctor doctor;
    private FormLayout formLayout;
    private TextField name;
    private TextField surname;
    private TextField patronymic;
    private TextField specialization;
    private HorizontalLayout buttonsLayout;
    private Button confirmButton;
    private boolean isNameChanged;
    private boolean isSurnameChanged;
    private boolean isPatronymicChanged;
    private boolean isSpecializationChanged;

    public EditDoctor(Doctor doctor) {
        super("Редактирование доктора");
        setHeight("400px");
        setWidth("400px");
        setStyleName("window");
        center();
        setDraggable(true);
        setResizable(false);
        setClosable(false);
        setModal(true);

        this.doctor = doctor;

        initForm();
        initButtons();

        formLayout.addComponents(name, surname, patronymic, specialization);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        verticalLayout.addComponents(formLayout, buttonsLayout);

        setContent(verticalLayout);
    }

    private void initForm() {
        formLayout = new FormLayout();
        formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        name = new TextField("Имя");
        name.setPlaceholder(doctor.getName());

        surname = new TextField("Фамилия");
        surname.setPlaceholder(doctor.getSurname());

        patronymic = new TextField("Отчество");
        patronymic.setPlaceholder(doctor.getPatronymic());

        specialization = new TextField("Специализация");
        specialization.setPlaceholder(doctor.getSpecialization());

        initValueChangeListeners();
    }

    private void initButtons() {
        confirmButton = new Button("OK");
        confirmButton.setEnabled(false);
        confirmButton.addStyleName("friendly");

        Button closeButton = new Button("Отменить");
        closeButton.addStyleName("danger");

        initCloseButtonEvents(closeButton);
        initConfirmButtonEvents();

        buttonsLayout = new HorizontalLayout();
        buttonsLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        buttonsLayout.addComponents(confirmButton, closeButton);
    }

    void initCloseButtonEvents(Button closeButton) {
        closeButton.addClickListener(clickEvent -> close());
        closeButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    void initConfirmButtonEvents() {
        confirmButton.addClickListener(clickEvent -> {
            setInput();
            try {
                new DoctorDAOImpl().update(doctor);
                ((MainView) UI.getCurrent()).getViewDoctor().update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            close();
        });
        confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void setInput() {
        if (!name.isEmpty())
            doctor.setName(name.getValue());
        if (!surname.isEmpty())
            doctor.setSurname(surname.getValue());
        if (!patronymic.isEmpty())
            doctor.setPatronymic(patronymic.getValue());
        if (!specialization.isEmpty())
            doctor.setSpecialization(specialization.getValue());
    }

    private void initValueChangeListeners() {
        name.addValueChangeListener(valueChangeEvent -> {
            isNameChanged = !name.isEmpty();
            formHasChanged();
        });
        surname.addValueChangeListener(valueChangeEvent -> {
            isSurnameChanged = !surname.isEmpty();
            formHasChanged();
        });
        patronymic.addValueChangeListener(valueChangeEvent -> {
            isPatronymicChanged = !patronymic.isEmpty();
            formHasChanged();
        });
        specialization.addValueChangeListener(valueChangeEvent -> {
            isSpecializationChanged = !specialization.isEmpty();
            formHasChanged();
        });
    }

    private void formHasChanged() {
        confirmButton.setEnabled(isNameChanged || isSurnameChanged || isPatronymicChanged || isSpecializationChanged);
    }

}
