[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ discards cards equal to the number of Swamps PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Swamp);
            event.processTargetPlayer(game, {
                game.addEvent(new MagicDiscardEvent(event.getSource(),it,amount));
            });
        }
    }
]
