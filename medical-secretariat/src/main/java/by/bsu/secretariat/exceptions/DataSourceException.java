package by.bsu.secretariat.exceptions;

public class DataSourceException extends RuntimeException {
    public DataSourceException(){
        super();
    }

    public DataSourceException(String message){
        super(message);
    }
}
