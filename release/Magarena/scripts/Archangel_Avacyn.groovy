def effect = MagicRuleEventAction.create("Transform SN.")

def DelayedTrigger = new AtUpkeepTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        game.addDelayedAction(new RemoveTriggerAction(permanent, this));
        return effect.getEvent(permanent);
    }
};

[
    new OtherDiesTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
            return died.hasSubType(MagicSubType.Angel) == false &&
                   died.isCreature() &&
                   died.isFriend(permanent);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Transform SN at the beginning of the next upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(
                event.getPermanent(),
                DelayedTrigger
            ));
        }
    }
]
