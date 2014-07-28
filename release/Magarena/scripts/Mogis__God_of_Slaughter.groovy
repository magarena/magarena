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
            return (permanent.isOpponent(upkeepPlayer)) ? new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(MagicTargetChoice.SACRIFICE_CREATURE),
                MagicSacrificeTargetPicker.create(),
                this,
                "SN deals 2 damage to PN unless he or she sacrifices a creature.\$"
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicSacrificeAction(it));
                    
                });
            } else {
                final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),2);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
