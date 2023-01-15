package com.uniproject.application.views.list;

import com.uniproject.application.data.entity.Status;
import com.uniproject.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RolesAllowed({"ROLE_USER","ROLE_ADMIN"})
@Route(value = "list_of_statuses", layout = MainLayout.class)
@PageTitle("Statuses | Vaadin Uni Project")
    public class StatusView extends VerticalLayout {
        CrmService service;
        Binder<Status> binder = new Binder<>(Status.class);
        Grid<Status> statusGrid = new Grid<>(Status.class);

        public StatusView(CrmService service){
            this.service = service;
            addClassName("status-list-view");
            setSizeFull();
            StatusGrid();
            //add(new H1("Hello World!"));

            add(getContent());

        }

        private Component getContent(){
            HorizontalLayout content = new HorizontalLayout(statusGrid);
            content.setFlexGrow(2,statusGrid);
            content.addClassName("content");
            content.setSizeFull();

            return content;
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
    }