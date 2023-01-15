package com.uniproject.application.views.list;

import com.uniproject.application.data.entity.Company;
import com.uniproject.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@RolesAllowed({"ROLE_USER","ROLE_ADMIN"})
@Route(value = "list_of_companies", layout = MainLayout.class)
@PageTitle("Companies | Vaadin Uni Project")
public class CompanyView extends VerticalLayout {
    Grid<Company> grid = new Grid<>(Company.class);
    CompanyForm form;
    CrmService service;


    public CompanyView(CrmService service) {
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
        form = new CompanyForm(service.findAllCompanies());
        form.setWidth("25em");
        form.addListener(CompanyForm.SaveEvent.class, this::saveCompany);
        form.addListener(CompanyForm.DeleteEvent.class, this::deleteCompany);
        form.addListener(CompanyForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveCompany(CompanyForm.SaveEvent event) {
        service.saveCompany(event.getCompany());
        updateList();
        closeEditor();
    }

    private void deleteCompany(CompanyForm.DeleteEvent event) {
        service.deleteCompany(event.getCompany());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.addColumn(company -> company.getName()).setHeader("Company").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(false));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCompany(event.getValue()));
    }

    private HorizontalLayout getToolbar() {

        Button addContactButton = new Button("Add company");
        addContactButton.addClickListener(click -> addCompany());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void editCompany(Company company){
        if (company == null) {
            closeEditor();
        } else {
            form.setCompany(company);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setCompany(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addCompany() {
        grid.asSingleSelect().clear();
        editCompany(new Company());
    }

    private void updateList(){
        grid.setItems(service.findAllCompanies());
    }
}