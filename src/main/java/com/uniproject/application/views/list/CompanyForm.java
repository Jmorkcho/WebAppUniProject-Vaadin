package com.uniproject.application.views.list;

import com.uniproject.application.data.entity.Company;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class CompanyForm extends FormLayout {
    ComboBox<Company> name = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Company> binder = new Binder<>(Company.class);
    private Company company1;

    public CompanyForm(List<Company> companies) {
        addClassName("company-form");
        binder.bind(name, "name");
        name.setItems(companies);
        name.setItemLabelGenerator(Company::getName);

        add(name,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, company1)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(company1);
            fireEvent(new SaveEvent(this, company1));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    public void setCompany(Company company1) {
        //this.name = name;
        binder.readBean(name.getValue());
    }

    // Events
    public static abstract class CompanyFormEvent extends ComponentEvent<CompanyForm> {
        private Company company;

        protected CompanyFormEvent(CompanyForm source, Company company) {
            super(source, false);
            this.company = company;
        }

        public Company getCompany() {return company;}
    }

    public static class SaveEvent extends CompanyFormEvent {
        SaveEvent(CompanyForm source, Company company) {
            super(source, company);
        }
    }

    public static class DeleteEvent extends CompanyFormEvent {
        DeleteEvent(CompanyForm source, Company company) {
            super(source, company);
        }

    }

    public static class CloseEvent extends CompanyFormEvent {
        CloseEvent(CompanyForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

