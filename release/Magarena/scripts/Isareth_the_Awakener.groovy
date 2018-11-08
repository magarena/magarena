def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCard card = event.getRefCard();
    final int cost = card.getConvertedCost();
    if (event.isYes()) {
        final MagicEvent manaCost = new MagicPayManaCostEvent(event.getSource(), "{${cost}}");
        if (manaCost.isSatisfied()) {
            game.addEvent(manaCost);
            game.doAction(new ReturnCardAction(
                MagicLocationType.Graveyard,
                card,
                event.getPlayer(),
                MagicPlayMod.CORPSE_COUNTER,
                MagicPlayMod.EXILE_WHEN_LEAVES
            ));
        }
    }
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                this,
                "Choose target creature card from your graveyard.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final int cost = it.getConvertedCost();
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Pay {${cost}}?"),
                    it,
                    action,
                    "PN may\$ pay {${cost}}. When PN does, PN returns target creature card with converted mana cost X from PN's graveyard to the battlefield with a corpse counter on it. If that creature would leave the battlefield, exile it instead of putting it anywhere else."
                ));
            });
        }
    }
]
