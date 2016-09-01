package ki;

import java.util.ArrayList;

/**
 * Created by Tim Bolz on 10.06.2016.
 */
public enum Property {

    PEACEFUL,AGGRESSIVE,ECONOMIC,GREEDY,DIPLOMATIC,STRATEGIC,DISTRUSTFUL;

	/**
     * gibt die Liste der noch hinfufügbaren eigenschaften zurück
     * @param list die Liste mit egenschaften, die überprüft werden soll
     * @return die liste mit möglichen  weiteren Eigenschften
     */
    public static ArrayList<Property> getMissingProperties(ArrayList<Property> list){
        ArrayList<Property> missing = new ArrayList<>();
        for(Property p:Property.values()){
            if(!list.contains(p)){
                if(!((p.equals(AGGRESSIVE) && list.contains(PEACEFUL))|| (p.equals(PEACEFUL) && list.contains(AGGRESSIVE)) ||
                        (p.equals(DIPLOMATIC) && list.contains(DISTRUSTFUL)) || (p.equals(DISTRUSTFUL) && list.contains(DIPLOMATIC)))) {
                    missing.add(p);
                }
            }
        }
        /*if(!missing.isEmpty()){
            for (Property p : missing) {
                if ((p.equals(AGGRESSIVE) && list.contains(PEACEFUL))|| (p.equals(PEACEFUL) && list.contains(AGGRESSIVE)) ||
                        (p.equals(DIPLOMATIC) && list.contains(DISTRUSTFUL)) || (p.equals(DISTRUSTFUL) && list.contains(DIPLOMATIC))) {
                    missing.remove(p);
                }
            }
        }*/
        return missing;

    }
}
