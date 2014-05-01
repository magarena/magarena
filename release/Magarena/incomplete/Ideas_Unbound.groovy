def DISCARD_THREE_CARDS = new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN discards three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),3));
        }
    };

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws three cards. PN discards three cards at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicDrawAction(event.getPlayer(),3));
                game.doAction(new MagicAddTriggerAction(event.getPlayer(),DISCARD_THREE_CARDS));
        }
    }
]
