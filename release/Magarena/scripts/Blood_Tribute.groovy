[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses half his or her life, rounded up. "+
                "If SN was kicked, PN gains life equal to the life lost this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final ChangeLifeAction act = new ChangeLifeAction(it,-(it.getLife()+1)/2);
                game.doAction(act);
                if (event.isKicked()) {
                    final int amount = -act.getLifeChange();
                    final MagicPlayer player = event.getPlayer();
                    game.logAppendValue(player,amount);
                    game.doAction(new ChangeLifeAction(player,amount));
                }
            });
        }
    }
]
