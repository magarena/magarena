package magic.data;

import java.util.Set;

public enum MagicFormat {
    Freeform,
    Modern,
    Standard,
    Vintage,
    Legacy
    ;
    
    private static Set<MagicFormat> allFormats;
   // private final List<MagicCardDefinition> bannedList; //to be loaded from relevant file in cards\formats\
    
    public static final Set<MagicFormat> ALL_FORMATS() {
        for (MagicFormat format:MagicFormat.values()) {
            allFormats.add(format);
        }
        return allFormats;
    }
    
    /*public final List<MagicCardDefinition> BannedList() { //to all getting of bannedLists
        return bannedList;
    }*/
    
}