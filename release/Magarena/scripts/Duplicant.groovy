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
            if (event.isYes()) {
                final MagicPermanent source = event.getPermanent();
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicRemoveFromPlayAction(target, MagicLocationType.Exile));
                        source.addExiledCard(target.getCard());
                    }
                });
            }
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            final MagicPermanent left = act.getPermanent();
            if(left == permanent){
                MagicCardList exiled = permanent.getExiledCards();
                if(exiled.getCardAtTop() != MagicCard.NONE){
                    exiled.removeCardAtTop();
                }
            }
            return MagicEvent.NONE;
        }
    },
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicCardList exiled = permanent.getExiledCards();
            final MagicCard card = exiled.getCardAtTop();
            if (card != MagicCard.NONE) {
                pt.set(card.getPower(),card.getToughness());
            }
        }
    }, 
    new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            final MagicCardList exiled = permanent.getExiledCards();
            final MagicCard card = exiled.getCardAtTop();
            if (card != MagicCard.NONE) {
                flags.addAll(card.getCardDefinition().genSubTypeFlags());
            }
        }
    }
]
