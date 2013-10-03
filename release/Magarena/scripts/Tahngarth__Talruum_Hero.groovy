[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{1}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target creature. " +
                "That creature deals damage equal to its power to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent target) {
                    final MagicDamage damage = new MagicDamage(
                        event.getPermanent(),
                        target,
                        event.getPermanent().getPower()
                    );
                    game.doAction(new MagicDealDamageAction(damage));
					final MagicDamage damage2 = new MagicDamage(
                        target,
						event.getPermanent(),
                        target.getPower()
                    );
                    game.doAction(new MagicDealDamageAction(damage2));
                    
                }
            });
        }
    }
]
