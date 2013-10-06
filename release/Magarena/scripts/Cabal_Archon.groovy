[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{B}"),
                new MagicSacrificePermanentEvent(
                    source,
                    new MagicTargetChoice("a Cleric to sacrifice")
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses 2 life and PN gains 2 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player, -2));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(), 2));
                }
            });
        }
    }
]
