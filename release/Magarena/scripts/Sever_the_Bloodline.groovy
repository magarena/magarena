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
                new MagicNameTargetFilter(
                    CREATURE,
                    it.getName()
                ).filter(event) each {
                    game.addEvent(new MagicExileEvent(it));
                }
            });
        }
    }
]
