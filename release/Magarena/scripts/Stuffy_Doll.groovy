[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 1 damage to itself."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicDamage damage=new MagicDamage(permanent,permanent,1);
            game.doAction(new MagicDealDamageAction(damage));
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {  
            if (damage.getTarget() == permanent) {
                final int amount = damage.getDealtAmount();
                final MagicTarget target = permanent.getChosenTarget();
                return new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN deals Rn damage to " + target + "."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getPermanent(),
                event.getPermanent().getChosenTarget(),
                event.getRefInt()
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
