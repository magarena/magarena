[
    new BecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return attacker == permanent.getEnchantedPermanent() ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent().getOpponent(),
                    this,
                    "Tap all lands PN controls."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            LAND.filter(event.getPlayer()) each {
                game.doAction(new TapAction(it));
            }
        }
    }
]
