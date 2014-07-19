def EventAction = {
    final MagicGame game, final MagicEvent event ->
    final List<MagicCard> choiceList = event.getPlayer().filterCards(MagicTargetFilterFactory.LAND_CARD_FROM_LIBRARY);
    game.addEvent(new MagicSearchOntoBattlefieldEvent(
        event,
        new MagicFromCardListChoice(choiceList, 2, true, "that are lands"),
        MagicPlayMod.TAPPED
    ));
};

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
