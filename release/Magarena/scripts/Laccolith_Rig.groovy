[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (blocked == enchanted) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(enchanted.getPower()),
                    blocked,
                    this,
                    "PN may\$ have RN deal damage equal to its power to target creature.\$ If PN does, RN assigns no combat damage this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent = event.getPermanent().getEnchantedPermanent();
                    final int amount = permanent.getPower();
                    game.logAppendMessage(event.getPlayer(),"("+amount+")")
                    game.doAction(new DealDamageAction(permanent,it,amount));
                    game.doAction(ChangeStateAction.Set(permanent,MagicPermanentState.NoCombatDamage));
                });
            }
        }
    }
]
