def CARD_NAMED_LILIANA_DEATH_WIELDER_FROM_YOUR_LIBRARY_OR_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getName().equals("Liliana, Death Wielder");
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library || targetType == MagicTargetType.Graveyard;
        }
    }

def searchAction = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicSearchToLocationEvent(
            event,
            new MagicFromCardFilterChoice(
                CARD_NAMED_LILIANA_DEATH_WIELDER_FROM_YOUR_LIBRARY_OR_GRAVEYARD,
                1,
                false,
                "a card named Liliana, Death Wielder from your library or graveyard"
            ),
            MagicLocationType.OwnersHand
        ));
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a -1/-1 counter on each creature PN doesn't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers().minus(event.getPlayer())) {
            for (final MagicPermanent permanent : player.getPermanents()) {
                if (permanent.hasType(MagicType.Creature)) {
                    game.doAction(new ChangeCountersAction(event.getPlayer(), permanent, MagicCounterType.MinusOne, 1));
                }
            }}

            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice("Search your library?"),
                searchAction,
                "PN may\$ search PN's library and/or graveyard for a card named Liliana, Death Wielder, reveal it, and put it into PN's hand. " +
                "If PN search PN's library this way, shuffle it."
            ));
        }
    }
]

