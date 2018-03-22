def abilityList = MagicAbility.getAbilityList("SN can't attack or block, its activated abilities can't be activated");

def condition = {
    final MagicSource source ->
    return ((MagicPermanent)source).getCounters(MagicCounterType.Brick) >= 3;
}

[
    new MagicPermanentActivation(
        [condition],
        new MagicActivationHints(MagicTiming.Tapping),
        "can't attack or block"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "Until PN's next turn, target creature\$ can't attack or block and its activated abilites can't be activated."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerEvent.processTargetPermanent(outerGame, {
                outerGame.doAction(new GainAbilityAction(it, abilityList, MagicStatic.Forever));

                AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                        if (upkeepPlayer.getId() == outerEvent.getPlayer().getId()) {
                            game.addDelayedAction(new LoseAbilityAction(permanent, abilityList, MagicStatic.Forever));
                            game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                        }
                        return MagicEvent.NONE;
                    }
                }
                outerGame.doAction(new AddTriggerAction(it, cleanup));
            });
        }
    }
]

