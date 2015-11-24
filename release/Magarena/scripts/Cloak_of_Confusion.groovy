[
    new AttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    creature,
                    this,
                    "PN may\$ have RN assign no combat damage this turn. If PN does, defending player discards a card at random."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(ChangeStateAction.Set(event.getRefPermanent(), MagicPermanentState.NoCombatDamage));
                game.addEvent(MagicDiscardEvent.Random(event.getSource(), game.getDefendingPlayer(), 1));
            }
        }
    }
]
