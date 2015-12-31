def effect = MagicRuleEventAction.create("return a creature card from your graveyard to your hand.")

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ sacrifices a creature, discards three cards, then loses 5 life." +
                "PN returns a creature card from his or her graveyard to his or her hand, draws three cards, then gain 5 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), it, SACRIFICE_CREATURE));
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, 3));
                game.doAction(new ChangeLifeAction(it,-5));

                game.addEvent(effect.getEvent(event));
                game.doAction(new DrawAction(event.getPlayer(), 3));
                game.doAction(new ChangeLifeAction(event.getPlayer(), 5));
            });
        }
    }
]
