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
            return source.getController().getDevotion(MagicColor.Black, MagicColor.Red) < 7;
        }
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ? 
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice("Sacrifice a creature?"),
                    this,
                    "PN may\$ sacrifice a creature. If PN doesn't, SN deals 2 damage to him or her."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                SACRIFICE_CREATURE
            );
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new DealDamageAction(
                    event.getSource(),
                    event.getPlayer(),
                    2
                ));
            }
        }
    }
]
