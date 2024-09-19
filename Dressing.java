public enum Dressing {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    RANCH,FRENCH,ITALIAN,GREEN_GODDESS,NA;

    public String toString(){
        return switch (this) {
            case RANCH -> "Ranch";
            case FRENCH -> "French";
            case ITALIAN -> "Italian";
            case GREEN_GODDESS -> "Green goddess";
            case NA -> "I don't mind...";
        };
    }

}

enum Meat {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    BEEF, CHICKEN, VEGAN, NA;

    public String toString(){
        return switch (this) {
            case BEEF -> "Beef";
            case CHICKEN -> "Chicken";
            case VEGAN -> "Vegan";
            case NA -> "Any meat will do...";
        };
    }


}

enum MenuType {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    SELECTTYPE,BURGER,SALAD;

    public String toString(){
        return switch (this) {
            case BURGER -> "Burger";
            case SALAD -> "Salad";
            case SELECTTYPE -> "Selecttype";
        };
    }


}








enum Sauce {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    TOMATO, GARLIC, AIOLI, BBQ, CHILLI, RANCH, SPECIAL, NA;

    public String toString(){
        return switch (this) {
            case TOMATO -> "Tomato";
            case GARLIC -> "Garlic";
            case AIOLI -> "Aioli (vegan friendly)";
            case BBQ -> "BBQ";
            case CHILLI -> "Chilli";
            case RANCH -> "Ranch";
            case SPECIAL -> "Special sauce";
            case NA -> "Any sauce will do...";
        };
    }

}


enum Filter {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    TYPE,BUN,MEAT,CHEESE,PICKLES,CUCUMBER,TOMATO,DRESSING,LEAFY_GREENS,SAUCE_S,ANY;

    public String toString(){
        return switch (this) {
            case TYPE -> "Menu item type";
            case BUN -> "Bun/bread";
            case MEAT -> "Meat";
            case CHEESE -> "Cheese";
            case PICKLES -> "Pickles (gherkins)";
            case CUCUMBER -> "Cucumber (continental)";
            case TOMATO -> "Tomato";
            case DRESSING -> "Salad dressing";
            case LEAFY_GREENS -> "Leafy greens";
            case SAUCE_S -> "Sauces";
            case ANY -> "any";
        };
    }

}


