package com.uniproject.application.views.list;

import com.uniproject.application.data.entity.Contact;
import com.uniproject.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_USER","ROLE_ADMIN"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin Uni Project")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterNameText = new TextField();
    TextField filterEmailText = new TextField();
    ContactForm form;
    CrmService service;


    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status").setSortable(true);
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(false));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterNameText.setPlaceholder("Filter by name...");
        filterNameText.setClearButtonVisible(true);
        filterNameText.setValueChangeMode(ValueChangeMode.LAZY);
        filterNameText.addValueChangeListener(e -> updateList());

        filterEmailText.setPlaceholder("Filter by email...");
        filterEmailText.setClearButtonVisible(true);
        filterEmailText.setValueChangeMode(ValueChangeMode.LAZY);
        filterEmailText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact");
        addContactButton.addClickListener(click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterNameText, filterEmailText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void editContact(Contact contact){
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void updateList(){
        grid.setItems(service.findAllContacts(filterEmailText.getValue(),filterNameText.getValue()));
    }
}