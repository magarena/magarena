[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Put target creature\$ card from a graveyard onto the battlefield under your control. " +
                "That creature is a black Zombie in addition to its other colors and types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard card ->
                game.doAction(new MagicReanimateAction(
                    card,
                    event.getPlayer(),
                    [MagicPlayMod.BLACK, MagicPlayMod.ZOMBIE]
                ));
            } as MagicCardAction);
        }
    }
]
