[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{2}{B}{R}")],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent=source;
            return [
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{2}{B}{R}")),
                new MagicSacrificeEvent(permanent)
            ];
        }
        
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target player\$."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicPermanent source=event.getPermanent();
                    final int amount=source.getPower();
                    if (amount > 0) {
                        final MagicDamage damage=new MagicDamage(source,target,amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    }
]
