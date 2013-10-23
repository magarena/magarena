def NONTOKEN_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && !target.isToken();
        }
    };
[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    NONTOKEN_CREATURE,
                    permanent
                ),
                MagicTargetHint.None,
                "another target nontoken creature to exile"
            );
			return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another target nontoken creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source = event.getPermanent();
			final MagicCardList exiled = source.getExiledCards();			
			if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
						game.doAction(new MagicRemoveFromPlayAction(target, MagicLocationType.Exile));
						exiled.addToTop(target.getCard());
					}
                });
            }else{
				exiled.addToTop(MagicCard.NONE);
			}
        }
    },
	new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicCardList exiled = permanent.getExiledCards();
			final MagicCard card = exiled.getCardAtTop();
			if (card != MagicCard.NONE) {
                pt.add(card.getPower(),card.getToughness());
            }
        }
    }
]
