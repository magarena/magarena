def SAC_ACTION = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new MagicSacrificeAction(it));
        if (it.hasSubType(MagicSubType.Island)) { 
            game.doAction(new MagicDealDamageAction(event.getPermanent(),event.getPlayer(),3));
        }
    })
}

def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

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
            final MagicPermanent SN = event.getPermanent();
            final MagicPlayer PN = event.getPlayer();
            final MagicEvent sac = new MagicEvent(
                SN,
                PN,
                MagicTargetChoice.SACRIFICE_LAND,
                MagicSacrificeTargetPicker.create(),
                SAC_ACTION,
                "Choose a land to sacrifice\$."
            );
            if (PN.controlsPermanent(MagicType.Land)) {
                game.addEvent(sac);
            }
        }
    },
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(MagicType.Land) == false;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(
                EFFECT.getEvent(source)
            ));
        }
    }
]
