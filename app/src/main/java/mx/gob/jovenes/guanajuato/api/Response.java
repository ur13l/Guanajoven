package mx.gob.jovenes.guanajuato.api;

public class Response<T> {
    public boolean success;
    public String[] errors;
    public int status;
    public T data;
}
