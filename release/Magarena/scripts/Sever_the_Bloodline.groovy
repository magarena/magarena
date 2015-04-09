[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$ and all other creatures with the same " +
                "name as that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                    CREATURE,
                    it.getName()
                );
                final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),targetFilter);
                for (final MagicPermanent permanent : targets) {
                    game.addEvent(new MagicExileEvent(permanent));
                }
            });
        }
    }
]
