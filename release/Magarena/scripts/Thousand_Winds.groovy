[
    new MagicWhenSelfTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                this,
                "Return all other tapped creatures to their owners' hands."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> tapped=
                game.filterPermanents(
                    event.getPlayer(),
                    new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.TAPPED_CREATURE,event.getPermanent())
                );
            for (final MagicPermanent creature : tapped) {
                game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
            }
        }
    }
]
