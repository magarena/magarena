[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature != permanent && creature.hasSubType(MagicSubType.Cat) && creature.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        "Pay {1}{G}{W}?"
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}{G}{W}"))
                    ),
                    creature,
                    this,
                    "PN may\$ pay {1}{G}{W}\$. If PN does, RN gains trample and gets +X/+X until end of turn, where X is its power."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent=event.getRefPermanent();
                final int amount = permanent.getPower();
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new GainAbilityAction(permanent, MagicAbility.Trample));
                game.doAction(new ChangeTurnPTAction(permanent, amount, amount));
            }
        }
    }
]
