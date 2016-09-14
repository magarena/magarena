[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Sacrifice"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicSacrificePermanentEvent(source,SACRIFICE_CREATURE)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals 2 damage to target creature or player if RN was red. Draw a card if RN was black."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent sacrificed=event.getRefPermanent();
                if (sacrificed.hasColor(MagicColor.Red)) {
                    game.doAction(new DealDamageAction(event.getSource(),it,2));
                }
                if (sacrificed.hasColor(MagicColor.Black)) {
                    game.doAction(new DrawAction(event.getPlayer(),1));
                }
            });
        }
    }
]
