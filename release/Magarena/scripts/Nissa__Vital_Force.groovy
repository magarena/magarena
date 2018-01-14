final MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        pt.set(5, 5);
    }
}

final MagicStatic ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Elemental);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
}

final MagicStatic haste = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Haste);
    }
}

final MagicTargetChoice TARGET_LAND_YOU_CONTROL = new MagicTargetChoice("target land you control")

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_LAND_YOU_CONTROL,
                this,
                "Untap target land PN controls.\$ Until PN's next turn, it becomes a 5/5 Elemental creature with haste. It's still a land."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerEvent.processTargetPermanent(outerGame, {
                outerGame.doAction(new UntapAction(it));
                outerGame.doAction(new BecomesCreatureAction(
                    it,
                    PT, ST, haste
                ));

                // remove the statics during player's next upkeep
                AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                        if (upkeepPlayer.getId() == outerEvent.getPlayer().getId()) {
                            game.addDelayedAction(new RemoveStaticAction(permanent, PT));
                            game.addDelayedAction(new RemoveStaticAction(permanent, ST));
                            game.addDelayedAction(new RemoveStaticAction(permanent, haste));
                        }
                        return MagicEvent.NONE;
                    }
                }
                outerGame.doAction(new AddTriggerAction(it, cleanup));
            });
        }
    }
]

