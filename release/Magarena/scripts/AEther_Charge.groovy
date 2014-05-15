[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.hasSubType(MagicSubType.Beast) &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.TARGET_OPPONENT),
                    otherPermanent,
                    this,
                    "You may\$ have RN deal 4 damage to "+permanent.getController().getOpponent().getName()
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game,{
                    final MagicPlayer player ->
                    final MagicDamage damage = new MagicDamage(event.getRefPermanent(),player,4);
                    game.doAction(new MagicDealDamageAction(damage));
                });
            }
        }
    }
]
