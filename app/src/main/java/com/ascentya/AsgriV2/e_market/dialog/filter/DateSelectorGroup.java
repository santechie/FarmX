package com.ascentya.AsgriV2.e_market.dialog.filter;

class DateSelectorGroup extends FilterGroup{

    boolean enabled;
    String dateStart;
    String dateEnd;
    long startMillis, endMillis;
    long selectedStartDate, selectedEndDate;

    public DateSelectorGroup(boolean enabled, String dateStart, String dateEnd) {
        this.enabled = enabled;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(long endMillis) {
        this.endMillis = endMillis;
    }

    public long getSelectedStartDate() {
        return selectedStartDate;
    }

    public void setSelectedStartDate(long selectedStartDate) {
        this.selectedStartDate = selectedStartDate;
    }

    public long getSelectedEndDate() {
        return selectedEndDate;
    }

    public void setSelectedEndDate(long selectedEndDate) {
        this.selectedEndDate = selectedEndDate;
    }
}
