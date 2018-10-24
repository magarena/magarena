[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isValid() ?
                new MagicEvent(
                    permanent,
                    this,
                    "If SN is on the battlefield, each player returns all cards named Myr Servitor from his or her graveyard to the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent SN = event.getPermanent();
            if (SN.isValid()) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final List<MagicCard> graveyard = cardName("Myr Servitor").from(MagicTargetType.Graveyard).filter(player);
                for (final MagicCard card : graveyard) {
                    game.doAction(new ReanimateAction(card, player));
                    }
                }
            }
        }
    }
]
