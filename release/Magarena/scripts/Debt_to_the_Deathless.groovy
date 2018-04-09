[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                cardOnStack.getOpponent(),
                payedCost.getX(),
                this,
                "PN loses twice X life. (X="+payedCost.getX()+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final ChangeLifeAction act = new ChangeLifeAction(event.getPlayer(), -event.getRefInt() * 2);
                game.doAction(act);
                final int amount = -act.getLifeChange();
                final MagicPlayer you = event.cardOnStack.getController();
                game.logAppendMessage(you, "${you.getName()} gains (${amount}) life.");
                game.doAction(new ChangeLifeAction(you,amount));
        }
    }
]
