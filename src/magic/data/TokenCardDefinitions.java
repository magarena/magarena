package magic.data;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicType;
import magic.model.event.MagicSacrificeManaActivation;

import java.util.Arrays;
import java.util.List;

public class TokenCardDefinitions {

	public static final MagicCardDefinition ANGEL4_TOKEN_CARD = new MagicCardDefinition("Angel","Angel4") {
		public void initialize() {
			setToken();
			setValue(4);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Angel"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(4,4);
			setAbility(MagicAbility.Flying);
		}
	};
	
	public static final MagicCardDefinition APE_TOKEN_CARD = new MagicCardDefinition("Ape","Ape") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Ape"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(3,3);
		}
	};
	
	public static final MagicCardDefinition BAT_TOKEN_CARD = new MagicCardDefinition("Bat") {
		public void initialize() {
			setToken();
			setValue(2);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Bat"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColoredType();
			setPowerToughness(1,1);
			setAbility(MagicAbility.Flying);
		}
	};

	public static final MagicCardDefinition BEAST3_TOKEN_CARD = new MagicCardDefinition("Beast","Beast3") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Beast"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(3,3);
		}
	};
	
	public static final MagicCardDefinition BEAST4_TOKEN_CARD = new MagicCardDefinition("Beast","Beast4") {
		public void initialize() {
			setToken();
			setValue(4);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Beast"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(4,4);
		}
	};
	
	public static final MagicCardDefinition BEAST8_TOKEN_CARD = new MagicCardDefinition("Beast","Beast8") {
		public void initialize() {
			setToken();
			setValue(5);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Beast"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColor(MagicColor.Green);
			setColor(MagicColor.White);			
			setColoredType();
			setPowerToughness(8,8);
		}
	};
	
	public static final MagicCardDefinition BIRD_TOKEN_CARD = new MagicCardDefinition("Bird") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Bird"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(3,3);
			setAbility(MagicAbility.Flying);
		}
	};
	
	public static final MagicCardDefinition BIRD1_TOKEN_CARD = new MagicCardDefinition("Bird","Bird1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Bird"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(1,1);
			setAbility(MagicAbility.Flying);
		}
	};
	
	public static final MagicCardDefinition CAT2_TOKEN_CARD = new MagicCardDefinition("Cat","Cat2") {
		public void initialize() {
			setToken();
			setValue(2);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Cat"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(2,2);
		}
	};	

	public static final MagicCardDefinition DRAGON4_TOKEN_CARD = new MagicCardDefinition("Dragon","Dragon4") {
		public void initialize() {
			setToken();
			setValue(4);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Dragon"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColoredType();
			setPowerToughness(4,4);
			setAbility(MagicAbility.Flying);
		}
	};
	
	public static final MagicCardDefinition DRAGON5_TOKEN_CARD = new MagicCardDefinition("Dragon","Dragon5") {
		public void initialize() {
			setToken();
			setValue(5);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Dragon"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColoredType();
			setPowerToughness(5,5);
			setAbility(MagicAbility.Flying);
		}
	};

	public static final MagicCardDefinition ELDRAZI_SPAWN_TOKEN_CARD = new MagicCardDefinition("Eldrazi Spawn") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Eldrazi","Spawn"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(0,1);
			addManaActivation(new MagicSacrificeManaActivation(Arrays.asList(MagicManaType.Colorless)));
			setExcludeManaOrCombat();
		}
	};

	public static final MagicCardDefinition ELEPHANT_TOKEN_CARD = new MagicCardDefinition("Elephant") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Elephant"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(3,3);
		}
	};

	public static final MagicCardDefinition ELF_WARRIOR_TOKEN_CARD = new MagicCardDefinition("Elf Warrior", "Elf1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Elf","Warrior"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(1,1);
		}
	};
	
	public static final MagicCardDefinition FAERIE_ROGUE_TOKEN_CARD = new MagicCardDefinition("Faerie Rogue") {
		public void initialize() {
			setToken();
			setValue(2);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Faerie","Rogue"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColoredType();
			setPowerToughness(1,1);
			setAbility(MagicAbility.Flying);
		}
	};

	public static final MagicCardDefinition GERM_TOKEN_CARD = new MagicCardDefinition("Germ") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Germ"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColoredType();
			setPowerToughness(0,0);
		}
	};	
	
	public static final MagicCardDefinition GOBLIN_SOLDIER_TOKEN_CARD = new MagicCardDefinition("Goblin Soldier") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Goblin","Soldier"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(1,1);
		}
	};
	
	public static final MagicCardDefinition GOBLIN1_TOKEN_CARD = new MagicCardDefinition("Goblin","Goblin1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Goblin"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColoredType();
			setPowerToughness(1,1);
		}
	};

	public static final MagicCardDefinition GOBLIN2_TOKEN_CARD = new MagicCardDefinition("Goblin","Goblin2") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Goblin"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColoredType();
			setPowerToughness(2,1);
			setAbility(MagicAbility.Haste);
		}
	};	
	
	public static final MagicCardDefinition GOLEM3_ARTIFACT_TOKEN_CARD = new MagicCardDefinition("Golem","Golem3") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Artifact);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Golem"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(3,3);
		}
	};	
	
	public static final MagicCardDefinition GRIFFIN_TOKEN_CARD = new MagicCardDefinition("Griffin") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Griffin"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(2,2);
			setAbility(MagicAbility.Flying);
		}
	};	
	
	public static final MagicCardDefinition INSECT1_TOKEN_CARD = new MagicCardDefinition("Insect","Insect1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Insect"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(1,1);
			setAbility(MagicAbility.Infect);
		}
	};
	
	public static final MagicCardDefinition MERFOLK_WIZARD_TOKEN_CARD = new MagicCardDefinition("Merfolk Wizard") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Merfolk","Wizard"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Blue);
			setColoredType();
			setPowerToughness(1,1);
		}
	};
	
	public static final MagicCardDefinition MYR1_TOKEN_CARD = new MagicCardDefinition("Myr","Myr1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Artifact);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Myr"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(1,1);
		}
	};
	
	public static final MagicCardDefinition OGRE_TOKEN_CARD = new MagicCardDefinition("Ogre") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Ogre"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Red);
			setColoredType();
			setPowerToughness(3,3);
		}
	};
	
	public static final MagicCardDefinition PLANT_TOKEN_CARD = new MagicCardDefinition("Plant") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Plant"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(0,1);
		}
	};	
	
	public static final MagicCardDefinition SAPROLING_TOKEN_CARD = new MagicCardDefinition("Saproling") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Saproling"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(1,1);
		}
	};	
	
	public static final MagicCardDefinition SNAKE_TOKEN_CARD = new MagicCardDefinition("Snake") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Snake"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(1,1);
		}
	};	
	
	public static final MagicCardDefinition SOLDIER_TOKEN_CARD = new MagicCardDefinition("Soldier") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Soldier"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(1,1);
		}
	};	

	public static final MagicCardDefinition SPIRIT1_TOKEN_CARD = new MagicCardDefinition("Spirit","Spirit1") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Spirit"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(1,1);
		}
	};	
	
	public static final MagicCardDefinition SPIRIT2_TOKEN_CARD = new MagicCardDefinition("Spirit","Spirit2") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Spirit"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(1,1);
			setAbility(MagicAbility.Flying);
		}
	};
	
	public static final MagicCardDefinition TUKTUK_THE_RETURNED_TOKEN_CARD = new MagicCardDefinition("Tuktuk the Returned") {
		public void initialize() {
			setToken();
			setValue(4);
			addType(MagicType.Legendary);
			addType(MagicType.Artifact);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Goblin","Golem"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(5,5);
		}
	};

	public static final MagicCardDefinition VOJA_TOKEN_CARD = new MagicCardDefinition("Voja") {
		public void initialize() {
			setToken();
			setValue(2);
			addType(MagicType.Legendary);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Wolf"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColor(MagicColor.White);
			setColoredType();
			setPowerToughness(2,2);
		}
	};
	
	public static final MagicCardDefinition WOLF_TOKEN_CARD = new MagicCardDefinition("Wolf") {
		public void initialize() {

			setToken();
			setValue(2);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Wolf"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(2,2);
		}
	};

	public static final MagicCardDefinition WORM_TOKEN_CARD = new MagicCardDefinition("Worm") {
		public void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Worm"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColor(MagicColor.Green);
			setColoredType();
			setPowerToughness(1,1);
		}
	};
	
	public static final MagicCardDefinition WURM1_TOKEN_CARD = new MagicCardDefinition("Wurm","Wurm1") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Artifact);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Wurm"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(3,3);
			setAbility(MagicAbility.Deathtouch);
		}
	};	
	
	public static final MagicCardDefinition WURM2_TOKEN_CARD = new MagicCardDefinition("Wurm","Wurm2") {
		public void initialize() {
			setToken();
			setValue(3);
			addType(MagicType.Artifact);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Wurm"});
			setCost(MagicManaCost.ZERO);
			setColoredType();
			setPowerToughness(3,3);
			setAbility(MagicAbility.LifeLink);
		}
	};

	public static final MagicCardDefinition ZOMBIE_GIANT_TOKEN_CARD = new MagicCardDefinition("Zombie Giant") {
		public void initialize() {
			setToken();
			setValue(4);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Zombie","Giant"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColoredType();
			setPowerToughness(5,5);
		}
	};
	
	public static final MagicCardDefinition ZOMBIE_TOKEN_CARD = new MagicCardDefinition("Zombie") {
		public void initialize() {
			setToken();
			setValue(2);
			addType(MagicType.Creature);
			setSubTypes(new String[]{"Zombie"});
			setCost(MagicManaCost.ZERO);
			setColor(MagicColor.Black);
			setColoredType();
			setPowerToughness(2,2);
		}
	};
	
	public static final List<MagicCardDefinition> TOKEN_CARDS = Arrays.asList(
			ANGEL4_TOKEN_CARD,
			APE_TOKEN_CARD,
			BAT_TOKEN_CARD,
			BEAST3_TOKEN_CARD,
			BEAST4_TOKEN_CARD,
			BEAST8_TOKEN_CARD,
			BIRD_TOKEN_CARD,
			BIRD1_TOKEN_CARD,
			CAT2_TOKEN_CARD,
			DRAGON4_TOKEN_CARD,
			DRAGON5_TOKEN_CARD,
			ELDRAZI_SPAWN_TOKEN_CARD,
			ELEPHANT_TOKEN_CARD,
			ELF_WARRIOR_TOKEN_CARD,
			FAERIE_ROGUE_TOKEN_CARD,
			GERM_TOKEN_CARD,
			GOBLIN1_TOKEN_CARD,
			GOBLIN2_TOKEN_CARD,
			GOBLIN_SOLDIER_TOKEN_CARD,
			GOLEM3_ARTIFACT_TOKEN_CARD,
			GRIFFIN_TOKEN_CARD,
			INSECT1_TOKEN_CARD,
			MERFOLK_WIZARD_TOKEN_CARD,
			MYR1_TOKEN_CARD,
			OGRE_TOKEN_CARD,
			PLANT_TOKEN_CARD,
			SAPROLING_TOKEN_CARD,
			SNAKE_TOKEN_CARD,
			SOLDIER_TOKEN_CARD,
			SPIRIT1_TOKEN_CARD,
			SPIRIT2_TOKEN_CARD,
			TUKTUK_THE_RETURNED_TOKEN_CARD,
			VOJA_TOKEN_CARD,
			WOLF_TOKEN_CARD,
			WORM_TOKEN_CARD,
			WURM1_TOKEN_CARD,
			WURM2_TOKEN_CARD,
			ZOMBIE_GIANT_TOKEN_CARD,
			ZOMBIE_TOKEN_CARD
		);
}
