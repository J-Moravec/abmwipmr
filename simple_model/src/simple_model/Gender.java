package simple_model;

public enum Gender {
	MALE, FEMALE;
	
	public static Gender other(Gender gender){
		Gender other_gender;
		if(gender == Gender.MALE){
			other_gender = Gender.FEMALE;
		} else {
			other_gender = Gender.MALE;
		}
		return other_gender;
	}
	
	
}
