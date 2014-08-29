
def subTypes = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.removeAll();
        flags.add(MagicSubType.Dragon);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Dragon"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{W}{U}{B}{R}{G}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN becomes a Dragon, gets +5/+3, and gains flying and trample."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            game.doAction(new MagicAddStaticAction(creature,subTypes));
            game.doAction(new MagicChangeTurnPTAction(creature,5,3));
            game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Flying,MagicAbility.Trample));
        }
    }
]
