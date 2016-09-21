package magic.model.mstatic;

import java.util.EnumSet;
import java.util.Set;

public enum MagicLayer {
    Card,        //0.  properties from the card, not formally defined in rules
    Copy,        //1.  copy
    Control,     //2.  control changing
    Text,        //3.  text changing
    CDASubtype,  //4a. CDA subtype
    Type,        //4b. type-changing (include sub and super types)
    CDAColor,    //5a. CDA color
    Color,       //5b. color changing
    Ability,     //6.  ability adding/removing
    AbilityCond, //6b. ability adding/removing that depends on condition
    CDAPT,       //7a. CDA p/t
    SetPT,       //7b. set p/t to specific value
    ModPT,       //7c. modify p/t
    CountersPT,  //7d. p/t changes due to counters
    SwitchPT,    //7e. switch p/t
    Player,      //8.  affect player, e.g. you have hexproof
    Game,        //9.  affect game rules, e.g. modify maximum hand size, creature is Indestructible
    CostIncrease,
    CostReduction,
    ;

    public static final Set<MagicLayer> PERMANENT = EnumSet.range(Card, SwitchPT);
}
