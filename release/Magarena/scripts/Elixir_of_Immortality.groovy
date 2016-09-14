[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
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
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new ChangeLifeAction(player, 5));
            game.doAction(new RemoveFromPlayAction(permanent, MagicLocationType.TopOfOwnersLibrary));
            game.doAction(new ShuffleLibraryAction(permanent.getOwner()));

            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            }
            game.doAction(new ShuffleLibraryAction(player));
        }
    }
]
