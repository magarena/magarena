final MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        pt.add(1,0);
    }
}

final MagicStatic Lifelink = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set flags) {
        flags.add(MagicAbility.Lifelink);
    }
}

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Until PN's next turn, creatures PN controls get +1/+0 and gain lifelink."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicPlayer player = outerEvent.getPlayer();
            CREATURE_YOU_CONTROL.filter(player) each {
                outerGame.addDelayedAction(new AddStaticAction(it,PT));
                outerGame.addDelayedAction(new AddStaticAction(it,Lifelink));

            // remove the statics during player's next upkeep
                AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                        if (upkeepPlayer.getId() == outerEvent.getPlayer().getId()) {
                            game.addDelayedAction(new RemoveStaticAction(permanent, PT));
                            game.addDelayedAction(new RemoveStaticAction(permanent, Lifelink));
                            game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                        }
                        return MagicEvent.NONE;
                    }
                }
                outerGame.doAction(new AddTriggerAction(it, cleanup));
            };
        }
    }
]

