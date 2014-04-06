[
    new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Creature.getMask();
        }
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.remove(MagicSubType.God);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().getDevotion(MagicColor.Green, MagicColor.Red) < 7;
        }
    },
    new MagicAtBeginOfCombatTrigger() {
                
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getController() == attackingPlayer ? new MagicEvent(
                permanent,
                new MagicTargetChoice(new MagicOtherPermanentTargetFilter(MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL, permanent),"another target creature you control"),
                MagicPumpTargetPicker.create(),
                this,
                "Another target creature\$ you control gains haste and gets +X/+X until end of turn, where X is that creature's power."
            ):MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent perm ->
                game.doAction(new MagicAddStaticAction(perm, new MagicStatic(MagicLayer.ModPT,MagicStatic.UntilEOT) {
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.add(permanent.getPower(),permanent.getPower());
                    }
                }));
            });
         
        }
    }
]
