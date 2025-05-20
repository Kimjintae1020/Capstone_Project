package JMP.JMP.Enum;

public enum Major {
    CHRISTIANITY("기독교학부"),
    LANGUAGE_CULTURE("어문학부"),
    SOCIAL_WELFARE("사회복지학부"),
    POLICE_SCIENCE("경찰학부"),
    TOURISM("관광학부"),
    EDUCATION("사범학부"),
    EARLY_CHILDHOOD_EDUCATION("유아교육과"),
    SPECIAL_EDUCATION("특수교육과"),
    EARLY_CHILDHOOD_SPECIAL_EDUCATION("유아특수교육과"),
    SPECIAL_PHYSICAL_EDUCATION("특수체육교육과"),
    COMPUTER_SCIENCE("컴퓨터공학부"),
    ADVANCED_IT("첨단IT학부"),
    HEALTH_SCIENCE("보건학부"),
    PHYSICAL_THERAPY("물리치료학과"),
    OPTOMETRY("안경광학과"),
    EMERGENCY_MEDICAL_SERVICE("응급구조학과"),
    NURSING("간호학과"),
    DENTAL_HYGIENE("치위생학과"),
    OCCUPATIONAL_THERAPY("작업치료학과"),
    DESIGN_MEDIA("디자인영상학부"),
    SPORTS_SCIENCE("스포츠과학부"),
    CULTURAL_ARTS("문화예술학부"),
    FOOD_SERVICE_INDUSTRY("외식산업학부"),
    INNOVATION_CONVERGENCE("혁신융합학부"),
    LIBERAL_ARTS("자유전공학부"),
    INTERNATIONAL_STUDIES("국제학부");

    private final String displayName;

    Major(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
