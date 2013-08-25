[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a basic land card and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY,
                {
                    final MagicGame G, final MagicEvent E ->
                    E.processTargetCard(G, {
                        final MagicCard card ->
                        G.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        G.doAction(new MagicPlayCardAction(card,event.getPlayer(),MagicPlayMod.TAPPED));
                        G.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                    } as MagicCardAction);
                } as MagicEventAction,
                "Selected card\$."
            ));
        }
    }
]
