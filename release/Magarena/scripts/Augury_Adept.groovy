[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                this,
                "Reveal the top card of PN's library and put that card into PN's hand. PN gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            player.getLibrary().getCardsFromTop(1) each {
                game.doAction(new RevealAction(it));
                game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                game.doAction(new ChangeLifeAction(player, it.getConvertedCost()));
            }
        }
    }
]
