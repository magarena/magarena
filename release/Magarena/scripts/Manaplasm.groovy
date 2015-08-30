[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            final int amount = spell.getConvertedCost();
            return spell.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN gets +X/+X until end of turn, where X is ("+spell.getName()+")'s converted mana cost. (X="+amount+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),amount,amount));
        }
    }
]
