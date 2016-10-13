[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            final int amount = spell.getConvertedCost();
            return spell.isFriend(permanent) && spell.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN creates X 1/1 black Thrull creature tokens, where X is ("+spell.getName()+")'s converted mana cost. (X="+amount+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 black Thrull creature token"),
                event.getRefInt()
            ));
        }
    }
]
