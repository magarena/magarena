[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses X life and you gain X life, where " +
                "X is the greatest power among creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    int power = 0;
                    for (final MagicPermanent creature : targets) {
                        power = Math.max(power,creature.getPower());
                    }
                    game.doAction(new MagicChangeLifeAction(player,-power));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),power));
                }
            });
        }
    }
]
