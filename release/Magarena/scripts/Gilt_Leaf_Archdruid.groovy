def UNTAPPED_DRUID_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Druid) &&
               target.isUntapped() &&
               target.isController(player);
    }
};

def AN_UNTAPPED_DRUID_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_DRUID_YOU_CONTROL,"an untapped Druid you control");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRepeatedPermanentsEvent(
                    source,
                    AN_UNTAPPED_DRUID_YOU_CONTROL,
                    7,
                    MagicChainEventFactory.Tap
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Gain control of all lands target player\$ controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                LAND_YOU_CONTROL.filter(it) each {
                    final MagicPermanent land ->
                    game.doAction(new GainControlAction(event.getPlayer(),land));
                }
            });
        }
    }
]
