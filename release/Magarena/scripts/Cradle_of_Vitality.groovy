[
    new LifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            return permanent.isController(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}{W}")),
                        POS_TARGET_CREATURE
                    ),
                    MagicPumpTargetPicker.create(),
                    lifeChange.amount,
                    this,
                    "PN may\$ pay {1}{W}\$. If PN does, he or she puts a +1/+1 counter " +
                    "on target creature\$ for each 1 life he or she gained."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,event.getRefInt()));
                });
            }
        }
    }
]
