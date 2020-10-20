package org.example.ui.patient;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.example.model.dao.PatientDAOImpl;
import org.example.model.entity.Patient;
import org.example.ui.MainView;

import java.sql.SQLException;

public class AddPatient extends Window {

    private FormLayout formLayout;
    private TextField name;
    private TextField surname;
    private TextField patronymic;
    private TextField phoneNumber;
    private HorizontalLayout buttonsLayout;
    private Button confirmButton;
    private boolean isNameChanged;
    private boolean isSurnameChanged;
    private boolean isPatronymicChanged;
    private boolean isPhoneNumberChanged;

    public AddPatient() {
        super("Добавить пациента");
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

        formLayout.addComponents(name, surname, patronymic, phoneNumber);

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

        phoneNumber = new TextField("Номер телефона");
        phoneNumber.setRequiredIndicatorVisible(true);
        phoneNumber.setPlaceholder("Номер телефона");

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
                new PatientDAOImpl().add(buildPatient());
                ((MainView) UI.getCurrent()).getViewPatient().update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            close();
        });
        confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private Patient buildPatient() {
        return new Patient(
                name.getValue(),
                surname.getValue(),
                patronymic.getValue(),
                phoneNumber.getValue()
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
        phoneNumber.addValueChangeListener(valueChangeEvent -> {
            isPhoneNumberChanged = !phoneNumber.isEmpty();
            formHasChanged();
        });
    }

    private void formHasChanged() {
        confirmButton.setEnabled(isNameChanged && isSurnameChanged && isPatronymicChanged && isPhoneNumberChanged);
    }

}
