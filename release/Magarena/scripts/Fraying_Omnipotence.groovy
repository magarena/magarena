def choice = new MagicTargetChoice("a creature to sacrifice");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player loses half their life, then discards half the cards in their hand, then sacrifices half the creatures they control. Round up each time."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int life = (int)((player.getLife()+1) / 2);
                final int discard = (int)((player.getHandSize()+1) / 2);
                final int sacrifice = (int)((player.getNrOfPermanents(MagicType.Creature)+1) / 2);

                game.doAction(new ChangeLifeAction(player, -life));
                game.addEvent(new MagicDiscardEvent(event.getSource(), player, discard));
                sacrifice.times {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        choice
                    ));
                }
            }
        }
    }
]
