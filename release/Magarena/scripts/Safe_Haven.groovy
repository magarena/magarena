[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(            
                permanent,
                new MagicMayChoice("Sacrifice SN?"),
                this,
                "PN may\$ sacrifice SN. If PN does, return each card exiled with SN to the battlefield under its owner's control"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.doAction(new ReturnLinkedExileAction(
                    event.getPermanent(),
                    MagicLocationType.Play,
                    event.getPlayer()
                ));
            }
        }
    }
]
