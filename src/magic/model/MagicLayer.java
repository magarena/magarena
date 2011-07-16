package magic.model; 

public enum MagicLayer {
    Layer1,  //copy 
    Layer2,  //control changing
    Layer3,  //text changing
    Layer4a, //CDA: subtype
    Layer4,  //type-changing (include sub and super types)
    Layer5a, //CDA: color
    Layer5,  //color changing
    Layer6,  //ability adding/removing
    Layer7a, //CDA: p/t
    Layer7b, //set p/t to specific value
    Layer7c, //modify p/t
    Layer7d, //p/t changes due to counters
    Layer7e, //switch p/t 
    Layer8,  //affect player, not objects
    Layer9,  //affect game rules, not objects
    ;
}
