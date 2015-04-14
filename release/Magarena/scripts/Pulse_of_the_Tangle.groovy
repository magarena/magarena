[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 3/3 green Beast creature token onto the battlefield. " +
                "Then if your opponent controls more creatures than you, return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new PlayTokenAction(player,TokenCardDefinitions.get("3/3 green Beast creature token")));
            final boolean more = player.getOpponent().getNrOfPermanents(MagicType.Creature) >
                                 player.getNrOfPermanents(MagicType.Creature);
            if (more) {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            }
        }
    }
]
