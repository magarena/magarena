[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. " + 
                "Its controller may search his or her library for a basic land card, put that card onto the battlefield tapped, then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        creature.getController(),
                        new MagicMayChoice(
                            "Search for a basic land card?",
                            MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                        ),
                        {
                            final MagicGame G, final MagicEvent E ->
                            if (E.isYes()) {
                                E.processTargetCard(G, {
                                    final MagicCard card ->
                                    G.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                                    G.doAction(new MagicPlayCardAction(card,E.getPlayer(),MagicPlayMod.TAPPED));
                                    G.doAction(new MagicShuffleLibraryAction(E.getPlayer()));
                                } as MagicCardAction);
                            }
                        } as MagicEventAction,
                        "Selected card\$."
                    ));
                }
            });
        }
    }
]
