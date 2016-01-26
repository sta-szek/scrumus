package edu.piotrjonski.scrumus.ui.view;

import lombok.Data;
import org.primefaces.model.DefaultDashboardColumn;

import java.util.List;

@Data
public class DashboardTitledColumn extends DefaultDashboardColumn {

    private String title;

    public DashboardTitledColumn(String title) {
        this.title = title;
    }

    public void addWidgets(List<String> widgetsIds) {
        widgetsIds.forEach(this::addWidget);
    }

}