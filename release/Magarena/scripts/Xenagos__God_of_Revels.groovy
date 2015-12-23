def choice = MagicTargetChoice.Positive("another target creature you control")

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
    new AtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getController() == attackingPlayer ? 
                new MagicEvent(
                    permanent,
                    choice,
                    MagicPumpTargetPicker.create(),
                    this,
                    "Another target creature\$ you control gains haste and gets +X/+X until end of turn, where X is that creature's power."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it, MagicAbility.Haste));
                game.doAction(new ChangeTurnPTAction(it, it.getPower(), it.getPower()));
            });
        }
    }
]
