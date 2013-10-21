def EventAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent search = new MagicSearchOntoBattlefieldEvent(
        event,
        new MagicMayChoice(
            "Search for a land card?",
            MagicTargetChoice.LAND_CARD_FROM_LIBRARY
        ),
        MagicPlayMod.TAPPED
    );
    game.addEvent(search);
    game.addEvent(search);
} as MagicEventAction;

def Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        EventAction,
        "PN may search his or her library for up to two land cards and put them onto the battlefield tapped, then shuffle his or her library."
    );
};

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
            return Event(permanent);
        }
    },
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent);
        }
    }      
]
