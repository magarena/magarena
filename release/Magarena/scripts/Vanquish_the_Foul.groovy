[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_POWER_4_OR_MORE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature with power 4 or greater\$. Scry 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.addEvent(new MagicScryEvent(event.getSource(),event.getPlayer()));
            });
        }
    }
]
