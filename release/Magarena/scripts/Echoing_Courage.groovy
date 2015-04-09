[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ and all other creatures with the same " +
                "name as that creature get +2/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter =
                    new MagicNameTargetFilter(it.getName());
                final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),targetFilter);
                for (final MagicPermanent permanent : targets) {
                    if (permanent.isCreature()) {
                        game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
                    }
                }
            });
        }
    }
]
