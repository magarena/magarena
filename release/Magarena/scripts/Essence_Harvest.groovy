[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses X life and you gain X life, where " +
                "X is the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                int power = 0;
                CREATURE_YOU_CONTROL.filter(event) each {
                    power = Math.max(power, it.getPower());
                }
                game.doAction(new ChangeLifeAction(it,-power));
                game.doAction(new ChangeLifeAction(event.getPlayer(),power));
            });
        }
    }
]
