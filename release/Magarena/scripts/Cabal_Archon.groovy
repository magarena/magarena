[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                MagicTargetFilter.Factory.tribal(MagicSubType.Cleric, MagicTargetFilter.Control.You),
                false,
                MagicTargetHint.None,
                "a Cleric to sacrifice"
            );
            return [
                new MagicPayManaCostEvent(source,"{B}"),
                new MagicSacrificePermanentEvent(source,targetChoice)
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
