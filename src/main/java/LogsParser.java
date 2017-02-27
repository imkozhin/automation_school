import java.util.Date;
import java.util.List;
import org.openqa.selenium.logging.LogEntry;

class LogsParser {

    private List<LogEntry> logEntryList;
    private Date startTime;

    //Конструктор принимает массив лога и дату с кототорой начать поиск
    LogsParser(List<LogEntry> log, Date date){
        logEntryList = log;
        startTime = date;
    }

    //поиск строки по логам
    boolean FindStringInLog(String findString ){
        //перебор массива
        for(LogEntry l :  logEntryList ){
            //фильтрую по тегу ReportManager и по времени тапа
            if(l.getTimestamp() > startTime.getTime()
                    && l.getMessage().contains("D/[Ya:ReportManager]")){
                //ищу строку
                if(l.getMessage().contains(findString)){
                    //совпадение найдено, отдаём на выходе true
                    return true;
                }
            }
        }
        //не нашли
        return false;
    }

    //Поиск строки по логам
    public boolean findStringInLogWithoutTimer(String findString ){

        //перебор массива
        for(LogEntry l :  logEntryList ){
            //фильтрую по тегу ReportManager
            if(l.getMessage().contains("D/[Ya:ReportManager]")){
                //ищу строку
                if(l.getMessage().contains(findString)){
                    //совпадение найдено, отдаём на выходе true
                    return true;
                }
            }
        }
        //не нашли
        return false;
    }

}