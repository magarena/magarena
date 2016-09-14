[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificePermanentEvent(source, new MagicTargetChoice("a red or white creature to sacrifice"))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                MagicDefaultTargetPicker.create(),
                payedCost.getTarget(),
                this,
                "SN deals damage to target creature or player\$ equal to half RN's power, rounded down."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                def amount = event.getRefPermanent().getPower() intdiv 2;
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getPermanent(),it,amount));
            });
        }
    }
]
