package magic.model.trigger;

public enum MagicTriggerType {
    AtUntap,                // player
    AtUpkeep,               // player
    AtDraw,                 // player
    AtEndOfTurn,            // player
    AtBeginOfCombat,        // player
    AtEndOfCombat,          // player
    WhenDamageIsDealt,      // damage
    WhenOtherSpellIsCast,   // card on stack
    WhenSpellIsCast,        // card on stack
    WhenComesIntoPlay,      // controller
    WhenLeavesPlay,         // RemoveFromPlayAction
    WhenBecomesTapped,      // permanent
    WhenBecomesUntapped,    // permanent
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
    WhenAttacksUnblocked,   // permanent
    WhenTargeted,           // permanent
    WhenLoseControl,        // permanent
    WhenBecomesState,       // ChangeStateAction
    WhenPlayerBecomesState, // ChangePlayerStateAction
    WhenTransforms,         // permanent
    WhenTurnedFaceUp,       // permanent
    WhenChampioned,         // MagicExiledUntilThisLeavesPlayAction
    WhenCycle,              // card
    WhenOtherCycle,         // card
    WhenScry,               // player
    WhenSacrifice,          // RemoveFromPlayAction
    IfDamageWouldBeDealt,   // item on stack
    IfPlayerWouldLose,      // player[]
    IfLifeWouldChange,      // MagicChangeLifeAction
    WhenClash,              // MagicPlayer
    Protection,             // MagicPermanent
    CannotBeBlocked,        // MagicPermanent
    CantBlock,              // MagicPermanent
    WhenOneOrMoreCountersArePlaced,    // object, counter placed
    WhenCounterIsRemoved,   // object, counter removed
    IfCounterWouldChange,   // MagicChangeCountersAction
    ;
}
