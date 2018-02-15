[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Exile all attacking creatures target player\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                int amount = 0;
                player.getPermanents().each {
                    if (it.hasType(MagicType.Creature) && it.hasState(MagicPermanentState.Attacking)) {
                        game.addEvent(new MagicExileEvent(it));
                        amount++;
                    }
                }
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event.getSource(),
                    player,
                    new MagicFromCardFilterChoice(
                        BASIC_LAND_CARD_FROM_LIBRARY,
                        amount,
                        true,
                        "basic land cards from your library"
                    ),
                    MagicPlayMod.TAPPED
                ));
            });
        }
    }
]

