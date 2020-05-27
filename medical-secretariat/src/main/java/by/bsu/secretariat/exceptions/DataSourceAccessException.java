package by.bsu.secretariat.exceptions;

public class DataSourceAccessException extends DataSourceException {
    public DataSourceAccessException(){
        super();
    }

    public DataSourceAccessException(String message){
        super(message);
    }
}