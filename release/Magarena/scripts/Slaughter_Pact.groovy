def PayOrLose = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            if (upkeepPlayer == stalePlayer) {
                game.addDelayedAction(new RemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    new MagicMayChoice("Pay {2}{B}?"),
                    this,
                    "PN pays {2}{B}. If PN doesn't he or she loses the game."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicPayManaCostEvent(event.getSource(),"{2}{B}");
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
                NEG_TARGET_NONBLACK_CREATURE,
                this,
                "Destroy target nonblack creature.\$. At the beginning of PN's next upkeep, he or she pays {2}{B}. "+
                "If PN doesn't, he or she loses the game."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new AddTriggerAction(
                    PayOrLose(event.getSource(), event.getPlayer())
                ));
            });
        }
    }
]
