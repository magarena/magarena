[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getKicker(),
                this,
                "PN gains 3 life plus 3 life for each additional {1}{G} spent to cast SN. (RN)"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),3 + 3 * event.getRefInt()));
        }
    }
]
