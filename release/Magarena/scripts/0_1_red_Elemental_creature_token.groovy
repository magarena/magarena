[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return a creature card named Rekindling Phoenix from PN's graveyard to the battlefield under his or her control. "+
                "It gains haste until end of turn."

            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new SacrificeAction(event.getPermanent()));
            CARD_FROM_GRAVEYARD.filter(event) find {
                if (it.getName().equals("Rekindling Phoenix")) {
                    game.doAction(new ReanimateAction(it, event.getPlayer(),[MagicPlayMod.HASTE_UEOT]));
                    return true;
                }
            }
        }
    }
]
