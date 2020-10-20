package org.example.ui.doctor;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.example.model.dao.DoctorDAOImpl;
import org.example.model.entity.Doctor;
import org.example.ui.MainView;

import java.sql.SQLException;

public class AddDoctor extends Window {

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

    public AddDoctor() {
        super("Добавить доктора");
        setHeight("400px");
        setWidth("400px");
        setStyleName("window");
        center();
        setDraggable(true);
        setResizable(false);
        setClosable(false);
        setModal(true);

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
        name.setRequiredIndicatorVisible(true);
        name.setPlaceholder("Имя");

        surname = new TextField("Фамилия");
        surname.setRequiredIndicatorVisible(true);
        surname.setPlaceholder("Фамилия");

        patronymic = new TextField("Отчество");
        patronymic.setRequiredIndicatorVisible(true);
        patronymic.setPlaceholder("Отчество");

        specialization = new TextField("Специализация");
        specialization.setRequiredIndicatorVisible(true);
        specialization.setPlaceholder("Специализация");

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
            try {
                new DoctorDAOImpl().add(buildDoctor());
                ((MainView) UI.getCurrent()).getViewDoctor().update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            close();
        });
        confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private Doctor buildDoctor() {
        return new Doctor(
                name.getValue(),
                surname.getValue(),
                patronymic.getValue(),
                specialization.getValue()
        );
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
        confirmButton.setEnabled(isNameChanged && isSurnameChanged && isPatronymicChanged && isSpecializationChanged);
    }

}
