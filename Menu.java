import java.util.*;

public class Menu {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    private final Set<MenuItem> menu = new HashSet<>();

    public void addItem(MenuItem menuItem){
        this.menu.add(menuItem);
    }

    public Set<Object> getAllIngredientTypes(Filter filter){
        Set<Object> allSubtypes = new LinkedHashSet<>();
        for(MenuItem menuItem: menu){
            if(menuItem.getDreamMenuItem().getAllFilters().containsKey(filter)){
                var ingredientTypes = menuItem.getDreamMenuItem().getFilter(filter);
                System.out.println(ingredientTypes);
                if(ingredientTypes instanceof Collection<?>) allSubtypes.addAll((Collection<?>) ingredientTypes);
                else allSubtypes.add(menuItem.getDreamMenuItem().getFilter(filter));
            }
        }
        allSubtypes.add("I don't mind");
        return allSubtypes;
    }

    public List<MenuItem> findMatch(DreamMenuItem dreamMenuItem) {
        List<MenuItem> matching = new ArrayList<>();
        for (MenuItem menuItem : menu) {
            System.out.println("Checking item: " + menuItem.getMenuItemName());

            // Check if the item matches the user's filter criteria
            if (!menuItem.getDreamMenuItem().matches(dreamMenuItem)) {
                System.out.println("Item did not match: " + menuItem.getMenuItemName());
                continue;
            }

            // Check if the item is within the price range
            if (menuItem.getPrice() < dreamMenuItem.getMinPrice() || menuItem.getPrice() > dreamMenuItem.getMaxPrice()) {
                System.out.println("Price mismatch for item: " + menuItem.getMenuItemName() + " | Item Price: " + menuItem.getPrice() + ", Min Price: " + dreamMenuItem.getMinPrice() + ", Max Price: " + dreamMenuItem.getMaxPrice());
                continue;
            }

            System.out.println("Item matched: " + menuItem.getMenuItemName());
            matching.add(menuItem);
        }

        System.out.println("Number of matching items found: " + matching.size());
        return matching;
    }


}
