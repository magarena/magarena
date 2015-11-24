[
    new SpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice("Sacrifice a land?"),
                this,
                "PN may\$ sacrifice a land. If PN doesn't, counter SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicType.Land) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), event.getPlayer(), SACRIFICE_LAND));
            } else {
                game.doAction(new CounterItemOnStackAction(event.getCardOnStack()));
            }
        }
    },
    
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN sacrifices a land."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(event.getSource(), event.getPlayer(), SACRIFICE_LAND));
        }
    }
]
