package fallofrome.game;

public enum Allegiance {
    ROMAN,               // Loyal Roman
    REBELLIOUS_ROMAN,    // Is this needed?
    PERSIAN,
    GERMAN_BARBARIAN,
    SCYTHIAN_BARBARIAN,
    TAURICAN_BARBARIAN,
    DACIAN_BARBARIAN,
    PICTISH_BARBARIAN,
    HUN_BARBARIAN,
    INDEPENDENT_STATE,
    REVOLTING_MILITIA;

    public boolean isRoman(){
        return this == ROMAN || this == REBELLIOUS_ROMAN;
    }

    public boolean isNonRoman(){
        return !isRoman();
    }

    public boolean isNonLoyalRoman(){
        return (this != ROMAN);
    }

    public boolean isBarbarian(){
        return this == GERMAN_BARBARIAN || this == SCYTHIAN_BARBARIAN || this == TAURICAN_BARBARIAN || this == DACIAN_BARBARIAN || this == PICTISH_BARBARIAN || this == HUN_BARBARIAN;
    }
}
