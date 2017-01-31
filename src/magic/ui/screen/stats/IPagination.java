package magic.ui.screen.stats;

public interface IPagination {
    public void displayFirstPage();
    public void displayPreviousPage();
    public void displayNextPage();
    public void displayLastPage();
    public int getPageNum();
    public int getTotalPages();
    public boolean hasPrevPage();
    public boolean hasNextPage();
}
