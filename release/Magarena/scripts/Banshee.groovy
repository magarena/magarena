[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{X}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount.intdiv(2)),
                amount,
                this,
                "SN deals half X damage, rounded down, to target creature or player,\$ and half X damage, rounded up, to PN. (X=${amount})"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent permanent = event.getPermanent();
                final int amount = event.getRefInt();
                final int targetAmount = (int)Math.floor(amount/2);
                final int playerAmount = (int)Math.ceil(amount/2);
                game.doAction(new DealDamageAction(permanent, it, targetAmount));
                game.doAction(new DealDamageAction(permanent, event.getPlayer(), playerAmount));
            });
        }
    }
]
