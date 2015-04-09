[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{1}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NegOther("target creature", source),
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to another target creature\$. " +
                "That creature becomes a Vampire in addition to its other types. " + 
                "Put a +1/+1 counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,1));
                game.doAction(new MagicAddStaticAction(it, MagicStatic.Vampire));
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1));
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{3}{B}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_VAMPIRE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target Vampire\$ for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddStaticAction(
                    event.getPermanent(), 
                    MagicStatic.ControlAsLongAsYouControlSource(event.getPlayer(),it)
                ));
            });
        }
    }
]
