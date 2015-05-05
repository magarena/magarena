[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{B}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.Other("a Warrior creature to sacrifice",source))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "Target player\$ loses X life and PN gains X life, where X is RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount=event.getRefPermanent().getPower();
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
                game.doAction(new ChangeLifeAction(it,-amount));
                game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
