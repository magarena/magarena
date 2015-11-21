def DrawOne = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DrawAction(event.getPlayer()))
    }
}

def DrawUpTo = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));
            return new MagicEvent(
                game.createDelayedSource(staleSource, stalePlayer),
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.DRAW_CARDS,
                    1,
                    MagicSimpleMayChoice.DEFAULT_NONE
                ),
                this,
                "PN may\$ draw up to two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer(), 1));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    new MagicMayChoice("Draw another card?"),
                    DrawOne,
                    ""
                ));
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
                NEG_TARGET_SPELL,
                this,
                "Counter target spell.\$ Its controller may draw up to two cards at the beginning of "+
                "the next turn's upkeep. PN draws a card at the beginning of the next turn's upkeep."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetItemOnStack(game, {
                final MagicCardOnStack spell ->
                game.doAction(new CounterItemOnStackAction(spell));
                game.doAction(new AddTriggerAction(
                    DrawUpTo(event.getSource(), spell.getController())
                ));
                game.doAction(new AddTriggerAction(
                    AtUpkeepTrigger.YouDraw(event.getSource(), event.getPlayer())
                ));
            });
        }
    }
]
