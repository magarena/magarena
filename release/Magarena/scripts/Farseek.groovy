[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a Plains, Island, Swamp, or Mountain card and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                MagicTargetChoice.PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY,
                {
                    final MagicGame G, final MagicEvent E ->
                    E.processTargetCard(G, {
                        final MagicCard card ->
                        G.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        G.doAction(new MagicPlayCardAction(card,E.getPlayer(),MagicPlayMod.TAPPED));
                        G.doAction(new MagicShuffleLibraryAction(E.getPlayer()));
                    } as MagicCardAction);
                } as MagicEventAction,
                "Selected card\$."
            ));
        }
    }
]
