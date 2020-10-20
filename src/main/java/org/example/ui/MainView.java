package org.example.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.example.ui.doctor.ViewDoctor;
import org.example.ui.patient.ViewPatient;
import org.example.ui.recipe.ViewRecipe;

import javax.servlet.annotation.WebServlet;


@Theme("mytheme")
public class MainView extends UI {

    private VerticalLayout rootLayout;
    private HorizontalLayout buttonsLayout;
    private ViewDoctor viewDoctor;
    private ViewPatient viewPatient;
    private ViewRecipe viewRecipe;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initLayout();
        initButtons();
    }

    private void initLayout() {
        rootLayout = new VerticalLayout();
        rootLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(rootLayout);
    }

    private void initButtons() {
        buttonsLayout = new HorizontalLayout();

        Button doctorButton = new Button("Просмотр докторов");
        doctorButton.addClickListener(clickEvent -> viewDoctors());

        Button patientButton = new Button("Просмотр пациентов");
        patientButton.addClickListener(clickEvent -> viewPatients());

        Button recipeButton = new Button("Просмотр рецептов");
        recipeButton.addClickListener(clickEvent -> viewRecipes());

        buttonsLayout.addComponents(doctorButton, patientButton, recipeButton);
        rootLayout.addComponent(buttonsLayout);
    }

    public void viewDoctors() {
        refresh();
        if (viewDoctor == null || rootLayout.getComponentCount() == 1) {
            viewDoctor = new ViewDoctor();
            rootLayout.addComponent(viewDoctor);
        }
        viewDoctor.update();
    }

    private void viewPatients() {
        refresh();
        if (viewPatient == null || rootLayout.getComponentCount() == 1) {
            viewPatient = new ViewPatient();
            rootLayout.addComponent(viewPatient);
        }
        viewPatient.update();
    }

    private void viewRecipes() {
        refresh();
        if (viewRecipe == null || rootLayout.getComponentCount() == 1) {
            viewRecipe = new ViewRecipe();
            rootLayout.addComponent(viewRecipe);
        }
        viewRecipe.update();
    }

    private void refresh() {
        rootLayout.removeAllComponents();
        rootLayout.addComponent(buttonsLayout);
    }

    public ViewDoctor getViewDoctor() {
        return viewDoctor;
    }

    public ViewPatient getViewPatient() {
        return viewPatient;
    }

    public ViewRecipe getViewRecipe() {
        return viewRecipe;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainView.class, productionMode = true)
    public static class MainViewServlet extends VaadinServlet {

    }

}


