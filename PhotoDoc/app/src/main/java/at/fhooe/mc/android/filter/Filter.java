package at.fhooe.mc.android.filter;

/**
 * Datenbehälter für den FilterAdapter
 */
public class Filter {
    /**
     * enthält einen ImagePath um jedem Filter eine Image Datei aus den Assets zuzuweisen
     */
    private String imagePath;
    /**
     * Erklärung zum Filter
     */
    private int filterText;
    /**
     * eindeutige ID um dem Klick einen Filter zuzuordnen
     */
    private int id;

    public Filter(String imagePath, int filterText, int id) {
        this.imagePath = imagePath;
        this.filterText = filterText;
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getFilterText() {
        return filterText;
    }

    public int getId() { return id; }
}
