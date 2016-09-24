[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                this,
                "PN exiles target creature card from his or her graveyard.\$ PN puts a "+
                "black Zombie creature token onto the battlefield. Its power is equal to "+
                "that card's power and its toughness is equal to that card's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard target ->
                game.doAction(new ShiftCardAction(target, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("black Zombie creature token"),
                    MagicPlayMod.PT(target.getPower(), target.getToughness())
                ));
            });
        }
    }
]
