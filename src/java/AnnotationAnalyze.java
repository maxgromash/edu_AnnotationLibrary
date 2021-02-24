import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationAnalyze {

    /*Все методы проверяют соответствие значения определенной аннотации
      В случае ошибки - возвращают Pair из текста ошибки и значения, не прошедшего проверку
      Если ошибки не возникает - функция возвращает null
      Также во всех методах реализована проверка на соответствие поддерживаемому типу объекта
      Пример сообщения: "@AnnotationName wrong type"
     */
    public static Pair validNotNull(Object object) {
        if (object != null)
            return null;
        else
            return new Pair("must not be null", object);
    }

    public static Pair validPositive(Object object) {
        if (object == null)
            return null;
        if (isNumberCheck(object)) {
            if (((Number) object).longValue() >= 0)
                return null;
            else
                return new Pair("must be positive", object);
        } else
            return new Pair("@Positive wrong type", object);
    }

    public static Pair validNegative(Object object) {
        if (object == null)
            return null;
        if (isNumberCheck(object)) {
            if (((Number) object).longValue() < 0)
                return null;
            else
                return new Pair("must be negative", object);
        } else
            return new Pair("@Negative wrong type", object);
    }

    public static Pair validInRange(Object object, long min, long max) {
        if (object == null)
            return null;
        if (isNumberCheck(object)) {
            long x = ((Number) object).longValue();
            if (x <= max && x >= min)
                return null;
            else
                return new Pair("must be in range between " + min + " and " + max, object);
        } else
            return new Pair("@InRange wrong type", object);

    }

    public static Pair validSize(Object object, int min, int max) {
        if (object == null)
            return null;
        switch (isCollectionOrStringCheck(object)) {
            case 1: {
                if (((Collection) object).size() >= min && ((Collection) object).size() <= max)
                    return null;
                else
                    return new Pair("must be in range between " + min + " and " + max, object);
            }
            case 3: {
                if (((Map) object).size() >= min && ((Map) object).size() <= max)
                    return null;
                else
                    return new Pair("must be in range between " + min + " and " + max, object);
            }
            case 4: {
                if (((String) object).length() >= min && ((String) object).length() <= max)
                    return null;
                else
                    return new Pair("must be in range between " + min + " and " + max, object);
            }
            default:
                return new Pair("@Size wrong type", object);
        }
    }

    public static Pair validNotEmpty(Object object) {
        if (object == null)
            return null;
        switch (isCollectionOrStringCheck(object)) {
            case 1: {
                if (((Collection) object).isEmpty())
                    return new Pair("must not be empty", object);
                else
                    return null;
            }
            case 3: {
                if (((Map) object).isEmpty())
                    return new Pair("must not be empty", object);
                else
                    return null;
            }
            case 4:
                if (((String) object).isEmpty())
                    return new Pair("must not be empty", object);
                else
                    return null;
            default:
                return new Pair("@NotEmpty wrong type", object);
        }
    }

    public static Pair validNotBlank(Object object) {
        if (object == null)
            return null;
        if (object instanceof String) {
            if (!((String) object).isBlank())
                return null;
            else
                return new Pair("must not be blank", object);
        } else
            return new Pair("@NotBlank wrong type", object);
    }

    public static Pair validAnyOf(Object object, String[] value) {
        if (object == null)
            return null;
        if (object instanceof String) {
            String key = (String) object;
            for (String s : value)
                if (s.equals(key))
                    return null;

            StringBuilder error = new StringBuilder();
            error.append("must be one of ");
            for (String s : value)
                error.append("'" + s + "' ");

            return new Pair(error.toString(), object);

        } else
            return new Pair("@AnyOf wrong type", object);

    }

    private static boolean isNumberCheck(Object object) {
        switch (object.getClass().getSimpleName()) {
            case "Integer":
            case "Byte":
            case "Short":
            case "Long":
                return true;
            default:
                return false;
        }
    }

    private static int isCollectionOrStringCheck(Object object) {
        if (object instanceof List || object instanceof Set)
            return 1;
        if (object instanceof Map)
            return 3;
        if (object instanceof String)
            return 4;
        return 0;
    }
}