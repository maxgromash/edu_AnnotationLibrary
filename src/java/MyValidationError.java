import java.util.Objects;

public class MyValidationError implements ValidationError {

    public MyValidationError(Pair info, String path) {
        Message = info.getMessage();
        FailedValue = info.getFailedValue();
        Path = path;
    }

    private final String Message;
    private final String Path;
    private final Object FailedValue;

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public String getPath() {
        return Path;
    }

    @Override
    public Object getFailedValue() {
        return FailedValue;
    }

    //Переопределяем equals() и hashCode() для сравнения ошибок в тестировании
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyValidationError error = (MyValidationError) o;
        return Objects.equals(Message, error.Message) && Objects.equals(Path, error.Path) && Objects.equals(FailedValue, error.FailedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Message, Path, FailedValue);
    }
}
