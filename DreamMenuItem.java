import java.util.*;

public class DreamMenuItem {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    private final Map<Filter,Object> filterMap;
    private final double minPrice;
    private final double maxPrice;

    public DreamMenuItem(Map<Filter, Object> filterMap, double minPrice, double maxPrice) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }
    public DreamMenuItem(Map<Filter, Object> filterMap) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        this.minPrice=-1;
        this.maxPrice=-1;
    }

    public Map<Filter, Object> getAllFilters() {
        return new LinkedHashMap<>(filterMap);
    }
    public Object getFilter(Filter key){return getAllFilters().get(key);}
    public double getMinPrice() {
        return minPrice;
    }
    public double getMaxPrice() {
        return maxPrice;
    }

    public String getInfo(){
        StringBuilder description = new StringBuilder();
        StringBuilder extras = new StringBuilder("\nExtras: ");
        for(Filter key: filterMap.keySet()) {
            if(getFilter(key) instanceof Collection<?>){
                description.append("\n").append(key).append(":");
                for(Object x:((Collection<?>) getFilter(key)).toArray()) description.append("\n").append(" --> ").append(x);
            }
            else if(getFilter(key).equals(true)) extras.append(key).append(", ");
            else if(!getFilter(key).equals(false)) description.append("\n").append(key).append(": ").append(getFilter(key));
        }
        description.append(extras.substring(0,extras.length()-2));
        return description.toString();
    }

    public boolean matches(DreamMenuItem dreamMenuItem) {
        for (Filter key : dreamMenuItem.getAllFilters().keySet()) {
            if (this.getAllFilters().containsKey(key)) {
                Object itemFilterValue = this.getFilter(key);
                Object userFilterValue = dreamMenuItem.getFilter(key);

                System.out.println("Checking filter: " + key);

                // Check if the filter value is a collection
                if (userFilterValue instanceof Collection<?>) {
                    if (itemFilterValue instanceof Collection<?>) {
                        // Both values are collections
                        Set<Object> intersect = new HashSet<>((Collection<?>) userFilterValue);
                        intersect.retainAll((Collection<?>) itemFilterValue);

                        // Print debug information
                        System.out.println("Collection filter: " + key + ", User selected: " + userFilterValue + ", Item options: " + itemFilterValue + ", Intersection: " + intersect);

                        if (intersect.isEmpty()) {
                            System.out.println("Mismatch found for collection filter: " + key);
                            return false;
                        }
                    } else {
                        // User filter is a collection; item filter is a single value
                        if (!((Collection<?>) userFilterValue).contains(itemFilterValue)) {
                            System.out.println("User selected 'I don't mind' for filter: " + key + ". Any value is acceptable.");
                            continue; // Accept any value for 'I don't mind' logic
                        }
                    }
                } else {
                    // Special case for handling "I don't mind" options
                    if (userFilterValue instanceof List) {
                        List<?> userOptions = (List<?>) userFilterValue;
                        if (!userOptions.contains(itemFilterValue)) {
                            System.out.println("Mismatch found for flexible filter: " + key);
                            return false;
                        }
                    } else if (!itemFilterValue.equals(userFilterValue)) {
                        System.out.println("Mismatch found for non-collection filter: " + key);
                        return false;
                    }
                }
            } else {
                System.out.println("Filter key not found in item: " + key);
            }
        }

        System.out.println("Item matched all filters.");
        return true;
    }

}
