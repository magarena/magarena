[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 5 life, then shuffle SN and his or her graveyard into their owner's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new ChangeLifeAction(player, 5));

            game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(), MagicLocationType.OwnersLibrary));
            
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.Graveyard
                ));
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersLibrary
                ));
            }
        }
    }
]
