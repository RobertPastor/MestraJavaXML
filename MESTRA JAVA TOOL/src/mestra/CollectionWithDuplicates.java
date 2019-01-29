package mestra;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * This class manages a collection of MestraIdentifier and 
 * is responsible for detecting duplicated identifiers.
 * @author PASTOR Robert
 * @since October 2007
 * 
 */
public class CollectionWithDuplicates {

    ArrayList<MestraIdentifier> collectionWithDuplicates = null;
    
    public CollectionWithDuplicates() {
        collectionWithDuplicates = new ArrayList<MestraIdentifier>();
    }
    
    private void checkDuplicates(MestraIdentifier mestraIdentifier) {
        Iterator<MestraIdentifier> Iter = collectionWithDuplicates.iterator();
        while (Iter.hasNext()) {
            MestraIdentifier aMestraIdentifier = Iter.next();
            if (aMestraIdentifier.getIdentifier().equalsIgnoreCase(mestraIdentifier.getIdentifier())) {
                aMestraIdentifier.setDuplicated(true);
                mestraIdentifier.setDuplicated(true);
            }
        }
    }
    
    public boolean add(MestraBase mestraBase) {
        checkDuplicates(mestraBase);
        return collectionWithDuplicates.add(mestraBase);
    }
}
