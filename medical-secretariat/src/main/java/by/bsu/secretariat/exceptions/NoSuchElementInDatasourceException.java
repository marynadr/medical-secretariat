package by.bsu.secretariat.exceptions;

public class NoSuchElementInDatasourceException extends DataSourceException {
    public NoSuchElementInDatasourceException(){
        super();
    }

    public NoSuchElementInDatasourceException(String message){
        super(message);
    }
}
