[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicBounceTargetPicker.create(),
                this,
                "Exile target creature\$ you control, then return " +
                "that card to the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new MagicRemoveCardAction(it.getCard(),MagicLocationType.Exile));
                game.doAction(new MagicPlayCardAction(it.getCard(),event.getPlayer()));
            });
        }
    }
]
