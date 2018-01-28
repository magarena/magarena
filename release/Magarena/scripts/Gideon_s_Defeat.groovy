[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("target attacking or blocking creature"),
                this,
                "Exile target creature that's attacking or blocking\$. " +
                "If it was a Gideon planeswalker, you gain 5 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
                if (it.hasType(MagicType.Planeswalker) && it.hasSubType(MagicSubType.Gideon)) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), 5));
                }
            });
        }
    }
]

