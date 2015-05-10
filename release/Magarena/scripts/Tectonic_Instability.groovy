[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.hasType(MagicType.Land) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Tap all lands RN's controller controls."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getRefPermanent().getController()
            LAND.filter(player) each {
                game.doAction(new TapAction(it))
            }
        }
    }
]
