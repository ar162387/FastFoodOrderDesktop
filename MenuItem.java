import java.text.DecimalFormat;

/**
 * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
 */

public class MenuItem {

    //fields
    private final long menuItemIdentifier;
    private final String menuItemName;
    private final String description;
    private final double price;
    private final DreamMenuItem dreamMenuItem;
    private String customisationMessage;

    //constructor/s
    public MenuItem(long menuItemIdentifier, String menuItemName, double price, String description, DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = menuItemIdentifier;
        this.menuItemName = menuItemName;
        this.price = price;
        this.description = description;
        this.dreamMenuItem=dreamMenuItem;
    }

    public MenuItem(DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = 0;
        this.menuItemName = "CUSTOM ORDER";
        this.price = -1;
        this.description = "custom - see preferences";
        this.dreamMenuItem=dreamMenuItem;
        this.customisationMessage = "N/A";
    }

    //getters
    public long getMenuItemIdentifier() {
        return menuItemIdentifier;
    }
    public String getMenuItemName() {
        return menuItemName;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public DreamMenuItem getDreamMenuItem(){ return dreamMenuItem;}

    //menu info
    public String getMenuItemInformation(){
        DecimalFormat df = new DecimalFormat("0.00");
        String output = "\n*******************************************";
        if(getMenuItemIdentifier()!=0) output+="\n"+this.getMenuItemName()+" ("+getMenuItemIdentifier()+")"+ "\n"+this.getDescription();
        output+=getDreamMenuItem().getInfo();
        if(price==-1) return output;
        else return output+"\nPrice: $"+df.format(this.getPrice());
    }

    public String getCustomisationMessage() {
        return customisationMessage;
    }

    public void setCustomisationMessage(String customisationMessage) {
        this.customisationMessage = customisationMessage;
    }
}
