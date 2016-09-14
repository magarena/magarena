[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "+Life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{G}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN gains life equal to RN's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getToughness();
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player,amount));
        }
    }
]
