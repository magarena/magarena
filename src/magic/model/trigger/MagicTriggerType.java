package magic.model.trigger;

public enum MagicTriggerType {
    AtUpkeep,               // player
    AtDraw,                 // player
    AtEndOfTurn,            // player
    AtEndOfCombat,          // player
    WhenDamageIsDealt,      // damage
    WhenOtherSpellIsCast,   // card on stack
    WhenSpellIsCast,        // card on stack
    WhenComesIntoPlay,      // controller
    WhenLeavesPlay,         // MagicRemoveFromPlayAction
    WhenBecomesTapped,      // permanent
    WhenDrawn,              // card
    WhenOtherDrawn,         // card
    WhenLifeIsGained,       // player, life gained
    WhenLifeIsLost,         // player, life lost
    WhenOtherComesIntoPlay, // permanent
    WouldBeMoved,           // MagicMoveCardAction
    WhenPutIntoGraveyard,       // graveyard trigger data
    WhenOtherPutIntoGraveyard,  // graveyard trigger data
    WhenOtherDies,          // permanent
    WhenAttacks,            // permanent
    WhenBlocks,             // permanent
    WhenBecomesBlocked,     // permanent
    WhenAttacksUnblocked,   // permanent
    WhenTargeted,           // permanent
    WhenLoseControl,        // permanent
    IfDamageWouldBeDealt,   // item on stack
    IfPlayerWouldLose,      // player[]
    IfLifeWouldChange,      // MagicChangeLifeAction
    ;

    public boolean usesStack() {
        return this != IfDamageWouldBeDealt;
    }
}
