[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Zombies PN controls get +2/+1 until end of turn. " +
                "Other creatures PN controls get +1/+1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.getPlayer().getPermanents().findAll({ it.hasType(MagicType.Creature) }).each({
                if (it.hasSubType(MagicSubType.Zombie)) {
                    game.doAction(new ChangeTurnPTAction(it, 2, 1));
                }
                else {
                    game.doAction(new ChangeTurnPTAction(it, 1, 1));
                }
            });
        }
    }
]

