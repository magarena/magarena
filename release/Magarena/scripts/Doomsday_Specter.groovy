def Discard = {
    final MagicGame game, final MagicEvent event ->
    if (event.getCardChoice().size() > 0) {
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),event.getCardChoice().get(0)));
    }
};


[
new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    (MagicPlayer) damage.getTarget(),
                    this,
                    "PN looks at RN's hand and chooses a card from it. RN discards that card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(event.getRefPlayer().getHand(),1),
                event.getRefPlayer(),
                Discard,
                ""
            ));
        }
    }
]
