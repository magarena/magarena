[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Land),
        "Search"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayLifeEvent(source,1),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a Swamp or Mountain card and put it onto the battlefield. Then shuffle PN's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                MagicTargetChoice.SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY,
                {
                    final MagicGame G, final MagicEvent E ->
                    E.processTargetCard(G, {
                        final MagicCard card ->
                        G.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        G.doAction(new MagicPlayCardAction(card,E.getPlayer()));
                        G.doAction(new MagicShuffleLibraryAction(E.getPlayer()));
                    } as MagicCardAction);
                } as MagicEventAction,
                "Selected card\$."
            ));
        }
    }
]
