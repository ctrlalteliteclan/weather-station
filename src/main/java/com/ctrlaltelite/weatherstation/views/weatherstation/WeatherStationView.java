package com.ctrlaltelite.weatherstation.views.weatherstation;

import com.ctrlaltelite.weatherstation.data.entity.WeatherEntry;
import com.ctrlaltelite.weatherstation.data.service.WeatherEntryService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Weather Station")
@Route(value = "station/:weatherEntryID?/:action?(edit)")
@RouteAlias(value = "")
public class WeatherStationView extends Div implements BeforeEnterObserver {

    private final String WEATHERENTRY_ID = "weatherEntryID";
    private final String WEATHERENTRY_EDIT_ROUTE_TEMPLATE = "station/%s/edit";

    private final Grid<WeatherEntry> grid = new Grid<>(WeatherEntry.class, false);

    private TextField temperature;
    private DateTimePicker time;
    private TextField location;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<WeatherEntry> binder;

    private WeatherEntry weatherEntry;

    private final WeatherEntryService weatherEntryService;

    public WeatherStationView(WeatherEntryService weatherEntryService) {
        this.weatherEntryService = weatherEntryService;
        addClassNames("weather-station-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("temperature").setAutoWidth(true);
        grid.addColumn("time").setAutoWidth(true);
        grid.addColumn("location").setAutoWidth(true);
        grid.setItems(query -> weatherEntryService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(WEATHERENTRY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(WeatherStationView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(WeatherEntry.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(temperature).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("temperature");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.weatherEntry == null) {
                    this.weatherEntry = new WeatherEntry();
                }
                binder.writeBean(this.weatherEntry);
                weatherEntryService.update(this.weatherEntry);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(WeatherStationView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> weatherEntryId = event.getRouteParameters().get(WEATHERENTRY_ID).map(Long::parseLong);
        if (weatherEntryId.isPresent()) {
            Optional<WeatherEntry> weatherEntryFromBackend = weatherEntryService.get(weatherEntryId.get());
            if (weatherEntryFromBackend.isPresent()) {
                populateForm(weatherEntryFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested weatherEntry was not found, ID = %s", weatherEntryId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(WeatherStationView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        temperature = new TextField("Temperature");
        time = new DateTimePicker("Time");
        time.setStep(Duration.ofSeconds(1));
        location = new TextField("Location");
        formLayout.add(temperature, time, location);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(WeatherEntry value) {
        this.weatherEntry = value;
        binder.readBean(this.weatherEntry);

    }
}
