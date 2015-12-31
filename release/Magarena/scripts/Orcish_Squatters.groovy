def TARGET_LAND_DEFENDER_CONTROLS = new MagicTargetChoice("target land an opponent controls");

def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new AddStaticAction(
            event.getPermanent(),
            MagicStatic.ControlAsLongAsYouControlSource(event.getPlayer(), it)
        ));
    });
}

[
    new AttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Gain control of target land defending player controls?"),
                    creature,
                    this,
                    "PN may\$ gain control of target land defending player controls "+
                    "for as long as PN controls SN. If PN does, SN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(ChangeStateAction.Set(event.getRefPermanent(), MagicPermanentState.NoCombatDamage));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    TARGET_LAND_DEFENDER_CONTROLS,
                    action,
                    "PN gains control of target land\$ defending player controls for as long as "+
                    "PN controls SN."
                ));
            }
        }
    }
]
