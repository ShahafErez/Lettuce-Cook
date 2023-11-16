package utils;

import com.shahaf.lettucecook.exceptions.BadRequestException;

public final class EnumConverter {
    private EnumConverter(){
        throw new IllegalStateException("Utility class");
    }
    public static <T extends Enum<T>> T stringToEnum(String value, Class<T> enumType) {
        T enumValue = null;
        if (value != null) {
            try {
                enumValue = Enum.valueOf(enumType, value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(String.format("Value %s does not match the available options", value));
            }
        }
        return enumValue;
    }
}
