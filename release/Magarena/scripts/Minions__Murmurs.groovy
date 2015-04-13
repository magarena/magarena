[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws X cards and loses X life, where X is the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicType.Creature);
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player,amount));
            game.doAction(new ChangeLifeAction(player,-amount));
            game.logAppendMessage(player," (X="+amount+")");
        }
    }
]
