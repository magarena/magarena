def PayOrLose = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            if (upkeepPlayer == stalePlayer) {
                game.addDelayedAction(new RemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    new MagicMayChoice("Pay {2}{G}{G}?"),
                    this,
                    "PN pays {2}{G}{G}. If PN doesn't he or she loses the game."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicPayManaCostEvent(event.getSource(),"{2}{G}{G}");
            if (event.isYes() && cost.isSatisfied()) {
                game.addEvent(cost);
            } else {
                game.doAction(new LoseGameAction(event.getPlayer()));
            }
            
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a green creature card, reveals it, and puts it into his or her hand. "+
                "At the beginning of PN's next upkeep, he or she pays {2}{G}{G}. "+
                "If PN doesn't, he or she loses the game."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardFilterChoice(
                    MagicTargetFilterFactory.GREEN_CREATURE_CARD_FROM_LIBRARY,
                    1,
                    true,
                    "to put into your hand"
                ),
                MagicLocationType.OwnersHand
            ));
            game.doAction(new AddTriggerAction(
                PayOrLose(event.getSource(), event.getPlayer())
            ));
        }
    }
]
