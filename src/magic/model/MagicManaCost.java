package magic.model;

import magic.data.IconImages;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicManaCostCondition;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicManaCost {

    private static final Map<String,MagicManaCost> COSTS_MAP=new HashMap<String,MagicManaCost>();
    private static final Map<String,MagicCondition> CONDS_MAP=new HashMap<String,MagicCondition>();

    private static final Pattern PATTERN=Pattern.compile("\\{[A-Z\\d/]+\\}");

    private static final int SINGLE_PENALTY[]={0,1,1,3,6,9};
    private static final int DOUBLE_PENALTY[]={0,0,1,2,4,6};
    
    private static final ImageIcon COLORLESS_ICONS[]={
        IconImages.COST_ZERO,
        IconImages.COST_ONE,
        IconImages.COST_TWO,
        IconImages.COST_THREE,
        IconImages.COST_FOUR,
        IconImages.COST_FIVE,
        IconImages.COST_SIX,
        IconImages.COST_SEVEN,
        IconImages.COST_EIGHT,
        IconImages.COST_NINE,
        IconImages.COST_TEN,
        IconImages.COST_ELEVEN,
        IconImages.COST_TWELVE,
        IconImages.COST_THIRTEEN,
        IconImages.COST_FOURTEEN,
        IconImages.COST_FIFTEEN,
        IconImages.COST_SIXTEEN
    };
    
    public static final MagicManaCost X=MagicManaCost.create("{X}");
    public static final MagicManaCost ZERO=MagicManaCost.create("{0}");
    public static final MagicManaCost ONE=MagicManaCost.create("{1}");
    public static final MagicManaCost BLACK=MagicManaCost.create("{B}");
    public static final MagicManaCost BLUE=MagicManaCost.create("{U}");
    public static final MagicManaCost GREEN=MagicManaCost.create("{G}");
    public static final MagicManaCost RED=MagicManaCost.create("{R}");
    public static final MagicManaCost WHITE=MagicManaCost.create("{W}");

    public static final MagicManaCost TWO=MagicManaCost.create("{2}");
    public static final MagicManaCost THREE=MagicManaCost.create("{3}");
    public static final MagicManaCost FOUR=MagicManaCost.create("{4}");
    public static final MagicManaCost FIVE=MagicManaCost.create("{5}");
    public static final MagicManaCost EIGHT=MagicManaCost.create("{8}");
    public static final MagicManaCost ONE_BLUE=MagicManaCost.create("{1}{U}");
    public static final MagicManaCost TWO_BLUE=MagicManaCost.create("{2}{U}");
    public static final MagicManaCost THREE_BLUE=MagicManaCost.create("{3}{U}");
    public static final MagicManaCost BLUE_BLUE=MagicManaCost.create("{U}{U}");
    public static final MagicManaCost TWO_BLUE_BLUE=MagicManaCost.create("{2}{U}{U}");
    public static final MagicManaCost ONE_BLUE_RED=MagicManaCost.create("{1}{U}{R}");
    public static final MagicManaCost ONE_GREEN=MagicManaCost.create("{1}{G}");
    public static final MagicManaCost TWO_GREEN=MagicManaCost.create("{2}{G}");
    public static final MagicManaCost THREE_GREEN=MagicManaCost.create("{3}{G}");
    public static final MagicManaCost TWO_GREEN_GREEN=MagicManaCost.create("{2}{G}{G}");
    public static final MagicManaCost TWO_GREEN_GREEN_GREEN=MagicManaCost.create("{2}{G}{G}{G}");
    public static final MagicManaCost GREEN_GREEN=MagicManaCost.create("{G}{G}");
    public static final MagicManaCost X_RED=MagicManaCost.create("{X}{R}");
    public static final MagicManaCost ONE_RED=MagicManaCost.create("{1}{R}");
    public static final MagicManaCost TWO_RED=MagicManaCost.create("{2}{R}");
    public static final MagicManaCost THREE_RED=MagicManaCost.create("{3}{R}");
    public static final MagicManaCost FIVE_RED_RED=MagicManaCost.create("{5}{R}{R}");
    public static final MagicManaCost RED_RED=MagicManaCost.create("{R}{R}");
    public static final MagicManaCost TWO_RED_WHITE=MagicManaCost.create("{2}{R}{W}");
    public static final MagicManaCost THREE_RED_WHITE=MagicManaCost.create("{3}{R}{W}");
    public static final MagicManaCost ONE_WHITE=MagicManaCost.create("{1}{W}");
    public static final MagicManaCost TWO_WHITE=MagicManaCost.create("{2}{W}");
    public static final MagicManaCost SEVEN_WHITE=MagicManaCost.create("{7}{W}");
    public static final MagicManaCost WHITE_WHITE=MagicManaCost.create("{W}{W}");
    public static final MagicManaCost WHITE_BLUE=MagicManaCost.create("{W}{U}");
    public static final MagicManaCost ONE_WHITE_WHITE=MagicManaCost.create("{1}{W}{W}");
    public static final MagicManaCost X_WHITE=MagicManaCost.create("{X}{W}");
    public static final MagicManaCost BLACK_GREEN=MagicManaCost.create("{B}{G}");
    public static final MagicManaCost TWO_BLACK_GREEN=MagicManaCost.create("{2}{B}{G}");
    public static final MagicManaCost THREE_BLACK_GREEN=MagicManaCost.create("{3}{B}{G}");
    public static final MagicManaCost GREEN_WHITE=MagicManaCost.create("{G}{W}");
    public static final MagicManaCost TWO_GREEN_WHITE=MagicManaCost.create("{2}{G}{W}");
    public static final MagicManaCost THREE_GREEN_WHITE=MagicManaCost.create("{3}{G}{W}");
    public static final MagicManaCost BLACK_BLACK=MagicManaCost.create("{B}{B}");
    public static final MagicManaCost X_BLACK_BLACK=MagicManaCost.create("{X}{B}{B}");
    public static final MagicManaCost ONE_BLACK_BLACK=MagicManaCost.create("{1}{B}{B}");
    public static final MagicManaCost ONE_BLACK=MagicManaCost.create("{1}{B}");
    public static final MagicManaCost TWO_BLACK=MagicManaCost.create("{2}{B}");
    public static final MagicManaCost TWO_BLACK_BLACK=MagicManaCost.create("{2}{B}{B}");
    public static final MagicManaCost THREE_BLACK=MagicManaCost.create("{3}{B}");
    public static final MagicManaCost THREE_BLACK_BLACK=MagicManaCost.create("{3}{B}{B}");
    public static final MagicManaCost THREE_BLACK_RED=MagicManaCost.create("{3}{B}{R}");
    public static final MagicManaCost FOUR_BLACK_RED=MagicManaCost.create("{4}{B}{R}");
    public static final MagicManaCost ONE_WHITE_BLACK=MagicManaCost.create("{1}{W}{B}");
    public static final MagicManaCost TWO_WHITE_BLACK=MagicManaCost.create("{2}{W}{B}");
    public static final MagicManaCost THREE_WHITE_BLACK=MagicManaCost.create("{3}{W}{B}");
    public static final MagicManaCost RED_OR_WHITE=MagicManaCost.create("{R/W}");
    public static final MagicManaCost RED_WHITE=MagicManaCost.create("{R}{W}");
    public static final MagicManaCost BLACK_OR_RED=MagicManaCost.create("{B/R}");
    public static final MagicManaCost ONE_WHITE_OR_BLUE=MagicManaCost.create("{1}{W/U}");
    public static final MagicManaCost THREE_WHITE_BLUE=MagicManaCost.create("{3}{W}{U}");
    public static final MagicManaCost TWO_WHITE_WHITE_BLUE_BLUE=MagicManaCost.create("{2}{W}{W}{U}{U}");
    public static final MagicManaCost RED_GREEN=MagicManaCost.create("{R}{G}");
    public static final MagicManaCost X_RED_GREEN=MagicManaCost.create("{X}{R}{G}");
    public static final MagicManaCost ONE_RED_GREEN=MagicManaCost.create("{1}{R}{G}");
    public static final MagicManaCost TWO_RED_GREEN=MagicManaCost.create("{2}{R}{G}");
    public static final MagicManaCost ONE_RED_RED_GREEN_GREEN=MagicManaCost.create("{1}{R}{R}{G}{G}");
    public static final MagicManaCost ONE_BLUE_BLACK=MagicManaCost.create("{1}{U}{B}");
    public static final MagicManaCost TWO_BLUE_BLACK=MagicManaCost.create("{2}{U}{B}");
    public static final MagicManaCost BLUE_BLUE_BLACK_BLACK=MagicManaCost.create("{U}{U}{B}{B}");
    public static final MagicManaCost ONE_GREEN_WHITE=MagicManaCost.create("{1}{G}{W}");
    public static final MagicManaCost GREEN_GREEN_WHITE_WHITE=MagicManaCost.create("{G}{G}{W}{W}");
    public static final MagicManaCost WHITE_OR_BLACK=MagicManaCost.create("{W/B}");
    public static final MagicManaCost WHITE_OR_BLACK_WHITE_OR_BLACK=MagicManaCost.create("{W/B}{W/B}");

    private final String costText;
    private final int amounts[];
    private final int converted;
    private final int XCount;
    private final List<MagicCostManaType> order;
    private MagicBuilderManaCost builderCost;
    private List<ImageIcon> icons;

    private MagicManaCost(final String aCostText) {
        costText = aCostText;
        amounts = new int[MagicCostManaType.NR_OF_TYPES];
        order = new ArrayList<MagicCostManaType>();

        final int XCountArr[] = {0}; 
        final int convertedArr[] = {0};

        final Matcher matcher = PATTERN.matcher(costText);
        while (matcher.find()) {
            addType(matcher.group(), XCountArr, convertedArr);
        }
        
        XCount = XCountArr[0];
        converted = convertedArr[0];

        //assert getCanonicalText().equals(costText) : "canonical: " + getCanonicalText() + " != cost: " + costText;
    }
    
    private void addType(final MagicCostManaType type,final int amount,final int convertedArr[]) {
        convertedArr[0] += amount;
        amounts[type.ordinal()] += amount;
        if (!order.contains(type)) {
            order.add(type);
        }
    }
    
    private void addType(final String typeText, final int XCountArr[], final int convertedArr[]) {
        final String symbol = typeText.substring(1, typeText.length() - 1);
        if (symbol.equals("X")) {
            XCountArr[0]++;
        } else if (isNumeric(symbol)) {
            addType(MagicCostManaType.Colorless,Integer.parseInt(symbol),convertedArr);
        } else {
            for (final MagicCostManaType type : MagicCostManaType.values()) {
                if (type.getText().equals(typeText)) {
                    addType(type,1,convertedArr);
                    return;
                }
            }
            throw new RuntimeException("Invalid cost \"" + costText + "\"");
        }
    }
    
    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public String getText() {
        return costText;
    }
    
    @Override
    public String toString() {
        return costText;
    }
    
    public int getXCount() {
        return XCount;
    }
    public boolean hasX() {
        return XCount > 0;
    }
    
    public List<MagicCostManaType> getCostManaTypes(final int x) {
        final List<MagicCostManaType> types=new ArrayList<MagicCostManaType>();
        int colorless=x;

        for (final MagicCostManaType type : order) {
            int amount=amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless) {
                colorless+=amount;
            } else {
                for (;amount>0;amount--) {
                    types.add(type);
                }
            }
        }
        
        for (;colorless>0;colorless--) {
            types.add(MagicCostManaType.Colorless);
        }
        
        return types;
    }
        
    public int getColorFlags() {
        int colorFlags = 0;
        for (final MagicCostManaType costType : order) {
            if (costType != MagicCostManaType.Colorless) {
                for (final MagicManaType manaType : costType.getTypes()) {
                    colorFlags |= manaType.getColor().getMask();
                }
            }
        }
        return colorFlags;
    }
    
    private String getCanonicalText() {
        final StringBuilder sb = new StringBuilder();
        //add X
        for (int i = 0; i < XCount; i++) {
            sb.append('{').append('X').append('}');
        }
        //add others
        for (final MagicCostManaType type : getCanonicalOrder()) {
            final int amt = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless && amt > 0) {
                sb.append('{').append(amt).append('}');
                continue;
            }
            for (int i = 0; i < amt ; i++) {
                sb.append(type.getText());
            }
        }
        if (sb.length() == 0) {
            return "{0}";
        } else {
            return sb.toString();
        }
    }

    private List<MagicCostManaType> getCanonicalOrder() {
        final List<MagicCostManaType> order = new ArrayList<MagicCostManaType>();
        for (MagicCostManaType type : MagicCostManaType.NON_MONO) {
            final int amt = amounts[type.ordinal()];
            if (amt > 0) {
                order.add(type);
            }
        }

        //add mono
        MagicCostManaType curr = findFirstMonoSymbol();
        for (int i = 0; i < 5; i++, curr = curr.next()) {
            final int amt = amounts[curr.ordinal()];
            if (amt > 0) {
                order.add(curr);
            }
        }

        return order;
    }

    //find the first mono color in the mana cost order
    private MagicCostManaType findFirstMonoSymbol() {
        //keep color with amount > 0 and has no prev
        List<MagicCostManaType> cand = new ArrayList<MagicCostManaType>();
        for (final MagicCostManaType color : MagicCostManaType.MONO) {
            final int amt_c = amounts[color.ordinal()];
            final int amt_p = amounts[color.prev().ordinal()];
            if (amt_c > 0 && amt_p == 0) {
                cand.add(color);
            }
        }

        if (cand.size() == 0) {
            //WUBRG
            return MagicCostManaType.White;
        } else if (cand.size() == 1) {
            //Linear chain
            return cand.get(0);
        } else {
            //cand.size() == 2
            //Wedge
            final MagicCostManaType t1 = cand.get(0);
            final MagicCostManaType t2 = cand.get(1);
            return (t1.prev().prev() == t2) ? t2 : t1;
        }
    }

    private void buildIcons() {
        for (int x=XCount;x>0;x--) {
            icons.add(IconImages.COST_X);
        }
        for (final MagicCostManaType type : order) {
            int amount = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless) {
                while (amount > 16) {
                    icons.add(COLORLESS_ICONS[16]);
                    amount-=16;
                }
                if (amount > 0) {
                    icons.add(COLORLESS_ICONS[amount]);
                }
            } else {
                final ImageIcon icon = type.getIcon();
                for (int a=amount;a>0;a--) {
                    icons.add(icon);
                }
            }
        }                
    }
    
    public List<ImageIcon> getIcons() {
        if (icons == null) {
            icons=new ArrayList<ImageIcon>();
            buildIcons();
        }
        return icons;
    }
    
    int getCostScore(final MagicPlayerProfile profile) {
        final int singleCounts[]=new int[MagicManaType.NR_OF_TYPES];
        int doubleCount=0;
        int maxSingleCount=0;
        for (final MagicCostManaType type : order) {
            final int amount = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless || amount == 0) {
                continue;
            }
            final MagicManaType profileTypes[] = type.getTypes(profile);
            switch (profileTypes.length) {
                case 0:
                    return 0;
                case 1:
                    final int typeIndex=profileTypes[0].ordinal();
                    singleCounts[typeIndex]+=amount;
                    maxSingleCount=Math.max(maxSingleCount,singleCounts[typeIndex]);
                    break;
                case 2:
                    doubleCount+=amount;
                    break;
            }
        }
        return 2*converted+3*(10-SINGLE_PENALTY[maxSingleCount]-DOUBLE_PENALTY[doubleCount]);
    }
    
    public int getConvertedCost() {
        return converted;
    }
        
    public MagicBuilderManaCost getBuilderCost() {
        if (builderCost == null) {
            builderCost=new MagicBuilderManaCost();
            addTo(builderCost);
        }
        return builderCost;
    }
    
    public void addTo(final MagicBuilderManaCost aBuilderCost) {
        for (final MagicCostManaType type : order) {
            aBuilderCost.addType(type,amounts[type.ordinal()]);
        }
        if (hasX()) {
            aBuilderCost.setXCount(XCount);
        }    
        aBuilderCost.compress();
    }
    
    public void addTo(final MagicBuilderManaCost aBuilderCost,final int x) {
        for (final MagicCostManaType type : order) {
            aBuilderCost.addType(type,amounts[type.ordinal()]);
        }
        aBuilderCost.addType(MagicCostManaType.Colorless,x);
        aBuilderCost.compress();
    }
                
    public MagicCondition getCondition() {
        MagicCondition cond = CONDS_MAP.get(costText);
        if (cond == null) {
            cond = new MagicManaCostCondition(this);
            CONDS_MAP.put(costText, cond);
        }
        return cond;
    }
    
    public static MagicManaCost create(final String costText) {
        MagicManaCost cost = COSTS_MAP.get(costText);
        if (cost == null) {
            cost = new MagicManaCost(costText);
            COSTS_MAP.put(costText,cost);
        }
        return cost;
    }
}
