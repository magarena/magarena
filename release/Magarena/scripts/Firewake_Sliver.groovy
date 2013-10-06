
def FirewakeSacrifice = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Pump"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{1}"),
            new MagicSacrificeEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = new MagicTargetChoice(
            new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_SLIVER, 
                source
            ),
            MagicTargetHint.Positive,
            "target Sliver creature"
        );
        return new MagicEvent(
            source,
            targetChoice,
            MagicPumpTargetPicker.create(),
            this,
            "Target Sliver creature\$ gets +2/+2 until end of turn."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicChangeTurnPTAction(creature,2,2));
            }
        });
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {    
            permanent.addAbility(FirewakeSacrifice);
        }
    }
]
