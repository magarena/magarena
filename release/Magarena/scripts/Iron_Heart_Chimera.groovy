def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Vigilance, flags);
    }
};

def choice = MagicTargetChoice.Positive("target Chimera creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Sacrifice"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                this,
                "Put a +2/+2 counter on target Chimera creature\$. It gains vigilance."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.PlusTwo,1));
                game.doAction(new MagicAddStaticAction(it, AB));      
            });
        }
    }
]
