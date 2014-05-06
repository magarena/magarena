def EventAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent search = new MagicSearchOntoBattlefieldEvent(
        event,
        new MagicMayChoice(
            "Search for an enchantment card?",
            MagicTargetChoice.ENCHANTMENT_CARD_COST_3_FROM_LIBRARY
        ),
        MagicPlayMod.UNTAPPED
    );
    game.addEvent(search);
};

def Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        EventAction,
        "PN may search his or her library for an enchantment card with converted mana cost 3 or less and put it onto the battlefield, then shuffle his or her library."
    );
};

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return Event(permanent);
        }
    }      
]
