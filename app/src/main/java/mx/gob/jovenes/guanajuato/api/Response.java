package mx.gob.jovenes.guanajuato.api;

/**
 * Created by code on 7/03/17.
 */

public class Response<T> {
    public boolean success;
    public String[] errors;
    public int status;
    public T data;

}
