[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN fights target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent=event.getPermanent();
                final MagicDamage damage1 = new MagicDamage(permanent,it,permanent.getPower());
                game.doAction(new MagicDealDamageAction(damage1));
                final MagicDamage damage2 = new MagicDamage(it,permanent,it.getPower());
                game.doAction(new MagicDealDamageAction(damage2));
            });
        }
    }
]
