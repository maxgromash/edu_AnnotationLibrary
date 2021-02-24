import annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyValidator implements Validator {

    private StringBuilder currentPath = new StringBuilder();
    HashSet<ValidationError> validationErrors = new HashSet<>();

    @Override
    public Set<ValidationError> validate(Object object) {

        if (object.getClass().getAnnotation(Constrained.class) == null)
            return null;

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {

            currentPath.append(field.getName());
            field.setAccessible(true);

            //Достаём сам объект из поля
            Object inner = null;
            try {
                inner = field.get(object);
            } catch (IllegalAccessException ignored) {
            }

            //Проверяем на null
            if (nullCheck(inner, field)) {
                fixPathLoop();
                continue;
            }

            //Получаем список аннотация и проверяем их
            var annotations = field.getAnnotatedType().getAnnotations();
            for (Annotation annot : annotations) {
                Pair response = annotationIdentification(annot, inner);
                if (response != null)
                    validationErrors.add(new MyValidationError(response, currentPath.toString()));
            }

            //Обработка случаев, когда поле List или класс имеет аннотацию @Constrained
            if (inner != null) {
                constrainedCheck(inner);
                listCheck(inner, field);
            }

            //Преобразуем путь
            fixPathLoop();
        }
        return validationErrors;
    }


    private void fixPathLoop() {
        if (currentPath.indexOf(".") != -1)
            currentPath.delete(currentPath.lastIndexOf(".") + 1, currentPath.length());
        else
            currentPath = new StringBuilder();
    }

    /**
     * Проверка на соответствие аннотации @NotNull
     *
     * @param inner Объект внутри поля
     * @param field Поле
     * @return True, если объект null и имеет аннотацию @NotNull
     */
    private boolean nullCheck(Object inner, Field field) {
        if (inner == null && field.getAnnotatedType().isAnnotationPresent(NotNull.class)) {
            validationErrors.add(new MyValidationError(new Pair("must not be null", null),
                    currentPath.toString()));
            return true;
        }
        return false;
    }

    /**
     * Метод проверяет класс на Constrained, в случае соответствия запускает рекурсиввную проверка
     *
     * @param inner Объект поля
     */
    private void constrainedCheck(Object inner) {
        //Если поле - Constrained класс, рекурсивно запускаем его проверку и получаем ошибки
        if (inner != null && inner.getClass().getAnnotation(Constrained.class) != null) {
            currentPath.append(".");
            validationErrors.addAll(validate(inner));
            currentPath.delete(currentPath.lastIndexOf("."), currentPath.length());
        }
    }

    /**
     * Метод проверяет является ли поле List и
     * проверяет её содержимое на соответствие аннотациям внутри
     *
     * @param inner Объект поля
     * @param field Поле
     */
    private void listCheck(Object inner, Field field) {
        //Если пришёл список - проверяем дженерик
        if (inner.getClass().getSimpleName().startsWith("List") && inner instanceof List) {
            List innerList = (List) inner;
            var annotations = ((AnnotatedParameterizedType) field.getAnnotatedType()).
                    getAnnotatedActualTypeArguments()[0].getAnnotations();

            //Пробегаем по листу и проверяем на аннотации в дженерике
            for (int i = 0; i < innerList.size(); i++) {
                currentPath.append("[").append(i).append("]");

                if (nullCheck(innerList.get(i), field))
                    continue;

                for (Annotation annotation : annotations) {
                    Pair responseMember = annotationIdentification(annotation, innerList.get(i));
                    //Если нашли ошщибку
                    if (responseMember != null) {
                        validationErrors.add(new MyValidationError(responseMember, currentPath.toString()));
                    }
                }

                //Запускаем проверку каждого
                constrainedCheck(innerList.get(i));

                currentPath.delete(currentPath.lastIndexOf("["), currentPath.length());
            }
        }
    }


    /**
     * Метод проверяет объект на соответствие аннотации
     *
     * @param annotation аннотация для проверки
     * @param object     объект для проверки
     * @return Ошибка, если она произошла
     */
    private Pair annotationIdentification(Annotation annotation, Object object) {
        if (annotation instanceof NotNull)
            return AnnotationAnalyze.validNotNull(object);
        else if (annotation instanceof Positive)
            return AnnotationAnalyze.validPositive(object);
        else if (annotation instanceof Negative)
            return AnnotationAnalyze.validNegative(object);
        else if (annotation instanceof NotBlank)
            return AnnotationAnalyze.validNotBlank(object);
        else if (annotation instanceof NotEmpty)
            return AnnotationAnalyze.validNotEmpty(object);
        else if (annotation instanceof Size)
            return AnnotationAnalyze.validSize(object, ((Size) annotation).min(), ((Size) annotation).max());
        else if (annotation instanceof InRange)
            return AnnotationAnalyze.validInRange(object, ((InRange) annotation).min(), ((InRange) annotation).max());
        else if (annotation instanceof AnyOf)
            return AnnotationAnalyze.validAnyOf(object, ((AnyOf) annotation).value());
        return null;
    }
}