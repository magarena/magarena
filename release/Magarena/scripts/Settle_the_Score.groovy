def effect = MagicRuleEventAction.create("Put two loyalty counters on a planeswalker you control.")

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                 game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                 game.addEvent(effect.getEvent(event));
            });
        }
    }
]
