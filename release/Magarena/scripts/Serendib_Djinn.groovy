def SAC_ACTION = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new SacrificeAction(it));
        if (it.hasSubType(MagicSubType.Island)) { 
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),3));
        }
    })
}

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice a land. If you sacrifice an Island this way, SN deals 3 damage to you."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                SACRIFICE_LAND,
                SAC_ACTION
            );
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            }
        }
    }
]
