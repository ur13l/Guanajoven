package mx.gob.jovenes.guanajuato.model;

import java.util.List;

/**
 * Created by codigus on 28/07/2017.
 */

public class DatosMensajes {
    private int total;
    private int perPage;
    private int currentPage;
    private int lastPage;
    private String nextPageUrl;
    private String prevPageUrl;
    private int from;
    private int to;
    private List<Mensaje> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<Mensaje> getData() {
        return data;
    }

    public void setData(List<Mensaje> mensajes) {
        this.data = mensajes;
    }
}
