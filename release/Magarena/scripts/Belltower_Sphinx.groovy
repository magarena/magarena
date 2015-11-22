[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return damage.getTarget() == permanent ?
                new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    damage.getDealtAmount(),
                    this,
                    "PN puts RN cards from the top of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(), event.getRefInt()));
        }
    }
]
