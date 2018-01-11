[
// +1: Untap target land you control. Until your next turn, it becomes a 5/5 Elemental creature with haste. It's still a land.;\
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicEvent(
                source,
                A_LAND_YOU_CONTROL,
                this,
                "Untap target land PN control.\$ Until PN's next turn, it becomes a 5/5 Elemental creature with haste. It's still a land."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {

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

            event.processTargetPermanent(game, {
                game.doAction(new BecomesCreatureAction(
                    it,
                    PT, ST, haste
                ));

                // remove the statics during player's next upkeep
                AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                        if (upkeepPlayer.getId() == event.getPlayer().getId()) {
                            game.addDelayedAction(new RemoveStaticAction(permanent, PT));
                            game.addDelayedAction(new RemoveStaticAction(permanent, ST));
                            game.addDelayedAction(new RemoveStaticAction(permanent, haste));
                        }
                        return MagicEvent.NONE;
                    }
                }
                game.doAction(new AddTriggerAction(it, cleanup));
            });
        }
    }
]

