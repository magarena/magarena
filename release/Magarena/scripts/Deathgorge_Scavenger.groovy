def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetCard(game, {
            if (it.hasType(MagicType.Creature) {
                game.doAction(new ChangeLifeAction(event.getPlayer(), 2));
            } else {
                game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 1, 1));
            }
        });
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicTargetChoice("target card from a graveyard")),
                action,
                "PN may\$ exile target card from a graveyard\$. " +
                "If a creature card is exiled this way, PN gains 2 life. " +
                "If a noncreature card is exiled this way, SN gets +1/+1 until end of turn."
            );
        }
    },
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicTargetChoice("target card from a graveyard")),
                action,
                "PN may\$ exile target card from a graveyard\$. " +
                "If a creature card is exiled this way, PN gains 2 life. " +
                "If a noncreature card is exiled this way, SN gets +1/+1 until end of turn."
            );
        }
    }
]

