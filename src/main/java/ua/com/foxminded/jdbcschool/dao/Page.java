package ua.com.foxminded.jdbcschool.dao;

public class Page {
    private int pageNumber;
    private int amountOnPage;

    public Page(int pageNumber, int amountOnPage) {
        this.pageNumber = pageNumber;
        this.amountOnPage = amountOnPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getAmountOnPage() {
        return amountOnPage;
    }
}
