[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack,
                this,
                "Counter RN if a card with the same name is in a graveyard or a nontoken permanent with the same name is on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardOnStack spell = event.getRefCardOnStack();
            final String name = spell.getCard().getName();
            final int graveyard = cardName(name)
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size()
            final int battlefield = nonTokenPermanentName(name, Control.Any).filter(event).size();
            final int amount = graveyard + battlefield;
            if (amount > 0) {
                game.logAppendMessage(event.getPlayer(), "("+name+") is countered.")
                game.doAction(new CounterItemOnStackAction(spell));
            }
        }
    }
]
