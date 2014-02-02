def ENCHANTMENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Enchantment);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def AN_ENCHANTMENT_CARD_FROM_LIBRARY = new MagicTargetChoice(
    ENCHANTMENT_CARD_FROM_LIBRARY,
    "an enchantment card"
);

def act = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo() == false) {
        event.processTargetCard(game, {
            final MagicCard card ->
            game.logAppendMessage(event.getPlayer(), "Found " + card + ".");
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
            game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
        });
    }
}

def evt = {
    final MagicEvent event, final MagicChoice choice ->
    return new MagicEvent(
        event.getSource(),
        event.getPlayer(), 
        choice,
        act,
        "PN may search his or her library for an enchantment card, reveals it, shuffle his or her library, and put that card on top of it."
    );
}

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_ENCHANTMENT_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Shroud, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
           return new MagicEvent(
                source,
                this,
                "PN may search his or her library for an enchantment card, reveals it, shuffle his or her library, and put that card on top of it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(evt(
                event,
                AN_ENCHANTMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
