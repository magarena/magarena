[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return the top creature card\$ of PN's graveyard to the battlefield. " +
                "That creature gains haste until end of turn. " +
                "Exile it at the beginning of the next end step. "
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCard card = player.getGraveyard().getCardAtTop();
            game.doAction(new ReanimateAction(
                card,
                player,
                [MagicPlayMod.HASTE_UEOT, MagicPlayMod.EXILE_AT_END_OF_TURN]
            ));
        }
    }
]
