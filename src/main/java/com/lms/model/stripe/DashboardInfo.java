package com.lms.model.stripe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardInfo {
    private int countUsers;
    private int countNewUsers;
    private int countAuthors;
    private int countNewAuthors;
    private int countCategories;
    private int countNewCategories;
    private int countBooks;
    private int countNewBooks;
}
