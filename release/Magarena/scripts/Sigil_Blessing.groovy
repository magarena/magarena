[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                this,
                "Until end of turn, target creature\$ PN controls gets +3/+3 and other creatures PN controls get +1/+1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    CREATURE_YOU_CONTROL.except(it)
                );
                game.doAction(new ChangeTurnPTAction(it,3,3));
                for (final MagicPermanent permanent : targets) {
                    game.doAction(new ChangeTurnPTAction(permanent,1,1));
                }
            });
        }
    }
]
