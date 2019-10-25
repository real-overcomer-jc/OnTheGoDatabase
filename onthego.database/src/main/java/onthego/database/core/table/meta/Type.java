package onthego.database.core.table.meta;

import java.util.regex.Pattern;

public abstract class Type {
	
	protected TypeConstants typeConstant;
	
	protected int length;
	
	protected int decimalLength;
	
	protected String valuePattern;
	
	protected Pattern pattern;
	
	public static Type of(TypeConstants typeConstants, int length, int decimalLength) {
		if (typeConstants == TypeConstants.CHAR) {
			return new CharType(length);
		} else if (typeConstants == TypeConstants.VARCHAR) {
			return new VarcharType(length);
		} else if (typeConstants == TypeConstants.INTEGER) {
			return new IntegerType(length);
		} else if (typeConstants == TypeConstants.NUMERIC) {
			return new NumericType(length, decimalLength);
		} else if (typeConstants == TypeConstants.BOOL) {
			return new BoolType();
		} else if (typeConstants == TypeConstants.CONST) {
			return new ConstType();
		} else if (typeConstants == TypeConstants.NIL) {
			return new NullType();
		} else {
			throw new TypeException("Could not create a type with a wrong TypeConstants's value.");
		}
	}
	
	protected Type(TypeConstants typeConstant) {
		initialize(typeConstant, 0, 0);
	}
	
	protected Type(TypeConstants typeConstant, int length) {
		initialize(typeConstant, length, 0);
	}

	protected Type(TypeConstants typeConstant, int length, int decimalLength) {
		initialize(typeConstant, length, decimalLength);
	}
	
	private void initialize(TypeConstants typeConstant, int length, int decimalLength) {
		this.typeConstant = typeConstant;
		this.length = length;
		generateValuePattern(length, decimalLength);
	}
	
	protected abstract String generateValuePatternString(int length, int decimalLength);
	
	private void generateValuePattern(int length, int decimalLength) {
		String patternString = generateValuePatternString(length, decimalLength);
		if (patternString == null) {
			return;
		}
		
		this.pattern = Pattern.compile(patternString);
	}
	
	public boolean verifyValue(String value) {
		if (pattern == null || value == null || value.length() == 0) {
			return false;
		}
		
		return pattern.matcher(value).matches();
	}
	
	public TypeConstants getTypeConstant() {
		return typeConstant;
	}

	protected void setTypeConstant(TypeConstants typeConstant) {
		this.typeConstant = typeConstant;
	}

	public int getLength() {
		return length;
	}

	public int getDecimalLength() {
		return this.decimalLength;
	}
	
	@Override
	public String toString() {
		return typeConstant.getName();
	}
}