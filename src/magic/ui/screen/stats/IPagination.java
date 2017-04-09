package magic.ui.screen.stats;

public interface IPagination {
    void displayFirstPage();
    void displayPreviousPage();
    void displayNextPage();
    void displayLastPage();
    int getPageNum();
    int getTotalPages();
    boolean hasPrevPage();
    boolean hasNextPage();
}
