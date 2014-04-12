[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Look"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN may look at target player's\$ hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                MagicPlayer player ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(player.getHand()),
                    player,
                    MagicEvent.NO_ACTION,
                    "RN revealed his or her hand."
                ));
            });
        }
    }
]
