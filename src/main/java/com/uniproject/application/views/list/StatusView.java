package com.uniproject.application.views.list;

import com.uniproject.application.data.entity.Status;
import com.uniproject.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "list_of_statuses", layout = MainLayout.class)
@PageTitle("Statuses | Vaadin Uni Project")
public class StatusView extends VerticalLayout {
    Grid<Status> statusGrid = new Grid<>(Status.class);
    StatusForm form;
    CrmService service;


    public StatusView(CrmService service) {
        this.service = service;
        addClassName("status-list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        //add(new H1("Hello World!"));

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(statusGrid, form);
        content.setFlexGrow(2, statusGrid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        form = new StatusForm(service.findAllStatuses());
        form.setWidth("25em");
        form.addListener(StatusForm.SaveEvent.class, this::saveStatus);
        form.addListener(StatusForm.DeleteEvent.class, this::deleteStatus);
        form.addListener(StatusForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveStatus(StatusForm.SaveEvent event) {
        service.saveStatus(event.getStatus());
        updateList();
        closeEditor();
    }


    private void deleteStatus(StatusForm.DeleteEvent event) {
        try {
            service.deleteStatus(event.getStatus());
            updateList();
            closeEditor();
        } catch (Exception e) {
            Dialog dialog = new Dialog();
            dialog.setCloseOnEsc(true);
            dialog.setCloseOnOutsideClick(true);
            dialog.add(String.format("Unable to delete status %s.", event.getStatus().getName()));
            dialog.open();
        }
    }

    private void configureGrid() {
        statusGrid.addClassNames("contact-grid");
        statusGrid.setSizeFull();
        //statusGrid.addColumn(Status::getName).setHeader("Status").setSortable(true);
        statusGrid.getColumns().forEach(col -> col.setAutoWidth(false));

        statusGrid.asSingleSelect().addValueChangeListener(status ->
                editStatus(status.getValue()));
    }


    public void StatusGrid() {
        String statusID = service.findAllStatuses().get(0).getName();
        statusGrid.setItems(service.findAllStatuses());

        List<Status> statusList = service.findAllStatuses();
        System.out.println(statusID);
        //statusGrid.addColumn(Status::getName).setHeader("Status");

        //binder.bindInstanceFields(this);
        //addClickListener(event -> binder.setBean(event.getItem()));
    }

    private HorizontalLayout getToolbar() {

        Button addStatusButton = new Button("Add status");
        addStatusButton.addClickListener(click -> addStatus());

        HorizontalLayout toolbar = new HorizontalLayout(addStatusButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void editStatus(Status status) {
        if (status == null) {
            closeEditor();
        } else {
            form.setStatus(status);
            form.setVisible(true);
            addClassName("editing");
        }
    }


    private void closeEditor() {
        form.setStatus(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private void addStatus() {
        statusGrid.asSingleSelect().clear();
        editStatus(new Status());
    }

    private void updateList() {
        statusGrid.setItems(service.findAllStatuses());
    }
}