def T = new MagicWhenLifeIsGainedTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
        return permanent.isController(lifeChange.player) ?
            new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_OPPONENT,
                lifeChange.amount,
                this,
                "Target opponent\$ loses RN life."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPlayer(game,new MagicPlayerAction() {
            public void doAction(final MagicPlayer player) {
                game.doAction(new MagicChangeLifeAction(
                    player,
                    -event.getRefInt()
                ));
            }
        });
    }
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{W}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Whenever PN gains life this turn, each opponent loses that much life."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicWhenLifeIsGainedTrigger trigger = T;
            outerGame.doAction(new MagicAddTriggerAction(outerEvent.getPermanent(), trigger));
            // remove the trigger at player's end of the turn
            MagicAtUpkeepTrigger cleanup = new MagicAtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    if (upkeepPlayer.getId() != outerEvent.getPlayer().getId()) {
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, trigger));
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, this));
                    }
                    return MagicEvent.NONE;
                }
            };
            outerGame.doAction(new MagicAddTriggerAction(outerEvent.getPermanent(), cleanup));

        }
    }
]
