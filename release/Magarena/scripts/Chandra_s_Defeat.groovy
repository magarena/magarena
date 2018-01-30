def NEG_TARGET_RED_CREATURE_OR_RED_PLANESWALKER = new MagicTargetChoice(
    new MagicPermanentFilterImpl() {
        @Override
        boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasColor(MagicColor.Red) && (target.hasType(MagicType.Creature) || target.hasType(MagicType.Planeswalker));
        }
    },
    MagicTargetHint.Negative,
    "target red creature or red planeswalker"
);

def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.addEvent(new MagicDiscardChosenEvent(event.getSource(), A_CARD_FROM_HAND));
        game.doAction(new DrawAction(event.getPlayer()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_RED_CREATURE_OR_RED_PLANESWALKER,
                this,
                "SN deals 5 damage to target red creature or red planeswalker\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 5));
                if (it.hasType(MagicType.Planeswalker) && it.hasSubType(MagicSubType.Chandra)) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicMayChoice(),
                        action,
                        "PN may\$ discard a card. If PN does, draw a card."
                    ));
                }
            });
        }
    }
]
