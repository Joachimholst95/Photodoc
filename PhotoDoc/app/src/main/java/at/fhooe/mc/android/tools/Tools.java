package at.fhooe.mc.android.tools;

/**
 * Datenbehälter für den Toolsadapter
 */
public class Tools {
    /**
     * jedem Tool wird ein Image zugeordnet
     */
    private int imageId;
    /**
     * Text der unter dem Image angezeigt wird
     */
    private int toolText;
    /**
     * ID um beim Klicken zu erkennen auf welches Tool gedrückt wurde
     */
    private int id;

    public Tools(int imageId, int toolText, int id) {
        this.imageId = imageId;
        this.toolText = toolText;
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public int getToolText() {
        return toolText;
    }

    public int getId() { return id; }
}
