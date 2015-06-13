def DrawTwo = {
    final MagicSource staleSource, final MagicPlayer stalePlayer ->
    return new MagicAtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPlayer spellcaster = staleSource.getController()
            return spellcaster.getId() == upkeepPlayer.getId();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            game.addDelayedAction(new RemoveTriggerAction(this));
            return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        2,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may\$ draw two cards."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer(), 2));
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
                    DrawTwo(event.getSource(), spell.getController())
                ));
                game.doAction(new AddTriggerAction(
                    MagicAtUpkeepTrigger.YouDraw(event.getSource(), event.getPlayer())
                ));
            });
        }
    }
]
