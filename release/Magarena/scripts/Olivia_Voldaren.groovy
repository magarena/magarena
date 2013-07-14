def control = {
    final MagicTargetFilter<MagicPermanent> filter, final int you ->
    return new MagicStatic(MagicLayer.Control,filter) {
        @Override
        public MagicPlayer getController(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return source.getController();
        }
        @Override
        public boolean condition(
                final MagicGame game,
                final MagicPermanent source,
                final MagicPermanent target) {
            if (you != source.getController().getIndex()) {
                //remove this static after the update
                game.addDelayedAction(new MagicRemoveStaticAction(source, this));
                return false;
            } else {
                return true;
            }
        }
    };
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_CREATURE,
                source
            );
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                targetFilter,
                true,
                MagicTargetHint.Negative,
                "another target creature"
            );
            return new MagicEvent(
                source,
                targetChoice,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to another target creature\$. " +
                "That creature becomes a Vampire in addition to its other types. " + 
                "Put a +1/+1 counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        creature,
                        1
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                    game.doAction(new MagicAddStaticAction(creature, MagicStatic.Vampire));
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}{B}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_VAMPIRE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target Vampire\$ for as long as you control SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent perm ->
                final MagicPermanent source = event.getPermanent();
                final MagicTargetFilter<MagicPermanent> filter = new MagicTargetFilter.MagicPermanentTargetFilter(perm);
                final int you = source.getController().getIndex();
                game.doAction(new MagicAddStaticAction(source, control(filter, you)));
            } as MagicPermanentAction);
        }
    }
]
